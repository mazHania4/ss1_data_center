
# VPC
resource "aws_vpc" "eks_vpc" {
  cidr_block           = "10.0.0.0/16"
  enable_dns_support   = true
  enable_dns_hostnames = true

  tags = {
    Name        = "${var.project_prefix}-vpc"
    Environment = "dev"
  }
}

# Public subnets (one per AZ)
resource "aws_subnet" "public" {
  count = var.az_count

  vpc_id                  = aws_vpc.eks_vpc.id
  cidr_block              = cidrsubnet(aws_vpc.eks_vpc.cidr_block, 8, count.index) # /24 blocks
  availability_zone       = data.aws_availability_zones.available.names[count.index]
  map_public_ip_on_launch = true

  tags = {
    Name                                          = "${var.project_prefix}-public-${count.index}"
    "kubernetes.io/role/elb"                      = "1"
    "kubernetes.io/cluster/${var.project_prefix}" = "shared"
  }
}

# Private subnets (one per AZ)
resource "aws_subnet" "private" {
  count = var.az_count

  vpc_id            = aws_vpc.eks_vpc.id
  cidr_block        = cidrsubnet(aws_vpc.eks_vpc.cidr_block, 8, count.index + var.az_count)
  availability_zone = data.aws_availability_zones.available.names[count.index]

  tags = {
    Name                                          = "${var.project_prefix}-private-${count.index}"
    "kubernetes.io/role/internal-elb"             = "1"
    "kubernetes.io/cluster/${var.project_prefix}" = "shared"
  }
}

# Internet Gateway
resource "aws_internet_gateway" "igw" {
  vpc_id = aws_vpc.eks_vpc.id
  tags = {
    Name = "${var.project_prefix}-igw"
  }
}

# Route table for public subnets
resource "aws_route_table" "public" {
  vpc_id = aws_vpc.eks_vpc.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw.id
  }
  tags = { Name = "${var.project_prefix}-public-rt" }
}

# Associate public subnets to public route table
resource "aws_route_table_association" "public_assoc" {
  count          = var.az_count
  subnet_id      = aws_subnet.public[count.index].id
  route_table_id = aws_route_table.public.id
}

# Elastic IPs for NAT (one per AZ recommended; here creamos 1 NAT por AZ)
resource "aws_eip" "nat_eip" {
  count = var.az_count
}

# NAT GW per public subnet
resource "aws_nat_gateway" "nat" {
  count         = var.az_count
  allocation_id = aws_eip.nat_eip[count.index].id
  subnet_id     = aws_subnet.public[count.index].id

  tags       = { Name = "${var.project_prefix}-nat-${count.index}" }
  depends_on = [aws_internet_gateway.igw]
}

# Route table for private subnets (each private subnet route to its NAT)
resource "aws_route_table" "private" {
  count  = var.az_count
  vpc_id = aws_vpc.eks_vpc.id

  route {
    cidr_block     = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.nat[count.index].id
  }

  tags = { Name = "${var.project_prefix}-private-rt-${count.index}" }
}

resource "aws_route_table_association" "private_assoc" {
  count          = var.az_count
  subnet_id      = aws_subnet.private[count.index].id
  route_table_id = aws_route_table.private[count.index].id
}

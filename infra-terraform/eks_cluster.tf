module "eks" {
  source  = "terraform-aws-modules/eks/aws"
  version = "20.8.2"

  cluster_name            = "${var.project_prefix}-eks"
  cluster_version         = "1.30"
  vpc_id                  = aws_vpc.eks_vpc.id
  subnet_ids = aws_subnet.public[*].id

  enable_irsa = true

  cluster_endpoint_public_access  = true
  cluster_endpoint_private_access = false

  eks_managed_node_groups = {
    nodes = {
      min_size     = 1
      max_size     = 1
      desired_size = 1

      instance_types = ["t3.micro"]
      subnet_ids     = aws_subnet.public[*].id
    }
  }
}

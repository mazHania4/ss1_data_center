
output "eks_vpc_id" { value = aws_vpc.eks_vpc.id }
output "eks_public_subnets" { value = aws_subnet.public[*].id }
output "eks_private_subnets" { value = aws_subnet.private[*].id }



module "eks" {
  source  = "terraform-aws-modules/eks/aws"
  version = "20.8.2"

  cluster_name    = "${var.project_prefix}-eks-cluster"
  cluster_version = "1.30"

  vpc_id     = aws_vpc.eks_vpc.id
  subnet_ids = concat(aws_subnet.public[*].id, aws_subnet.private[*].id)

  enable_irsa = true

  cluster_endpoint_public_access  = true
  cluster_endpoint_private_access = true

  tags = {
    Environment = "dev"
    Terraform   = "true"
  }

  eks_managed_node_groups = {
    nodes = {
      desired_size = 2
      max_size     = 4
      min_size     = 1

      instance_types = ["t3.medium"]
      subnet_ids     = aws_subnet.private[*].id

      tags = {
        Name = "${var.project_prefix}-ng"
      }
    }
  }
}


# Acceso IAM al cluster EKS
resource "aws_eks_access_entry" "admin_user" {
  cluster_name  = module.eks.cluster_name
  principal_arn = var.iam_user_admin_arn
  type          = "STANDARD"
}

resource "aws_eks_access_policy_association" "admin_user_policy" {
  cluster_name  = module.eks.cluster_name
  principal_arn = aws_eks_access_entry.admin_user.principal_arn

  policy_arn = "arn:aws:eks::aws:cluster-access-policy/AmazonEKSClusterAdminPolicy"

  access_scope {
    type       = "cluster"
    namespaces = []
  }
}

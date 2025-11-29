variable "aws_region" {
  type        = string
  default     = "us-east-2"
  description = "AWS region where resources will be created"
}

# (opcional) para controlar naming prefix
variable "project_prefix" {
  type    = string
  default = "ss1-org"
}

# ARN de un usuario IAM con permisos de administrador al que se le darán permisos de administración del cluster eks
variable "iam_user_admin_arn" {
  type    = string
  default = "arn:aws:iam::767828726453:user/admin-terraform"
}

variable "az_count" {
  type    = number
  default = 3
}


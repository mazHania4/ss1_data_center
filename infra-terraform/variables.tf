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

variable "az_count" {
  type    = number
  default = 3
}


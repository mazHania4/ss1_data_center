provider "aws" {
  profile = "sso-terraform"
  region  = var.aws_region
  # credentials se toman desde env vars o perfil configurado por aws cli
}

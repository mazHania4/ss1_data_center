
resource "kubernetes_secret" "ayuda_humanitaria_secrets" {
  metadata {
    name = "ayuda-humanitaria-secrets"
  }

  data = {
    DB_USERNAME        = base64encode(var.ah_db_master_username)
    DB_PASSWORD        = base64encode(var.ah_db_password)
    JWT_SECRET         = base64encode(var.jwt_secret)
    JWT_REFRESH_SECRET = base64encode(var.jwt_refresh_secret)
    EMAIL_USERNAME     = base64encode(var.smtp_email_user)
    EMAIL_PASSWORD     = base64encode(var.smtp_email_pass)

    AWS_ACCESS_KEY = base64encode(aws_iam_access_key.ayuda_bucket_key.id)
    AWS_SECRET_KEY = base64encode(aws_iam_access_key.ayuda_bucket_key.secret)
  }

  type = "Opaque"
}

output "ayuda_humanitaria_secrets" {
  sensitive = true
  value = {
    DB_USERNAME        = var.ah_db_master_username
    DB_PASSWORD        = var.ah_db_password
    JWT_SECRET         = var.jwt_secret
    JWT_REFRESH_SECRET = var.jwt_refresh_secret
    EMAIL_USERNAME     = var.smtp_email_user
    EMAIL_PASSWORD     = var.smtp_email_pass

    AWS_ACCESS_KEY = aws_iam_access_key.ayuda_bucket_key.id
    AWS_SECRET_KEY = aws_iam_access_key.ayuda_bucket_key.secret
  }
}


resource "kubernetes_secret" "centro_secrets" {
  metadata {
    name = "centro-datos-secrets"
  }

  data = {
    DB_USERNAME        = base64encode(var.cd_db_master_username)
    DB_PASSWORD        = base64encode(var.cd_db_password)
    JWT_SECRET         = base64encode(var.jwt_secret)
    JWT_REFRESH_SECRET = base64encode(var.jwt_refresh_secret)
    EMAIL_USERNAME     = base64encode(var.smtp_email_user)
    EMAIL_PASSWORD     = base64encode(var.smtp_email_pass)

    AWS_ACCESS_KEY = base64encode(aws_iam_access_key.centro_bucket_key.id)
    AWS_SECRET_KEY = base64encode(aws_iam_access_key.centro_bucket_key.secret)
  }

  type = "Opaque"
}

output "centro_secrets" {
  sensitive = true
  value = {
    DB_USERNAME        = var.cd_db_master_username
    DB_PASSWORD        = var.cd_db_password
    JWT_SECRET         = var.jwt_secret
    JWT_REFRESH_SECRET = var.jwt_refresh_secret
    EMAIL_USERNAME     = var.smtp_email_user
    EMAIL_PASSWORD     = var.smtp_email_pass

    AWS_ACCESS_KEY = aws_iam_access_key.centro_bucket_key.id
    AWS_SECRET_KEY = aws_iam_access_key.centro_bucket_key.secret
  }
}

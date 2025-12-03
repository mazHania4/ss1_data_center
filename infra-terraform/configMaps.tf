resource "kubernetes_config_map" "centro_datos_config_map" {
  metadata {
    name = "centro-datos-config"
  }

  data = {
    SERVER_PORT       = "8080"
    DB_HOST           = aws_db_instance.centro_db.address
    DB_NAME           = "centro_datos"
    HUMANITARY_HELP_SERVICE_URL = "http://ayuda-humanitaria:8080"
    FRONTEND_ORIGIN   = aws_cloudfront_distribution.frontend.domain_name
    S3_BUCKET         = aws_s3_bucket.centro_bucket.bucket
    S3_REGION         = "us-east-2"
  }
}

output "centro_datos_config_map" {
  value = {
    SERVER_PORT     = "8080"
    DB_HOST         = aws_db_instance.centro_db.address
    DB_NAME         = "centro_datos"
    HUMANITARY_HELP_SERVICE_URL = "http://ayuda-humanitaria:8080"
    FRONTEND_ORIGIN = aws_cloudfront_distribution.frontend.domain_name
    S3_BUCKET       = aws_s3_bucket.centro_bucket.bucket
    S3_REGION       = "us-east-2"
  }
}

resource "kubernetes_config_map" "ayuda_humanitaria_config_map" {
  metadata {
    name = "ayuda-humanitaria-config"
  }

  data = {
    SERVER_PORT       = "8080"
    DB_HOST           = aws_db_instance.ayuda_db.address
    DB_NAME           = "ayuda_humanitaria"
    DATA_CENTER_SERVICE_URL = "http://centro-datos:8080"
    FRONTEND_ORIGIN   = aws_cloudfront_distribution.frontend.domain_name
    S3_BUCKET         = aws_s3_bucket.ayuda_bucket.bucket
    S3_REGION         = "us-east-2"
  }
}

output "ayuda_humanitaria_config_map" {
  value = {
    SERVER_PORT     = "8080"
    DB_HOST         = aws_db_instance.ayuda_db.address
    DB_NAME         = "ayuda_humanitaria"
    DATA_CENTER_SERVICE_URL = "http://centro-datos:8080"
    FRONTEND_ORIGIN = aws_cloudfront_distribution.frontend.domain_name
    S3_BUCKET       = aws_s3_bucket.ayuda_bucket.bucket
    S3_REGION       = "us-east-2"
  }
}
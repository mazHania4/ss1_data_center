resource "aws_s3_bucket" "frontend" {
  bucket = "${var.project_prefix}-frontend-bucket"
}

resource "aws_s3_bucket_public_access_block" "frontend_public" {
  bucket = aws_s3_bucket.frontend.id

  block_public_acls       = false
  block_public_policy     = false
  ignore_public_acls      = false
  restrict_public_buckets = false
}

resource "aws_s3_bucket_website_configuration" "frontend_web" {
  bucket = aws_s3_bucket.frontend.id

  index_document {
    suffix = "index.html"
  }
  error_document {
    key = "index.html"
  }
}

resource "aws_s3_bucket_policy" "frontend_policy" {
  bucket = aws_s3_bucket.frontend.id
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect    = "Allow"
        Principal = "*"
        Action    = "s3:GetObject"
        Resource  = "${aws_s3_bucket.frontend.arn}/*"
      }
    ]
  })
}

resource "aws_cloudfront_distribution" "frontend" {
  enabled             = true
  default_root_object = "index.html"

  origin {
    domain_name = aws_s3_bucket.frontend.bucket_regional_domain_name
    origin_id   = "s3-frontend"
  }

  default_cache_behavior {
    allowed_methods  = ["GET", "HEAD"]
    cached_methods   = ["GET", "HEAD"]
    target_origin_id = "s3-frontend"

    viewer_protocol_policy = "redirect-to-https"

    forwarded_values {
      query_string = true
      cookies {
        forward = "none" # requerido
      }
    }

  }

  restrictions {
    geo_restriction {
      restriction_type = "none" # requerido
    }
  }

  viewer_certificate {
    cloudfront_default_certificate = true
  }
}

output "frontend_url" {
  value = aws_cloudfront_distribution.frontend.domain_name
}

resource "aws_iam_user" "frontend_deployer" {
  name = "${var.project_prefix}-frontend-deployer"
}

data "aws_iam_policy_document" "frontend_policy_doc" {
  statement {
    sid = "S3FrontendDeploy"
    actions = [
      "s3:PutObject",
      "s3:PutObjectAcl",
      "s3:DeleteObject",
      "s3:ListBucket",
      "s3:GetObject"
    ]
    resources = [
      aws_s3_bucket.frontend.arn,
      "${aws_s3_bucket.frontend.arn}/*"
    ]
  }

  statement {
    sid = "CloudFrontInvalidation"
    actions = [
      "cloudfront:CreateInvalidation",
      "cloudfront:GetInvalidation",
      "cloudfront:ListInvalidations"
    ]
    resources = ["*"]
    # No se puede restringir a nivel de ARN porque CloudFront no soporta restricciones por distribución
  }
}

resource "aws_iam_policy" "frontend_policy" {
  name   = "${var.project_prefix}-frontend-deploy-policy"
  policy = data.aws_iam_policy_document.frontend_policy_doc.json
}

resource "aws_iam_user_policy_attachment" "attach_frontend_policy" {
  user       = aws_iam_user.frontend_deployer.name
  policy_arn = aws_iam_policy.frontend_policy.arn
}

resource "aws_iam_access_key" "frontend_deployer_key" {
  user = aws_iam_user.frontend_deployer.name
}

output "frontend_deployer_access_key_id" {
  value     = aws_iam_access_key.frontend_deployer_key.id
  sensitive = true
}

output "frontend_deployer_secret_access_key" {
  value     = aws_iam_access_key.frontend_deployer_key.secret
  sensitive = true
}
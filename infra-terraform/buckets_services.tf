
# Bucket para Ayuda Humanitaria
resource "aws_s3_bucket" "ayuda_bucket" {
  bucket = "${var.project_prefix}-ayuda-humanitaria-bucket"
}

resource "aws_iam_user" "ayuda_bucket_user" {
  name = "${var.project_prefix}-ayuda-humanitaria-bucket-user"
}

# Keys del usuario Ayuda Humanitaria
resource "aws_iam_access_key" "ayuda_bucket_key" {
  user = aws_iam_user.ayuda_bucket_user.name
}

data "aws_iam_policy_document" "ayuda_bucket_policy_doc" {
  statement {
    effect = "Allow"
    actions = [
      "s3:PutObject",
      "s3:GetObject",
      "s3:DeleteObject",
      "s3:ListBucket"
    ]

    resources = [
      aws_s3_bucket.ayuda_bucket.arn,
      "${aws_s3_bucket.ayuda_bucket.arn}/*"
    ]
  }
}

resource "aws_iam_policy" "ayuda_bucket_policy" {
  name   = "${var.project_prefix}-ayuda-policy"
  policy = data.aws_iam_policy_document.ayuda_bucket_policy_doc.json
}

resource "aws_iam_user_policy_attachment" "ayuda_attach" {
  user       = aws_iam_user.ayuda_bucket_user.name
  policy_arn = aws_iam_policy.ayuda_bucket_policy.arn
}


# Bucket para Centro de Datos
resource "aws_s3_bucket" "centro_bucket" {
  bucket = "${var.project_prefix}-centro-datos-bucket"
}

resource "aws_iam_user" "centro_bucket_user" {
  name = "${var.project_prefix}-centro-datos-bucket-user"
}

# Keys del usuario Centro de Datos
resource "aws_iam_access_key" "centro_bucket_key" {
  user = aws_iam_user.centro_bucket_user.name
}

data "aws_iam_policy_document" "centro_bucket_policy_doc" {
  statement {
    effect = "Allow"
    actions = [
      "s3:PutObject",
      "s3:GetObject",
      "s3:DeleteObject",
      "s3:ListBucket"
    ]

    resources = [
      aws_s3_bucket.centro_bucket.arn,
      "${aws_s3_bucket.centro_bucket.arn}/*"
    ]
  }
}

resource "aws_iam_policy" "centro_bucket_policy" {
  name   = "${var.project_prefix}-centro-policy"
  policy = data.aws_iam_policy_document.centro_bucket_policy_doc.json
}

resource "aws_iam_user_policy_attachment" "centro_attach" {
  user       = aws_iam_user.centro_bucket_user.name
  policy_arn = aws_iam_policy.centro_bucket_policy.arn
}

output "ayuda_s3_access_key_id" {
  value = aws_iam_access_key.ayuda_bucket_key.id
}

output "ayuda_s3_secret_key" {
  value     = aws_iam_access_key.ayuda_bucket_key.secret
  sensitive = true
}

output "centro_s3_access_key_id" {
  value = aws_iam_access_key.centro_bucket_key.id
}

output "centro_s3_secret_key" {
  value     = aws_iam_access_key.centro_bucket_key.secret
  sensitive = true
}


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
  default = "arn:aws:iam::XXXX:user/admin-terraform"
}

variable "az_count" {
  type    = number
  default = 3
}

variable "postgres_password" {
  type      = string
  sensitive = true
  default = "secure_password" # CAMBIAR!!
}

variable "db_instance_class" {
  description = "RDS instance class"
  type        = string
  default     = "db.t3.micro"
}

variable "db_allocated_storage" {
  description = "Allocated storage (GB)"
  type        = number
  default     = 5
}

variable "db_engine_version" {
  description = "Postgres engine version"
  type        = string
  default     = "17.4"
}

variable "db_master_username" {
  description = "Master username for DBs"
  type        = string
  default     = "postgres"
}

variable "db_port" {
  description = "Port for Postgres"
  type        = number
  default     = 5432
}

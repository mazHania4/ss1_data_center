variable "aws_region" {
  type        = string
  default     = "us-east-2"
  description = "AWS region where resources will be created"
}

data "aws_availability_zones" "available" {
  state = "available"
}

variable "az_count" {
  type    = number
  default = 2
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

variable "ah_db_master_username" {
  description = "Master username for ayuda humanitaria DB"
  type        = string
  default     = "postgres"
}

variable "cd_db_master_username" {
  description = "Master username for centro de datos DB"
  type        = string
  default     = "postgres"
}

variable "ah_db_password" {
  type      = string
  sensitive = true
  default = "ss1-org_db_pass" # CAMBIAR!!
}

variable "cd_db_password" {
  type      = string
  sensitive = true
  default = "ss1-org_db_pass" # CAMBIAR!!
}

variable "db_port" {
  description = "Port for Postgres"
  type        = number
  default     = 5432
}

variable "smtp_email_user" {
  type      = string
  sensitive = true
  default = "fkzw pueu twfa enqx" # CAMBIAR!!
}

variable "smtp_email_pass" {
  type      = string
  sensitive = true
  default = "haniamazariegos202031953@cunoc.edu.gt" # CAMBIAR!!
}

variable "jwt_secret" {
  type      = string
  sensitive = true
  default = "supersecretoAccesoSupersecretoRefreshSupersecretoAcceso" # CAMBIAR!!
}

variable "jwt_refresh_secret" {
  type      = string
  sensitive = true
  default = "supersecretoAccesoSupersecretoAccesoSupersecretoAcceso" # CAMBIAR!!
}
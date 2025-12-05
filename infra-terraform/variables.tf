#variables.tf

# (opcional) para controlar naming prefix
variable "project_prefix" {
  type    = string
  default = "ss1-org"
}

# --------------------------
# VARIABLES PARA AZURE
# --------------------------
variable "azure_region" {
  type        = string
  default     = "canadacentral"
  description = "Azure region where resources will be created"
}

# Cantidad de subnets (solo 1 para minimizar costos)
variable "subnet_count" {
  type        = number
  default     = 1
}


# --------------------------
# Variables para la BD
# --------------------------

variable "db_instance_class" {
  description = "SKU for Azure PostgreSQL Flexible Server (Basic tier for low cost)"
  type        = string
  default     = "B_Standard_B1ms"
}

variable "db_storage_mb" {
  description = "Storage for PostgreSQL DB (in MB)"
  type        = number
  default     = 32768 # 32GB
}

variable "db_engine_version" {
  description = "PostgreSQL engine version"
  type        = string
  default     = "17"
}

variable "ah_db_master_username" {
  description = "Master username for ayuda humanitaria DB"
  type        = string
  default     = "xxx"
}

variable "cd_db_master_username" {
  description = "Master username for centro de datos DB"
  type        = string
  default     = "xxx"
}

variable "ah_db_password" {
  type      = string
  sensitive = true
  default = "xxx" 
}

variable "cd_db_password" {
  type      = string
  sensitive = true
  default = "xxx" 
}

variable "db_port" {
  description = "Port for PostgreSQL"
  type        = number
  default     = 5432
}

# --------------------------
# variables para la VM
# --------------------------
variable "k3s_vm_size" {
  description = "SKU for Azure VM (Basic tier for low cost)"
  type        = string
  default     = "Standard_B2ats_v2"
}

variable "k3s_disk_size" {
  description = "(GB) size of disk for VM"
  type        = number
  default     = 30
}


# --------------------------
# SMTP
# --------------------------

variable "smtp_email_user" {
  type      = string
  sensitive = true
  default = "xxx"
}

variable "smtp_email_pass" {
  type      = string
  sensitive = true
  default = "xxx" 
}

# --------------------------
# JWT
# --------------------------

variable "jwt_secret" {
  type      = string
  sensitive = true
  default = "xxx"
}

variable "jwt_refresh_secret" {
  type      = string
  sensitive = true
  default = "xxx"
}
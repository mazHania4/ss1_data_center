########################################
# Azure Postgres Flexible Servers
# - Public network access = Enabled (temporal)
# - Firewall 0.0.0.0/0 (temporal)
# - Two separate DB servers (ayuda + centro)
########################################

# Ayuda Humanitaria
resource "azurerm_postgresql_flexible_server" "ayuda_db" {
  name                   = "${var.project_prefix}ayudadb"
  resource_group_name    = azurerm_resource_group.main.name
  location               = var.azure_region

  administrator_login          = var.ah_db_master_username
  administrator_password       = var.ah_db_password
  version                      = var.db_engine_version
  storage_mb                  = var.db_storage_mb
  sku_name                     = var.db_instance_class

  public_network_access_enabled = true

  backup_retention_days = 7
  geo_redundant_backup_enabled = false

  lifecycle {
    prevent_destroy = false
  }

  tags = {
    Name       = "${var.project_prefix}-ayuda-humanitaria-db"
    Terraform  = "true"
    Temporary  = "true"
  }
}

resource "azurerm_postgresql_flexible_server_database" "ayuda_db_name" {
  name      = "ayuda_humanitaria"
  server_id = azurerm_postgresql_flexible_server.ayuda_db.id
}

resource "azurerm_postgresql_flexible_server_firewall_rule" "ayuda_fw" {
  name      = "allow-all-temporal"
  server_id = azurerm_postgresql_flexible_server.ayuda_db.id

  start_ip_address = "0.0.0.0"
  end_ip_address   = "255.255.255.255"
}



# Centro de datos
resource "azurerm_postgresql_flexible_server" "centro_db" {
  name                   = "${var.project_prefix}centrodb"
  resource_group_name    = azurerm_resource_group.main.name
  location               = var.azure_region

  administrator_login    = var.cd_db_master_username
  administrator_password = var.cd_db_password
  version                = var.db_engine_version
  storage_mb             = var.db_storage_mb
  sku_name               = var.db_instance_class

  public_network_access_enabled = true

  backup_retention_days = 7
  geo_redundant_backup_enabled = false

  lifecycle {
    prevent_destroy = false
  }

  tags = {
    Name       = "${var.project_prefix}-centro-datos-db"
    Terraform  = "true"
    Temporary  = "true"
  }
}

resource "azurerm_postgresql_flexible_server_database" "centro_db_name" {
  name      = "centro_datos"
  server_id = azurerm_postgresql_flexible_server.centro_db.id
}

resource "azurerm_postgresql_flexible_server_firewall_rule" "centro_fw" {
  name      = "allow-all-temporal"
  server_id = azurerm_postgresql_flexible_server.centro_db.id

  start_ip_address = "0.0.0.0"
  end_ip_address   = "255.255.255.255"
}


# Outputs
output "ayuda_db_host" {
  value       = azurerm_postgresql_flexible_server.ayuda_db.fqdn
  description = "FQDN for ayuda_humanitaria DB"
}

output "centro_db_host" {
  value       = azurerm_postgresql_flexible_server.centro_db.fqdn
  description = "FQDN for centro_datos DB"
}

output "db_port" {
  value = 5432
}

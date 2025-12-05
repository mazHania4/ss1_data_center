# DATA SOURCES NECESARIOS
data "azurerm_client_config" "current" {}

# STORAGE ACCOUNT
resource "azurerm_storage_account" "frontend_storage" {
  name                     = replace("${var.project_prefix}-frontend", "-", "")
  resource_group_name      = azurerm_resource_group.main.name
  location                 = azurerm_resource_group.main.location

  account_tier             = "Standard"
  account_replication_type = "LRS"
  account_kind             = "StorageV2"

  allow_nested_items_to_be_public = true
}

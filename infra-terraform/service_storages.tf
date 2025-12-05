# AYUDA HUMANITARIA STORAGE
resource "azurerm_storage_account" "ayuda_storage" {
  name                     = "ss1ayuda"
  resource_group_name      = azurerm_resource_group.main.name
  location                 = azurerm_resource_group.main.location

  account_tier             = "Standard"
  account_replication_type = "LRS"
  account_kind             = "StorageV2"

  allow_nested_items_to_be_public = true
}

resource "azurerm_storage_container" "ayuda_container" {
  name               = "ayuda-bucket"
  storage_account_id = azurerm_storage_account.ayuda_storage.id
  container_access_type = "private"
}

# Service principal para Ayuda Humanitaria
resource "azuread_application" "ayuda_app" {
  display_name = "${var.project_prefix}-ayuda-sp"
}

resource "azuread_service_principal" "ayuda_sp" {
  client_id = azuread_application.ayuda_app.client_id
}

resource "azuread_service_principal_password" "ayuda_sp_secret" {
  service_principal_id = azuread_service_principal.ayuda_sp.id
}

resource "azurerm_role_assignment" "ayuda_role" {
  scope                = azurerm_storage_account.ayuda_storage.id
  role_definition_name = "Storage Blob Data Contributor"
  principal_id         = azuread_service_principal.ayuda_sp.object_id
}


# OUTPUTS
output "ayuda_storage_account" {
  value = azurerm_storage_account.ayuda_storage.name
}

output "ayuda_storage_container" {
  value = azurerm_storage_container.ayuda_container.name
}

output "ayuda_sp_client_id" {
  value = azuread_application.ayuda_app.client_id
}

output "ayuda_sp_client_secret" {
  value     = azuread_service_principal_password.ayuda_sp_secret.value
  sensitive = true
}

output "tenant_id" {
  value = data.azurerm_client_config.current.tenant_id
}

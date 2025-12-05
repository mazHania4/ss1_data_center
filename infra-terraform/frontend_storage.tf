
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

# STATIC WEBSITE CONFIG
resource "azurerm_storage_account_static_website" "frontend_static" {
  storage_account_id = azurerm_storage_account.frontend_storage.id

  index_document     = "index.html"
  error_404_document = "index.html"
}

# SERVICE PRINCIPAL (PARA CI/CD)
resource "azuread_application" "frontend_app" {
  display_name = "${var.project_prefix}-frontend-ci"
}

resource "azuread_service_principal" "frontend_sp" {
  client_id = azuread_application.frontend_app.client_id
}

resource "azuread_service_principal_password" "frontend_sp_secret" {
  service_principal_id = azuread_service_principal.frontend_sp.id
}

# ROLE ASSIGNMENT (BLOB CONTRIBUTOR)
resource "azurerm_role_assignment" "frontend_sp_role" {
  scope                = azurerm_storage_account.frontend_storage.id
  role_definition_name = "Storage Blob Data Contributor"
  principal_id         = azuread_service_principal.frontend_sp.object_id
}

# OUTPUTS
output "frontend_url" {
  description = "URL pública del sitio estático"
  value       = azurerm_storage_account.frontend_storage.primary_web_endpoint
}

output "frontend_sp_client_id" {
  description = "Client ID para GitHub Actions"
  value       = azuread_application.frontend_app.client_id
}

output "frontend_sp_client_secret" {
  description = "Client Secret para CI/CD"
  value       = azuread_service_principal_password.frontend_sp_secret.value
  sensitive   = true
}

output "frontend_tenant_id" {
  description = "Tenant ID"
  value       = data.azurerm_client_config.current.tenant_id
}

output "storage_account_name" {
  description = "Storage Account Name"
  value       = azurerm_storage_account.frontend_storage.name
}

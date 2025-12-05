
output "ayuda_humanitaria_config_map" {
  value = {
    SERVER_PORT = "8080"
    DB_HOST     = azurerm_postgresql_flexible_server.ayuda_db.fqdn
    DB_NAME     = "ayuda_humanitaria"

    DC_SERVICE_URL = "http://centro-datos:8080"

    FRONTEND_ORIGIN = azurerm_storage_account.frontend_storage.primary_web_endpoint

    STORAGE_ACCOUNT         = azurerm_storage_account.ayuda_storage.name
    STORAGE_ACCOUNT_ENDPOINT = azurerm_storage_account.ayuda_storage.primary_blob_endpoint
    STORAGE_CONTAINER        = azurerm_storage_container.ayuda_container.name
    STORAGE_REGION   = var.azure_region
  }
}

output "ayuda_humanitaria_secrets" {
  sensitive = true
  value = {
    DB_USERNAME        = var.ah_db_master_username
    DB_PASSWORD        = var.ah_db_password

    JWT_SECRET         = var.jwt_secret
    JWT_REFRESH_SECRET = var.jwt_refresh_secret

    EMAIL_USERNAME     = var.smtp_email_user
    EMAIL_PASSWORD     = var.smtp_email_pass

    STORAGE_ACCESS_KEY = azurerm_storage_account.ayuda_storage.primary_access_key
  }
}



output "centro_datos_config_map" {
  value = {
    SERVER_PORT  = "8080"
    DB_HOST      = azurerm_postgresql_flexible_server.centro_db.fqdn
    DB_NAME      = "centro_datos"

    HH_SERVICE_URL = "http://ayuda-humanitaria:8080"

    FRONTEND_ORIGIN = azurerm_storage_account.frontend_storage.primary_web_endpoint
  }
}

output "centro_secrets" {
  sensitive = true
  value = {
    DB_USERNAME        = var.cd_db_master_username
    DB_PASSWORD        = var.cd_db_password

    JWT_SECRET         = var.jwt_secret
    JWT_REFRESH_SECRET = var.jwt_refresh_secret

    EMAIL_USERNAME     = var.smtp_email_user
    EMAIL_PASSWORD     = var.smtp_email_pass
  }
}



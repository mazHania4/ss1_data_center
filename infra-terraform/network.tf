# Resource Group
resource "azurerm_resource_group" "main" {
  name     = "${var.project_prefix}-rg"
  location = var.azure_region
}

# VNET
resource "azurerm_virtual_network" "vnet" {
  name                = "${var.project_prefix}-vnet"
  location            = var.azure_region
  resource_group_name = azurerm_resource_group.main.name
  address_space       = ["10.0.0.0/16"]

  tags = {
    environment = "dev"
  }
}

# Subnet pública
resource "azurerm_subnet" "public" {
  count                = var.subnet_count
  name                 = "${var.project_prefix}-subnet-${count.index}"
  resource_group_name  = azurerm_resource_group.main.name
  virtual_network_name = azurerm_virtual_network.vnet.name
  address_prefixes     = [cidrsubnet("10.0.0.0/16", 8, count.index)]
}


output "vnet_id" {
  value = azurerm_virtual_network.vnet.id
}

output "subnet_ids" {
  value = azurerm_subnet.public[*].id
}

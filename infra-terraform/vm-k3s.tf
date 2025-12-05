
# NETWORK SECURITY GROUP
resource "azurerm_network_security_group" "k3s_nsg" {
  name                = "${var.project_prefix}-k3s-nsg"
  location            = var.azure_region
  resource_group_name = azurerm_resource_group.main.name

  security_rule {
    name                       = "SSH"
    priority                   = 100
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "*"
    destination_port_range     = "22"
    source_address_prefix      = "*"
    destination_address_prefix = "*"
  }

  security_rule {
    name                       = "K3s-API"
    priority                   = 110
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "*"
    destination_port_range     = "6443"
    source_address_prefix      = "*"
    destination_address_prefix = "*"
  }

  security_rule {
    name                       = "NodePorts"
    priority                   = 120
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "*"
    destination_port_range     = "30000-32767"
    source_address_prefix      = "*"
    destination_address_prefix = "*"
  }
}

# PUBLIC IP
resource "azurerm_public_ip" "k3s_ip" {
  name                = "${var.project_prefix}-k3s-ip"
  location            = var.azure_region
  resource_group_name = azurerm_resource_group.main.name
  allocation_method   = "Static"
  sku                 = "Standard"
}

# NETWORK INTERFACE
resource "azurerm_network_interface" "k3s_nic" {
  name                = "${var.project_prefix}-k3s-nic"
  location            = var.azure_region
  resource_group_name = azurerm_resource_group.main.name

  ip_configuration {
    name                          = "internal"
    subnet_id                     = azurerm_subnet.public[0].id
    private_ip_address_allocation = "Dynamic"
    public_ip_address_id          = azurerm_public_ip.k3s_ip.id
  }
}

# Asociar NSG a NIC
resource "azurerm_network_interface_security_group_association" "k3s_assoc" {
  network_interface_id      = azurerm_network_interface.k3s_nic.id
  network_security_group_id = azurerm_network_security_group.k3s_nsg.id
}

# SSH KEY
resource "azurerm_ssh_public_key" "k3s_ssh" {
  name                = "${var.project_prefix}-k3s-ssh"
  resource_group_name = azurerm_resource_group.main.name
  location            = var.azure_region
  public_key          = file("~/.ssh/id_rsa.pub")
}

# VIRTUAL MACHINE
resource "azurerm_linux_virtual_machine" "k3s_vm" {
  name                = "${var.project_prefix}-k3s-master"
  resource_group_name = azurerm_resource_group.main.name
  location            = var.azure_region
  size                = var.k3s_vm_size
  admin_username      = "azureuser"

  network_interface_ids = [
    azurerm_network_interface.k3s_nic.id
  ]

  admin_ssh_key {
    username   = "azureuser"
    public_key = azurerm_ssh_public_key.k3s_ssh.public_key
  }

  os_disk {
    caching              = "ReadWrite"
    storage_account_type = "Standard_LRS"
    disk_size_gb         = var.k3s_disk_size
  }

  source_image_reference {
    publisher = "Canonical"
    offer     = "0001-com-ubuntu-server-jammy"
    sku       = "22_04-lts"
    version   = "latest"
  }

  tags = {
    Environment = "dev"
    Role        = "k3s-master"
  }
}


# OUTPUTS
output "k3s_master_public_ip" {
  value = azurerm_public_ip.k3s_ip.ip_address
}

output "k3s_master_private_ip" {
  value = azurerm_network_interface.k3s_nic.private_ip_address
}

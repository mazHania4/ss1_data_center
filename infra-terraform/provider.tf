terraform {
  required_version = ">= 1.4.0"

  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "~> 4.0"
    }
  }
}

provider "azurerm" {
  subscription_id = "6462e5aa-3886-4cbc-96c4-2e3449047cd2"
  features {
    resource_group {}
  }
}

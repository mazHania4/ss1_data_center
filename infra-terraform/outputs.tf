
output "eks_vpc_id" { value = aws_vpc.eks_vpc.id }
output "eks_public_subnets" { value = aws_subnet.public[*].id }
output "eks_private_subnets" { value = aws_subnet.private[*].id }


output "ayuda_db_endpoint" {
  value = aws_db_instance.ayuda_db.address
  description = "Endpoint (host) for ayuda-humanitaria-db"
}

output "centro_db_endpoint" {
  value = aws_db_instance.centro_db.address
  description = "Endpoint (host) for centro-datos-db"
}

output "ayuda_db_port" {
  value = aws_db_instance.ayuda_db.port
}

output "centro_db_port" {
  value = aws_db_instance.centro_db.port
}
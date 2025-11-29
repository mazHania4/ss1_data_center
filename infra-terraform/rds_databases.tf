########################################
# RDS Postgres instances (2 DBs)
# - Publicly accessible (temporal)
# - Password generated with random_password
# - Password stored in AWS Secrets Manager (one secret per DB)
# - Subnet group uses public subnets so endpoint is reachable from internet
########################################

# ---------- Security group que permite acceso público temporal ----------
resource "aws_security_group" "rds_public_sg" {
  name        = "${var.project_prefix}-rds-public-sg"
  description = "Allow public Postgres access (TEMPORAL) - CIDR 0.0.0.0/0"
  vpc_id      = aws_vpc.eks_vpc.id

  # Ingreso (Postgres)
  ingress {
    description      = "Postgres access TEMPORAL"
    from_port        = var.db_port
    to_port          = var.db_port
    protocol         = "tcp"
    cidr_blocks      = ["0.0.0.0/0"]
  }

  # Salida abierta (para accesos a internet)
  egress {
    description = "Allow all outbound"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "${var.project_prefix}-rds-public-sg"
    Terraform = "true"
    Temporary = "true"
  }
}

# ---------- Subnet group (usamos las subnets públicas para que sea accesible) ----------
resource "aws_db_subnet_group" "rds_public_subnets" {
  name       = "${var.project_prefix}-rds-pub-subnet-group"
  subnet_ids = aws_subnet.public[*].id
  tags = {
    Name = "${var.project_prefix}-rds-pub-subnet-group"
  }
}

# ---------- Instancias RDS Postgres ----------
resource "aws_db_instance" "ayuda_db" {
  identifier              = "${var.project_prefix}-ayuda-humanitaria-db"
  allocated_storage       = var.db_allocated_storage
  engine                  = "postgres"
  engine_version          = var.db_engine_version
  instance_class          = var.db_instance_class
  db_name                    = "ayuda_humanitaria"        # DB name (in-database)
  username                = var.db_master_username
  password                = var.postgres_password
  port                    = var.db_port
  multi_az                = false
  publicly_accessible     = true
  skip_final_snapshot     = true
  vpc_security_group_ids  = [aws_security_group.rds_public_sg.id]
  db_subnet_group_name    = aws_db_subnet_group.rds_public_subnets.name
  deletion_protection     = false
  tags = {
    Name = "ayuda-humanitaria-db"
    Terraform = "true"
    Temporary = "true"
  }
}

resource "aws_db_instance" "centro_db" {
  identifier              = "${var.project_prefix}-centro-datos-db"
  allocated_storage       = var.db_allocated_storage
  engine                  = "postgres"
  engine_version          = var.db_engine_version
  instance_class          = var.db_instance_class
  db_name                    = "centro_datos"            # DB name (in-database)
  username                = var.db_master_username
  password                = var.postgres_password
  port                    = var.db_port
  multi_az                = false
  publicly_accessible     = true
  skip_final_snapshot     = true
  vpc_security_group_ids  = [aws_security_group.rds_public_sg.id]
  db_subnet_group_name    = aws_db_subnet_group.rds_public_subnets.name
  deletion_protection     = false
  tags = {
    Name = "centro-datos-db"
    Terraform = "true"
    Temporary = "true"
  }
}


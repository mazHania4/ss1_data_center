## Guía de despliegue reproducible — EKS con Terraform
1. Tecnologías y versiones usadas (referencia)

-   Terraform >= **1.4.0**
-  Provider AWS: `hashicorp/aws` `~> 5.0` (configurado en `versions.tf`)
-  Módulo EKS: `terraform-aws-modules/eks/aws` **version 20.8.2**
-   Kubernetes (EKS) version: **1.30*
-   Node type: `t3.medium` (managed node group)
-   Herramientas locales:
    -   `awscli` (AWS CLI v2)
    -   `kubectl` (compatible con Kubernetes 1.30)
    -   `eksctl` (opcional, útil)
    -   `terraform`

- **Servicios AWS utilizados:**
    - VPC
        - Subnets
        - Internet Gateway
        - NAT Gateway
        - EIP
    - EKS
    - EC2 (nodes)
        - ELB (Load Balancer)
        - Security Group
    - IAM

# 2. Estructura del repo

``` bash
├── data_az.tf # AZs disponibles (data source) 
├── eks_access.tf # Gestión de acceso IAM -> EKS (aws_eks_access_* resources) 
├── eks_cluster.tf # Llamada al módulo EKS (cluster y node group) 
├── eks_vpc.tf # VPC, subnets públicas/privadas, IGW, NAT, RTs 
├── outputs.tf # Outputs exportados por Terraform 
├── provider.tf # Provider AWS y configuración 
├── saved_outputs/ # outputs que guardan salida tras un apply 
│   └── output_apply_eks_cluster.txt
├── terraform_readme.md # (este archivo o similar) 
├── terraform.tfstate # estado local (no subir a VCS) 
├── variables.tf # variables usadas (region, prefix, etc.) 
└── versions.tf # versión Terraform y providers
``` 


**Archivos clave y qué hacen**

-   `eks_vpc.tf` — crea VPC `10.0.0.0/16`, 3 subnets públicas y 3 privadas (1 por AZ), IGW, NAT GW(s), route tables. Las subnets públicas tienen las etiquetas que EKS/Cloud Controller requiere (`kubernetes.io/role/elb` y `kubernetes.io/role/internal-elb`).

-   `eks_cluster.tf` — usa el módulo oficial `terraform-aws-modules/eks/aws` para crear el clúster EKS y un node group administrado (2 nodes `t3.medium` por defecto).

-   `eks_access.tf` — agrega el IAM user admin (ARN en `variables.tf`) como administrador del clúster mediante recursos `aws_eks_access_entry` y `aws_eks_access_policy_association` (estas entran en la nueva API de EKS Access Management).

-   `outputs.tf` — contiene `eks_vpc_id`, `eks_public_subnets`, `eks_private_subnets`, etc.

# 3. Requisitos previos

## Paquetes instalados en local

### AWS-CLI:
#### Linux
A partir de binarios:
```bash
curl  "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip  awscliv2.zip
sudo ./aws/install
``` 
Usando snap:
```bash
sudo snap install aws-cli --classic
```
#### Arch y derivados:
```bash
sudo pacman -Syu aws-cli
```
```bash
yay -Syu aws-cli-v2
```
Verificar:
```bash
aws --version
```

### Terraform:
#### Linux
A partir de binarios:
```bash
# Add the HashiCorp GPG key.
curl -fsSL https://apt.releases.hashicorp.com/gpg | sudo gpg --dearmor -o /usr/share/keyrings/hashicorp-archive-keyring.gpg
# Add the HashiCorp APT repository.
echo "deb [signed-by=/usr/share/keyrings/hashicorp-archive-keyring.gpg] https://apt.releases.hashicorp.com $(lsb_release -cs) main" | sudo tee /etc/apt/sources.list.d/hashicorp.list
# Update package lists and install Terraform:
sudo apt update && sudo apt install terraform
```
#### Arch y derivados:
```bash
sudo pacman -Syu terraform
```
```bash
 yay -Syu terraform
```
Verificar:
```bash
terraform version
```

### kubectl:
https://kubernetes.io/docs/tasks/tools/
#### Linux
A partir de binarios:
```bash
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl
```
#### Arch y derivados:
```bash
sudo pacman -Syu kubectl
``` 
Verificar:
```bash
kubectl version --client
```

### eksctl: (opcional)
#### Debian y derivados
```bash
curl --silent --location "https://github.com/weaveworks/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp  
sudo mv /tmp/eksctl /usr/local/bin  
eksctl version
```
#### Arch y derivados:
```bash
yay -Syu eksctl
```
Es posible y recomendado instalar los binarios desde GitHub releases


### En macOS:
`brew install terraform awscli kubectl eksctl`


## AWS

-   Cuenta AWS con permisos para crear VPC, EKS, EC2, IAM, etc. (al menos temporalmente `AdministratorAccess`).

-   Opción de autenticación: **IAM User con Access Key** (recomendado para la CLI local) o **AWS SSO (Identity Center)**.
    **No usar la cuenta root.**

# 4. Configuración de credenciales / login AWS (resumen)

### IAM User (sencillo)

1.  Crear un IAM User en la consola AWS (ej: `dev-admin`) y generar Access Key & Secret.
2.  Configurar localmente:
```bash
aws configure # pedirá algo como:  
# AWS Access Key ID: <AKIA...>  
# AWS Secret Access Key: <...>  
# Default region name: us-east-2    # o lo que esté en variables.tf  
# Default output format: json
``` 

### AWS SSO

- Crear y configurar desde la consola de aws en `IAM Identity Center`
- Localmente configurar con:
```bash
aws configure sso
```
Pedirá iniciar sesión en el navegador, luego usar:
```bash
aws sso login --profile [nombre dado al perfil]
```

**Nota:** el cluster fue creado en `us-east-2` (ver variable `aws_region`), usar la misma región al ejecutar `aws eks update-kubeconfig`.


# 5. Cómo replicar (resumen)
1.  **Clonar repo** y revisar `variables.tf`, actualizar:
    -   `aws_region`: si se necesita
    -   `project_prefix`: (opcional) se usa para nombrar algunos componentes
    -   `iam_user_admin_arn`: poner el ARN del IAM user con el que se inicia sesión local y que idealmente tiene permisos de admin en la cuenta de aws. A este usuario se le darán permisos de admin sobre el clúster
2.  **Configurar credenciales AWS** (IAM User o SSO)
3.  **Levantar infraestructura con Terraform**
    `terraform init` → `terraform plan` → `terraform apply`
    (Ver siguiente sección)
4. Ejecutar `aws eks update-kubeconfig` con el nombre del cluster que haya salido en outputs.
5. `kubectl get nodes` — verificar.
6. Ya se pueden hacer despliegues o continuar con otras configuraciones

# 6. Levantar la infraestructura usando Terraform

Dentro de la carpeta `infra-terraform/`:

1. Inicializar
   `terraform init`
2. Validar sintaxis
   `terraform validate`
3. Plan (revisar el plan)
   `terraform plan`
   Si se quiere guardar el plan para ejecutar después:
   `terraform plan -out tfplan`
4. Aplicar
   Crea la infraestructura, puede tardar hasta 10~15 minutos
   `terraform apply`
   Si se quire ejecutar un plan previamente revisado:
   `terraform apply tfplan`

**Para destruir la infraestructura:**
`terraform destroy`

**Opcional:** borrar el contexto kubeconfig
```
kubectl config delete-context arn:aws:eks:us-east-2:<account>:cluster/ss1-org-eks-cluster
```
**Notas**
-   Mantener `terraform.tfstate` seguro (no subirlo a GitHub). Si es trabajo en equipo y fuera necesario que varios accedieran se podría usar un backend remoto (S3 + DynamoDB locking).
-   Ajustar variables en `variables.tf` o pasar `-var`/`-var-file` para entornos de otros usuarios.

# 7. Cómo obtener acceso con kubectl

Tras `terraform apply` se habrá creado el cluster, cuando ya esté `ACTIVE`:

1.  Actualizar `kubeconfig` local:
    Esto genera un contexto nuevo en `~/.kube/config`
```bash
aws eks --region us-east-2 update-kubeconfig --name <cluster-name> 
# por ejemplo para este proyecto:
#aws eks --region us-east-2 update-kubeconfig --name ss1-org-eks-cluster
``` 
2.  Ver contexto:
```bash
kubectl config view 
# Opcionalmente agregar --minify
``` 
3.  Probar conexión:
```bash
kubectl get nodes
``` 
Debería mostrar los **EC2 worker nodes** con estado `Ready`
```
kubectl get pods -n kube-system
```
Debería mostrar:
-   CoreDNS
-   Kube-proxy
-   VPC CNI
-   Node local storage
    Y otros componentes administrados

Si `kubectl` dice “You must be logged in”, revisar que el IAM User/Role esté mapeado como administrador en el clúster.
En este repo usamos `eks_access.tf` para asociar `var.iam_user_admin_arn` al policy `AmazonEKSClusterAdminPolicy`.
Asegúrarse de que `variables.tf` tenga el ARN correcto del usuario.

----------

# 8 . Probar un despliegue sencillo con nginx y LoadBalancer

1.  Crear deployment:
```bash
kubectl create deployment nginx --image=nginx
``` 
2.  Exponer con tipo LoadBalancer:
```bash
kubectl expose deployment nginx --port=80 --target-port=80 --type=LoadBalancer
``` 
3.  Ver servicio y esperar EXTERNAL-IP / hostname:
    `kubectl get svc nginx -o wide # o vigilar kubectl get svc -w`

**¿Qué pasa internamente?**
-   Kubernetes (el cloud-controller-manager de AWS) solicita a AWS crear un ELB/NLB/ALB para este servicio.
-   El Load Balancer se crea en las **subnets públicas** (las etiquetadas en `eks_vpc.tf`).
-   ELB recibirá tráfico público y lo balanceará hacia los NodePorts en los nodos privados, que redirigen a los pods.

**IP / DNS del LB**

-   AWS entrega un **nombre DNS** para el LB (p. ej. `a1b2c3d4e5f6.elb.amazonaws.com`).
-   La IP pública del LB **puede cambiar** (el DNS permanece).
-   Si necesitas IP fija: usar **NLB con Elastic IPs** o una solución de Ingress que permita IPs estáticas; por defecto el ALB/NLB usa IPs que pueden variar.

### ¿Qué implica un `deployment`?
-   Define la plantilla del pod (imagen, variables de entorno, puertos).
-   Maneja réplicas (replica set) y rolling updates.
-   Al actualizar la imagen, Kubernetes realiza un rollout (reemplaza pods gradualmente).
-   Al aplicar el mismo `deployment` con cambios, Kubernetes actualiza los pods, no “sobrescribe” archivos en nodos.


----------
# 9. Comandos útiles (resumen)

```bash
#Terraform 
terraform init
terraform fmt
terraform validate
terraform plan -out tfplan
terraform apply tfplan
terraform destroy 

# AWS CLI (kubeconfig) 
aws eks --region us-east-2 update-kubeconfig --name ss1-org-eks-cluster 

# Kubectl básicos 
kubectl config view
kubectl config current-context
kubectl config use-context <name>
kubectl get nodes
kubectl get pods -A
kubectl get pods -n kube-system
kubectl create deployment nginx --image=nginx
kubectl expose deployment nginx --port=80 --type=LoadBalancer
kubectl get svc -w # Ver detalles del Service / LB kubectl describe svc nginx 
# Ver logs de un pod
kubectl logs -f <pod-name>
# Acceso a un pod
kubectl exec -it <pod-name> -- /bin/sh
# Deploy de prueba (nginx)
kubectl apply -f nginx.yaml
kubectl get svc -w
# Para eliminar kubectl delete svc nginx
kubectl delete deployment nginx
``` 

----------

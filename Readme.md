# PokemonFiap

**Pokemon Dev Ops 4 – Java 21/ Azure / CI-CD**

---

## 👥 Autor

* Enzo Prado Soddano — RM557937
* Lucas Resende Lima — RM556564
* Vinícius Prates Altafini — RM559183

---

## 📝 Descrição

Este projeto é um aplicativo web para gerenciamento de **Pokémons e Treinadores**, utilizando Java 21, Spring Boot, banco de dados Azure SQL e deploy automático via GitHub Actions no Azure Web App.

O sistema oferece:

- CRUD completo de Treinadores e Pokémons  
- Validação de níveis de Pokémon (1 a 100)  
- Persistência em Azure SQL Database  
- Deploy contínuo com GitHub Actions  
- Hospedagem em Azure Web App  

---


# Arquitetura

<img width="1536" height="1024" alt="image" src="https://github.com/user-attachments/assets/8f50bec4-a5c6-4f4e-b145-d102a4efea20" />


# 🚀 Passo a Passo para Configuração do Projeto

## 1️⃣ Configurar o Banco de Dados

O primeiro passo é criar as tabelas **treinador** e **pokemon** no banco de dados. Copie e cole o script do banco, que está localizado em: script.sql



✅ Com isso, você terá a estrutura básica para armazenar Treinadores e Pokémons.

2️⃣ Criar a infraestrutura no Azure
Agora, vamos criar os recursos no Azure (Resource Group, SQL Server, Banco de Dados e Web App). Abra o Azure CLI e execute o seguinte comando em uma linha ou copie em um script:

```bash

bash
Copiar código
RESOURCE_GROUP="rg-pokemonfiap-sprint3"
LOCATION="eastus2"
SQL_SERVER_NAME="sqlserver-pokemonfiap-945-sprint3"
SQL_DATABASE_NAME="pokemonfiapDB"
ADMIN_USER="Trainer"
ADMIN_PASSWORD="Azurecast@666"
APPSERVICE_PLAN_NAME="plan-pokemonfiap-sprint3"
WEBAPP_NAME="webapp-pokemonfiap-945-sprint3"
JAVA_RUNTIME="JAVA:21-java21"

# Criar grupo de recursos
az group create --name $RESOURCE_GROUP --location $LOCATION

# Criar servidor SQL
az sql server create \
  --name $SQL_SERVER_NAME \
  --resource-group $RESOURCE_GROUP \
  --location $LOCATION \
  --admin-user $ADMIN_USER \
  --admin-password $ADMIN_PASSWORD

# Configurar firewall do SQL
az sql server firewall-rule create \
  --resource-group $RESOURCE_GROUP \
  --server $SQL_SERVER_NAME \
  --name AllowAzureServices \
  --start-ip-address 0.0.0.0 \
  --end-ip-address 0.0.0.0

# Criar banco de dados
az sql db create \
  --resource-group $RESOURCE_GROUP \
  --server $SQL_SERVER_NAME \
  --name $SQL_DATABASE_NAME \
  --service-objective S0

# Criar App Service Plan
az appservice plan create \
  --name $APPSERVICE_PLAN_NAME \
  --resource-group $RESOURCE_GROUP \
  --sku B1 \
  --is-linux

# Criar Web App
az webapp create \
  --name $WEBAPP_NAME \
  --resource-group $RESOURCE_GROUP \
  --plan $APPSERVICE_PLAN_NAME \
  --runtime $JAVA_RUNTIME

echo "🚀 Infraestrutura criada com sucesso!"

```


✅ Após esse passo, você terá o banco e o Web App prontos para receber a aplicação.

3️⃣ Configurar o deploy automático no GitHub
Para que o deploy seja feito automaticamente via GitHub Actions, vamos criar um Service Principal e configurar o segredo AZURE_CREDENTIALS.

a) Criar o script deploy.sh
Na raiz do projeto, crie um arquivo chamado start-deploy.sh com o seguinte conteúdo:

Copiar código

```bash
bash

#!/bin/bash

# Configurações
APP_NAME="webapp-pokemonfiap-945-sprint3"
RESOURCE_GROUP="rg-pokemonfiap-sprint3"
REPO="vinicius945/PokemonFiap"
WORKFLOW_PATH=".github/workflows/deploy.yml"

# 1. Criar Service Principal
echo "🔐 Criando Service Principal..."
az ad sp create-for-rbac \
  --name "GitHub-Action-Deploy-PokemonFiap" \
  --role "Contributor" \
  --scopes "/subscriptions/$(az account show --query id -o tsv)/resourceGroups/$RESOURCE_GROUP/providers/Microsoft.Web/sites/$APP_NAME" \
  --sdk-auth > azure-credentials.json

# 2. Criar segredo no GitHub
echo "🔑 Adicionando segredo AZURE_CREDENTIALS ao GitHub..."
gh secret set AZURE_CREDENTIALS --repo "$REPO" < azure-credentials.json

# 3. Apagar o JSON local
rm azure-credentials.json

# 4. Criar o arquivo de workflow
echo "🛠️ Gerando workflow de deploy em $WORKFLOW_PATH..."
mkdir -p .github/workflows
cat > "$WORKFLOW_PATH" << 'EOF'
name: Build and Deploy to Azure Web App

on:
  push:
    branches:
      - main
  workflow_dispatch:

jobs:
  build-and-deploy:
    runs-on: ubuntu-latest

    steps:
    - name: Checkout code
      uses: actions/checkout@v4

    - name: Set up Java version
      uses: actions/setup-java@v4
      with:
        java-version: '21'
        distribution: 'temurin'

    - name: Build with Maven
      run: mvn clean install

    - name: Login to Azure
      uses: azure/login@v1
      with:
        creds: ${{ secrets.AZURE_CREDENTIALS }}

    - name: Deploy to Azure Web App
      uses: azure/webapps-deploy@v3
      with:
        app-name: 'webapp-pokemonfiap-945-sprint3'
        package: '${{ github.workspace }}/target/*.jar'
EOF

# 5. Commit e push do workflow
git add "$WORKFLOW_PATH"
git commit -m "✨ Add GitHub Actions workflow for Azure deploy (PokemonFiap)"
git push origin main

````

echo "✅ Deploy automático configurado!"
b) Executar o script
bash
Copiar código

```bash
chmod +x deploy.sh
./deploy.sh
```


✅ Esse script vai:

Criar ou atualizar o Service Principal no Azure

Configurar o segredo AZURE_CREDENTIALS no GitHub

Gerar o workflow .github/workflows/deploy.yml

Commitar e pushar para a branch main

Disparar o deploy automático no Azure


# Após isso teste a aplicação

[Link da Aplicação](webapp-pokemonfiap-945-sprint3.azurewebsites.net)


# Após criar e testar o projeto

Crie o app.sh, e cole o seguinte script nele:

```bash
#!/bin/bash

# Variáveis
RESOURCE_GROUP="rg-pokemonfiap-sprint3"
WEBAPP_NAME="webapp-pokemonfiap-945-sprint3"
APP_INSIGHTS_NAME="appinsights-pokemonfiap"
LOCATION="eastus2"  # mesma região do Web App

echo "🔹 Criando Application Insights..."
az monitor app-insights component create \
    --app $APP_INSIGHTS_NAME \
    --location $LOCATION \
    --resource-group $RESOURCE_GROUP \
    --application-type web

echo "🔹 Recuperando Instrumentation Key..."
INSTRUMENTATION_KEY=$(az monitor app-insights component show \
    --app $APP_INSIGHTS_NAME \
    --resource-group $RESOURCE_GROUP \
    --query instrumentationKey -o tsv)

echo "🔹 Configurando Web App para enviar métricas ao Application Insights..."
az webapp config appsettings set \
    --name $WEBAPP_NAME \
    --resource-group $RESOURCE_GROUP \
    --settings "APPINSIGHTS_INSTRUMENTATIONKEY=$INSTRUMENTATION_KEY"

echo "🔹 Verificando se a configuração foi aplicada..."
az webapp config appsettings list \
    --name $WEBAPP_NAME \
    --resource-group $RESOURCE_GROUP

echo "✅ Tudo pronto! Web App configurado com Application Insights."
```

Passo 2: Rodar o script

Abra o Azure Cloud Shell ou terminal com o Azure CLI instalado.

Navegue até o diretório onde o app.sh está salvo.

Dê permissão de execução ao script:
```bash
chmod +x app.sh
```

Execute o script:

```bash
./app.sh
````


E pronto, o Application Insights, foi criado e configurado.

4️⃣ Testando localmente
Antes do deploy, você pode rodar a aplicação localmente:

Certifique-se de ter o Java 21 e Maven instalados.

Configure as variáveis de ambiente:

bash
Copiar código

```bash
SPRING_DATASOURCE_USERNAME=Trainer
SPRING_DATASOURCE_PASSWORD=Azurecast@666
SPRING_DATASOURCE_URL=jdbc:sqlserver://sqlserver-pokemonfiap-945-sprint3.database.windows.net:1433;database=pokemonfiapDB;encrypt=true;trustServerCertificate=false;


```
Rode a aplicação:

bash
Copiar código
```bash
mvn spring-boot:run
````
Acesse http://localhost:8080 no navegador.




# Veja o video do deploy da aplicação

[Vídeo](https://youtu.be/wSgRGp5Qy7g?si=5gA6-7cTAiBIiqx8)

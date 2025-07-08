# SmartAgenda - Deployment Docker

Guida completa per il deployment di SmartAgenda utilizzando Docker e Docker Compose.

## 🚀 Avvio Rapido

### Prerequisiti
- Docker Desktop (Windows/macOS) o Docker Engine (Linux)
- Docker Compose
- Java 11+ (per la compilazione)

### Avvio con Un Solo Comando

#### Linux/macOS
```bash
./start.sh
```

#### Windows
```cmd
start.bat
```

#### Manuale
```bash
# 1. Build del progetto
./build.sh          # Linux/macOS
build.bat           # Windows

# 2. Avvio dei container
docker-compose up --build -d
```

## 📁 Struttura del Progetto

```
ProgettoSmartAgenda/
├── Dockerfile              # Configurazione container app
├── docker-compose.yml      # Orchestrazione servizi
├── .dockerignore           # File esclusi dal build
├── docker/
│   └── context.xml         # Configurazione Tomcat
├── build.sh/.bat           # Script di build
├── start.sh/.bat           # Script di avvio completo
└── target/
    └── SmartAgenda.war     # Applicazione compilata
```

## 🐳 Servizi Docker

### MySQL Database
- **Immagine**: mysql:8.0
- **Container**: smartagenda-mysql
- **Porta**: 3306
- **Database**: SMARTAGENDA
- **Utente**: alessandro / studenteEcampus2025

### SmartAgenda App
- **Immagine**: Tomcat 9.0 + JDK 11
- **Container**: smartagenda-app
- **Porta**: 8080
- **Context**: ROOT (accessibile da /)

## 🔧 Comandi Utili

### Gestione Container
```bash
# Avvia i servizi
docker-compose up -d

# Ferma i servizi
docker-compose down

# Riavvia i servizi
docker-compose restart

# Visualizza lo stato
docker-compose ps

# Visualizza i log
docker-compose logs -f

# Visualizza log di un servizio specifico
docker-compose logs -f smartagenda
docker-compose logs -f mysql
```

### Accesso ai Container
```bash
# Accesso al container dell'app
docker-compose exec smartagenda bash

# Accesso al database MySQL
docker-compose exec mysql mysql -u alessandro -p SMARTAGENDA

# Accesso come root MySQL
docker-compose exec mysql mysql -u root -p
```

### Gestione Dati
```bash
# Backup del database
docker-compose exec mysql mysqldump -u alessandro -p SMARTAGENDA > backup.sql

# Ripristino del database
docker-compose exec -T mysql mysql -u alessandro -p SMARTAGENDA < backup.sql

# Visualizza volumi
docker volume ls

# Rimuovi tutti i dati (ATTENZIONE!)
docker-compose down --volumes
```

## 🌐 Accesso all'Applicazione

### URL Principale
- **Applicazione**: http://localhost:8080
- **Login**: http://localhost:8080/jsp/login.jsp

### Utenti Demo
| Username   | Password     | Ruolo          |
|------------|--------------|----------------|
| admin      | admin123     | Amministratore |
| tizio      | tizio123     | Utente         |
| caio       | caio123      | Utente         |
| sempronio  | sempronio123 | Utente         |

## 🔧 Configurazione

### Variabili di Ambiente
Il file `docker-compose.yml` include le seguenti variabili:

```yaml
environment:
  - DB_HOST=mysql
  - DB_PORT=3306
  - DB_NAME=SMARTAGENDA
  - DB_USER=alessandro
  - DB_PASSWORD=studenteEcampus2025
```

### Porte Esposte
- **8080**: Applicazione SmartAgenda
- **3306**: Database MySQL (opzionale, per accesso esterno)

### Volumi Persistenti
- **mysql_data**: Dati del database MySQL
- **app_logs**: Log dell'applicazione

## 🛠️ Personalizzazione

### Modificare la Configurazione del Database
Edita il file `docker-compose.yml`:

```yaml
mysql:
  environment:
    MYSQL_ROOT_PASSWORD: tuapassword
    MYSQL_DATABASE: TUODATABASE
    MYSQL_USER: tuoutente
    MYSQL_PASSWORD: tuapassword
```

### Modificare la Porta dell'Applicazione
```yaml
smartagenda:
  ports:
    - "8081:8080"  # Cambia 8081 con la porta desiderata
```

### Aggiungere Configurazioni Personalizzate
Crea file in `docker/` e montali nel container:

```yaml
smartagenda:
  volumes:
    - ./docker/server.xml:/usr/local/tomcat/conf/server.xml
```

## 🐛 Risoluzione Problemi

### L'applicazione non si avvia
```bash
# Controlla i log
docker-compose logs smartagenda

# Verifica lo stato dei container
docker-compose ps

# Riavvia i servizi
docker-compose restart
```

### Errori di connessione al database
```bash
# Controlla se MySQL è pronto
docker-compose exec mysql mysqladmin ping -h localhost

# Verifica la configurazione di rete
docker network ls
docker network inspect progettosmartAgenda_smartagenda-network
```

### Problemi di memoria
```bash
# Aumenta la memoria per Docker Desktop
# Windows/macOS: Docker Desktop > Settings > Resources > Memory

# Linux: modifica /etc/docker/daemon.json
{
  "default-runtime": "runc",
  "runtimes": {
    "runc": {
      "path": "runc"
    }
  }
}
```

### Pulizia Completa
```bash
# Ferma tutto e rimuovi volumi
docker-compose down --volumes --rmi all

# Rimuovi immagini non utilizzate
docker image prune -a

# Rimuovi volumi non utilizzati
docker volume prune
```

## 📦 Distribuzione

### Creazione di un Pacchetto Completo
```bash
# Crea un archivio con tutti i file necessari
tar -czf smartagenda-docker.tar.gz \
  Dockerfile \
  docker-compose.yml \
  .dockerignore \
  docker/ \
  src/ \
  setup_smartagenda.sql \
  build.sh \
  start.sh \
  DOCKER_README.md
```

### Deployment su Server
```bash
# Estrai l'archivio
tar -xzf smartagenda-docker.tar.gz

# Avvia l'applicazione
./start.sh
```

## 🔒 Sicurezza

### Raccomandazioni per la Produzione
1. **Cambia le password di default**
2. **Usa HTTPS con reverse proxy (nginx)**
3. **Limita l'accesso alle porte**
4. **Abilita i log di audit**
5. **Aggiorna regolarmente le immagini**

### Esempio con Nginx (opzionale)
```yaml
nginx:
  image: nginx:alpine
  ports:
    - "80:80"
    - "443:443"
  volumes:
    - ./nginx.conf:/etc/nginx/nginx.conf
  depends_on:
    - smartagenda
```

## 📞 Supporto

Per problemi o domande:
1. Controlla i log con `docker-compose logs`
2. Verifica lo stato dei container con `docker-compose ps`
3. Consulta la documentazione Docker ufficiale
4. Controlla la configurazione di rete e firewall 
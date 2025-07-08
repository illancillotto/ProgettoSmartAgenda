# SmartAgenda - Quick Start Guide

## 🚀 Avvio Rapido (1 minuto)

### Prerequisiti
- Docker Desktop (Windows/macOS) o Docker Engine (Linux)
- Java 11+ (per la compilazione)

### Avvio Immediato

#### Linux/macOS
```bash
./start.sh
```

#### Windows
```cmd
start.bat
```

### Avvio Manuale
```bash
# 1. Compila il progetto
./build.sh          # Linux/macOS
build.bat           # Windows

# 2. Avvia i container
docker-compose up --build -d
```

## 🌐 Accesso

- **URL**: http://localhost:8080
- **Login**: http://localhost:8080/jsp/login.jsp

## 👥 Utenti Demo

| Username   | Password     | Ruolo          |
|------------|--------------|----------------|
| admin      | admin123     | Amministratore |
| tizio      | tizio123     | Utente         |
| caio       | caio123      | Utente         |
| sempronio  | sempronio123 | Utente         |

## 🔧 Comandi Utili

```bash
# Visualizza stato
docker-compose ps

# Visualizza log
docker-compose logs -f

# Ferma i container
docker-compose down

# Riavvia
docker-compose restart

# Pulizia completa
docker-compose down --volumes --rmi all
```

## 🎯 Funzionalità Principali

- **Gestione Appuntamenti**: Crea, modifica, elimina appuntamenti
- **Categorie Colorate**: Organizza appuntamenti per categoria
- **Condivisione**: Condividi appuntamenti tra utenti
- **Inviti**: Sistema di inviti per appuntamenti condivisi
- **Ricerca**: Cerca appuntamenti per titolo/descrizione
- **Statistiche**: Dashboard per amministratori
- **Multi-utente**: Gestione di utenti con ruoli diversi

## 🐛 Risoluzione Problemi

### Porta 3306 occupata
Se MySQL è già in esecuzione localmente, il docker-compose usa automaticamente la porta 3307.

### Errori di compilazione
Assicurati di avere Java 11+ installato:
```bash
java -version
```

### Container non si avviano
```bash
# Pulisci tutto e riprova
docker-compose down --volumes --rmi all
./start.sh
```

## 📚 Documentazione Completa

- **DOCKER_README.md**: Guida dettagliata Docker
- **readme.txt**: Documentazione completa del progetto

## 🎉 Pronto!

L'applicazione è ora disponibile su http://localhost:8080

Buon utilizzo! 🚀 
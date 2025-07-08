#!/bin/bash

echo "============================================================"
echo "        SMARTAGENDA - AVVIO COMPLETO"
echo "============================================================"
echo

# Colori per output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

print_status() {
    echo -e "${GREEN}✓${NC} $1"
}

print_info() {
    echo -e "${BLUE}ℹ${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}⚠${NC} $1"
}

print_error() {
    echo -e "${RED}❌${NC} $1"
}

# Controlla se Docker è installato
if ! command -v docker &> /dev/null; then
    print_error "Docker non è installato!"
    echo
    echo "Installa Docker da: https://www.docker.com/get-started"
    exit 1
fi

# Controlla se Docker Compose è installato
if ! command -v docker-compose &> /dev/null; then
    print_error "Docker Compose non è installato!"
    echo
    echo "Installa Docker Compose da: https://docs.docker.com/compose/install/"
    exit 1
fi

print_status "Docker e Docker Compose trovati"

# Controlla se il file docker-compose.yml esiste
if [ ! -f "docker-compose.yml" ]; then
    print_error "File docker-compose.yml non trovato!"
    exit 1
fi

# Ferma eventuali container in esecuzione
print_info "Fermando eventuali container in esecuzione..."
docker-compose down 2>/dev/null

# Pulisce le immagini vecchie (opzionale)
read -p "Vuoi rimuovere le immagini Docker esistenti? (s/N): " -n 1 -r
echo
if [[ $REPLY =~ ^[Ss]$ ]]; then
    print_info "Rimozione immagini esistenti..."
    docker-compose down --rmi all --volumes 2>/dev/null
fi

# Build del progetto
print_info "Build del progetto Java..."
if [ -f "build.sh" ]; then
    chmod +x build.sh
    ./build.sh
    if [ $? -ne 0 ]; then
        print_error "Errore durante il build del progetto"
        exit 1
    fi
else
    print_error "Script build.sh non trovato!"
    exit 1
fi

# Verifica che il file WAR sia stato creato
if [ ! -f "target/SmartAgenda.war" ]; then
    print_error "File WAR non trovato! Il build potrebbe essere fallito."
    exit 1
fi

print_status "File WAR creato con successo"

# Avvia i container
print_info "Avvio dei container Docker..."
echo
docker-compose up --build -d

if [ $? -eq 0 ]; then
    print_status "Container avviati con successo!"
    echo
    
    # Aspetta che i servizi siano pronti
    print_info "Attendo che i servizi siano pronti..."
    
    # Attende MySQL
    echo -n "MySQL: "
    for i in {1..30}; do
        if docker-compose exec mysql mysqladmin ping -h localhost --silent; then
            print_status "MySQL pronto"
            break
        fi
        echo -n "."
        sleep 2
    done
    
    # Attende l'applicazione
    echo -n "SmartAgenda: "
    for i in {1..30}; do
        if curl -s http://localhost:8080 > /dev/null; then
            print_status "SmartAgenda pronto"
            break
        fi
        echo -n "."
        sleep 2
    done
    
    echo
    echo "============================================================"
    echo "        SMARTAGENDA AVVIATO CON SUCCESSO!"
    echo "============================================================"
    echo
    echo "🌐 Accesso all'applicazione:"
    echo "   URL: http://localhost:8080"
    echo
    echo "👥 Utenti demo disponibili:"
    echo "   ┌─────────────┬─────────────┬─────────────────────────┐"
    echo "   │ Username    │ Password    │ Ruolo                   │"
    echo "   ├─────────────┼─────────────┼─────────────────────────┤"
    echo "   │ admin       │ admin123    │ Amministratore          │"
    echo "   │ tizio       │ tizio123    │ Utente (Professionista) │"
    echo "   │ caio        │ caio123     │ Utente (Studente)       │"
    echo "   │ sempronio   │ sempronio123│ Utente (Manager)        │"
    echo "   └─────────────┴─────────────┴─────────────────────────┘"
    echo
    echo "🔧 Comandi utili:"
    echo "   - Visualizza log: docker-compose logs -f"
    echo "   - Ferma i container: docker-compose down"
    echo "   - Riavvia: docker-compose restart"
    echo "   - Accesso MySQL: docker-compose exec mysql mysql -u alessandro -p SMARTAGENDA"
    echo
    echo "📊 Stato dei container:"
    docker-compose ps
    
else
    print_error "Errore durante l'avvio dei container"
    echo
    echo "Controlla i log con: docker-compose logs"
    exit 1
fi 
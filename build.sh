#!/bin/bash

echo "============================================================"
echo "        BUILD SMARTAGENDA PER DOCKER"
echo "============================================================"
echo

# Colori per output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Funzione per stampare messaggi colorati
print_status() {
    echo -e "${GREEN}✓${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}⚠${NC} $1"
}

print_error() {
    echo -e "${RED}❌${NC} $1"
}

# Controlla se esiste la directory src
if [ ! -d "src" ]; then
    print_error "Directory src non trovata! Assicurati di essere nella root del progetto."
    exit 1
fi

# Crea directory target se non esiste
mkdir -p target
mkdir -p target/WEB-INF/classes
mkdir -p target/WEB-INF/lib

print_status "Directory target create"

# Copia le librerie necessarie
if [ -d "src/main/webapp/WEB-INF/lib" ]; then
    cp -r src/main/webapp/WEB-INF/lib/* target/WEB-INF/lib/
    print_status "Librerie copiate"
else
    print_warning "Directory lib non trovata, potresti dover aggiungere manualmente le librerie"
fi

# Copia web.xml
if [ -f "src/main/webapp/WEB-INF/web.xml" ]; then
    cp src/main/webapp/WEB-INF/web.xml target/WEB-INF/
    print_status "web.xml copiato"
fi

# Copia le risorse webapp
cp -r src/main/webapp/* target/
print_status "Risorse webapp copiate"

# Compila i file Java
echo
echo "🔨 Compilazione file Java..."

# Trova tutte le librerie JAR
CLASSPATH=""
for jar in target/WEB-INF/lib/*.jar; do
    if [ -f "$jar" ]; then
        CLASSPATH="$CLASSPATH:$jar"
    fi
done

# Compila tutti i file Java
find src/main/java -name "*.java" -type f > sources.txt

if [ -s sources.txt ]; then
    javac -cp ".:$CLASSPATH" -d target/WEB-INF/classes @sources.txt
    
    if [ $? -eq 0 ]; then
        print_status "Compilazione completata con successo"
    else
        print_error "Errore durante la compilazione"
        rm -f sources.txt
        exit 1
    fi
else
    print_error "Nessun file Java trovato"
    exit 1
fi

# Pulisci file temporanei
rm -f sources.txt

# Crea il file WAR
echo
echo "📦 Creazione file WAR..."
cd target
jar -cvf SmartAgenda.war *
cd ..

if [ -f "target/SmartAgenda.war" ]; then
    print_status "File WAR creato: target/SmartAgenda.war"
    
    # Mostra dimensione del file
    size=$(du -h target/SmartAgenda.war | cut -f1)
    echo "   Dimensione: $size"
else
    print_error "Errore nella creazione del file WAR"
    exit 1
fi

echo
echo "============================================================"
echo "        BUILD COMPLETATO CON SUCCESSO!"
echo "============================================================"
echo
echo "📁 File creati:"
echo "   - target/SmartAgenda.war"
echo
echo "🚀 Prossimi passi:"
echo "   1. docker-compose up --build"
echo "   2. Accedi a http://localhost:8080"
echo
echo "👥 Utenti demo disponibili:"
echo "   - admin / admin123"
echo "   - tizio / tizio123"
echo "   - caio / caio123"
echo "   - sempronio / sempronio123"
echo 
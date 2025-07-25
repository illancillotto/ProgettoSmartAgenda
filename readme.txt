SMART AGENDA - Sistema di Gestione Appuntamenti
=============================================

Un'applicazione web Java per la gestione di appuntamenti con funzionalità avanzate
di categorizzazione, condivisione e gestione multi-utente.

SETUP DATABASE
--------------

**METODO 1: Docker (Raccomandato)**
- Il database viene creato automaticamente dai container Docker
- Nessuna configurazione manuale necessaria
- Tutti gli utenti demo vengono creati automaticamente

**METODO 2: Setup Manuale (Solo per deployment tradizionale)**
1. Installare MySQL server
2. Eseguire manualmente il file setup_smartagenda.sql per creare il database e le tabelle
3. Il database verrà creato con il nome SMARTAGENDA
4. Verrà creato l'utente MySQL 'alessandro' con password 'studenteEcampus2025'

UTENTI DEMO
-----------
Il sistema include diversi utenti demo per testare le funzionalità:

1. AMMINISTRATORE
   - Username: admin
   - Password: admin123
   - Email: admin@smartagenda.com
   - Ruolo: admin
   - Funzionalità: Gestione completa del sistema, statistiche, creazione appuntamenti per altri utenti

2. TIZIO
   - Username: tizio
   - Password: tizio123
   - Email: tizio@smartagenda.com
   - Ruolo: utente
   - Categorie: Lavoro (blu), Sport (verde acqua), Famiglia (rosa)
   - Appuntamenti: Riunioni di lavoro, allenamenti, cene familiari

3. CAIO
   - Username: caio
   - Password: caio123
   - Email: caio@smartagenda.com
   - Ruolo: utente
   - Categorie: Università (viola), Hobby (arancione), Medico (rosso)
   - Appuntamenti: Esami universitari, corsi di chitarra, visite mediche

4. SEMPRONIO
   - Username: sempronio
   - Password: sempronio123
   - Email: sempronio@smartagenda.com
   - Ruolo: utente
   - Categorie: Lavoro (grigio scuro), Viaggi (azzurro), Casa (verde)
   - Appuntamenti: Meeting clienti, organizzazione viaggi, pulizie domestiche

**Diversità di Profili:**
- **tizio:** Professionista generico con vita sociale
- **caio:** Studente universitario con hobby e salute
- **sempronio:** Manager con focus su lavoro e lifestyle

FUNZIONALITÀ PRINCIPALI
-----------------------
- Gestione appuntamenti con data, ora, titolo e descrizione
- Sistema di categorie personalizzabili con colori
- Condivisione appuntamenti tra utenti
- Vista calendario con evidenziazione appuntamenti passati/odierni
- Ricerca appuntamenti per titolo/descrizione
- Dashboard statistiche per amministratori
- Gestione multi-utente con ruoli differenziati
- Sistema di notifiche e promemoria

CARATTERISTICHE TECNICHE
------------------------
- Architettura MVC con servlet Java
- Database MySQL con relazioni normalizzate
- Frontend responsive con Bootstrap 5
- Sicurezza con hash SHA-256 per le password
- Gestione sessioni utente
- Filtri di autenticazione
- Pattern DAO per accesso ai dati

STRUTTURA PROGETTO
------------------
src/main/java/
├── controller/     # Servlet per gestione richieste
├── dao/           # Data Access Objects
├── model/         # Classi modello
└── filter/        # Filtri per autenticazione

src/main/webapp/
├── jsp/           # Pagine JSP
├── WEB-INF/       # Configurazioni
└── META-INF/      # Metadati applicazione

DEPLOYMENT
----------

**OPZIONE 1: Docker (Raccomandato per Distribuzione)**
1. Installare Docker e Docker Compose
2. Avviare con un comando:
   - **Linux/macOS**: `./start.sh`
   - **Windows**: `start.bat`
3. Accedere a http://localhost:8080

**OPZIONE 2: Deployment Tradizionale**
1. Configurare il database MySQL
2. Importare manualmente setup_smartagenda.sql
3. Configurare il context.xml con i parametri del database
4. Deployare su server Tomcat
5. Accedere all'applicazione via browser

**File di Setup Disponibili:**
- **Docker**: Dockerfile, docker-compose.yml, build.sh/.bat, start.sh/.bat
- **Database**: setup_smartagenda.sql (per deployment tradizionale)
- **Documentazione**: DOCKER_README.md per deployment containerizzato

Per ulteriori informazioni consultare la documentazione tecnica inclusa. 
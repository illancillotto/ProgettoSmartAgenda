# Gestione Appuntamenti Multi-Utente - SmartAgenda

## Panoramica

L'applicazione SmartAgenda è stata migliorata per supportare una gestione avanzata degli appuntamenti multi-utente, con funzionalità specifiche per amministratori e utenti normali.

## Funzionalità Implementate

### 1. **Creazione Appuntamenti per Altri Utenti (Admin)**

Gli amministratori possono ora creare appuntamenti per conto di altri utenti:

- **Interfaccia**: Dropdown di selezione utente nel form di creazione (solo per admin)
- **Validazione**: Controllo esistenza utente target
- **Notifiche**: Promemoria automatici per l'utente target
- **Categorie**: Verifica che le categorie appartengano all'utente corretto

### 2. **Metodi DAO Potenziati**

#### AppuntamentoDAO - Nuovi Metodi:
- `insertForUser(Appuntamento app, int targetUserId)`: Crea appuntamento per utente specifico
- `findByMultipleUsers(List<Integer> userIds)`: Trova appuntamenti per più utenti
- `countByUser(int idUtente)`: Conta appuntamenti per utente
- `canUserModifyAppointment(int appointmentId, int userId, boolean isAdmin)`: Verifica permessi modifica
- `transferAppointments(int fromUserId, int toUserId)`: Trasferisce appuntamenti tra utenti

#### CategoriaDAO - Nuovo Metodo:
- `belongsToUser(int idCategoria, int idUtente)`: Verifica appartenenza categoria

### 3. **Statistiche Appuntamenti**

Nuova sezione dedicata alle statistiche (solo admin):

- **Metriche Generali**:
  - Totale appuntamenti nel sistema
  - Appuntamenti condivisi
  - Numero utenti registrati
  - Media appuntamenti per utente

- **Statistiche per Utente**:
  - Tabella con conteggio appuntamenti per ogni utente
  - Informazioni ruolo e contatto
  - Link rapido per creare appuntamenti

### 4. **Interfaccia Utente Migliorata**

#### Agenda (agenda.jsp):
- **Selezione Utente**: Dropdown per admin per creare appuntamenti per altri utenti
- **Pulsante Statistiche**: Accesso rapido alle statistiche (solo admin)
- **Visualizzazione Migliorata**: Mostra username del proprietario per admin

#### Nuova Pagina Statistiche (stats.jsp):
- **Dashboard Visuale**: Cards colorate con metriche principali
- **Tabella Utenti**: Dettagli completi con conteggio appuntamenti
- **Navigazione**: Link rapidi per tornare all'agenda o al pannello admin

### 5. **Sicurezza e Validazione**

- **Controllo Permessi**: Verifica ruolo admin per funzioni avanzate
- **Validazione Input**: Controllo esistenza utenti target
- **Gestione Errori**: Messaggi informativi per operazioni non valide

## Utilizzo

### Per Amministratori:

1. **Creare Appuntamento per Altro Utente**:
   - Accedere all'agenda
   - Nel form "Nuovo Appuntamento", selezionare l'utente target dal dropdown
   - Compilare i dettagli dell'appuntamento
   - Cliccare "Aggiungi Appuntamento"

2. **Visualizzare Statistiche**:
   - Dall'agenda, cliccare su "Statistiche"
   - Visualizzare metriche generali e per utente
   - Utilizzare i link rapidi per azioni specifiche

### Per Utenti Normali:

- Le funzionalità esistenti rimangono invariate
- Possono creare solo appuntamenti per se stessi
- Accesso alle funzioni standard di gestione agenda

## Struttura Tecnica

### Controller
- `AppuntamentoServlet`: Gestisce le nuove azioni "stats" e la creazione multi-utente

### DAO
- `AppuntamentoDAO`: Metodi per gestione multi-utente e statistiche
- `CategoriaDAO`: Validazione appartenenza categorie

### JSP
- `agenda.jsp`: Form migliorato con selezione utente
- `stats.jsp`: Nuova pagina per statistiche (solo admin)

### Database
- Struttura esistente mantenuta
- Utilizzo ottimizzato delle relazioni tra tabelle

## Vantaggi

1. **Gestione Centralizzata**: Admin possono gestire appuntamenti di tutti gli utenti
2. **Monitoraggio**: Statistiche dettagliate sull'utilizzo del sistema
3. **Sicurezza**: Controlli di accesso basati su ruoli
4. **Usabilità**: Interfaccia intuitiva e responsive
5. **Scalabilità**: Architettura pronta per future estensioni

## Compatibilità

- Mantiene piena compatibilità con funzionalità esistenti
- Non richiede modifiche al database
- Interfaccia responsive per dispositivi mobili

## Note Tecniche

- Utilizzo di Bootstrap 5.1.3 per styling
- Font Awesome 6.0.0 per icone
- Validazione lato server per sicurezza
- Gestione errori completa con messaggi utente-friendly 
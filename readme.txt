# SmartAgenda – Web Application per gestione appuntamenti

Progetto realizzato in JavaEE (Servlet + JSP) con pattern MVC, database MySQL e deploy su Tomcat 9.  
L'applicazione consente la registrazione utenti, login, gestione agenda personale e condivisa.

---

## ✅ Requisiti

- Java JDK 17
- Apache Tomcat 9.x
- MySQL Server (5.7 o 8.x)
- Eclipse EE o altro IDE compatibile
- MySQL Connector/J (`mysql-connector-java-8.x.x.jar`) incluso in `WEB-INF/lib`

---

## 📁 Struttura del progetto

ProgettoSmartAgenda/
│
├── src/ # Codice Java (model, dao, controller)
├── src/main/webapp/ # Contenuti web (jsp, WEB-INF)
│ ├── jsp/
│ │ ├── login.jsp
│ │ ├── registration.jsp
│ │ ├── agenda.jsp
│ │ └── nuovoAppuntamento.jsp
│ └── WEB-INF/
│ ├── db.properties # Configurazione DB (non esportare con credenziali reali)
│ └── web.xml
│
├── setup_smartagenda.sql # Script per creare DB, utente, tabelle
├── README.txt # Istruzioni (questo file)
 
---

## ⚙️ Configurazione MySQL

1. Apri un terminale e lancia il seguente comando:
mysql -u root -p < setup_smartagenda.sql

yaml
Copia

Oppure, accedi a MySQL da terminale o phpMyAdmin e copia manualmente il contenuto di `setup_smartagenda.sql`.

2. Lo script crea:
- Database `smartagenda`
- Utente `alessandro` con password `studenteEcampus2025`
- Tabelle `utenti` e `appuntamenti`

---

## 🔑 File `WEB-INF/db.properties`

Contiene la configurazione del database:
db.url=jdbc:mysql://localhost:3306/smartagenda
db.user=alessandro
db.password=studenteEcampus2025

yaml
Copia

**⚠️ Non inserire questo file in repository pubblici se contiene credenziali reali.**

---

## 🚀 Deploy del progetto

1. Importa il progetto in Eclipse come *Dynamic Web Project*.
2. Aggiungi il server **Apache Tomcat 9**.
3. Fai clic destro → Run on Server.
4. Visita:  
   `http://localhost:8080/ProgettoSmartAgenda/jsp/login.jsp`

---

## 🧪 Funzionalità disponibili

- Registrazione nuovo utente
- Login e sessione autenticata
- Visualizzazione degli appuntamenti personali
- Inserimento appuntamenti (con campo “condiviso”)
- Logout

---

## 📌 Note finali

- Il file `db.properties` viene letto da `/WEB-INF/` tramite `ServletContext`.
- Il driver JDBC è incluso manualmente nella cartella `WEB-INF/lib`.
- Non è necessario Maven, il progetto è pensato per essere facilmente esportabile e avviabile in ambiente accademico o didattico.

---

© Alessandro Porcu – Università eCampus
Nota sull’avvio del progetto e sulla configurazione
Il progetto SmartAgenda è stato dockerizzato per rendere l’avvio e il deploy più rapidi e semplici.
Grazie a Docker e al file docker-compose.yml, è possibile avviare automaticamente sia il backend Java (Tomcat) sia il database MySQL già configurato per l’applicazione. 

Avvio rapido con Docker - TESTATO SU LINUX
Clonare la repository:
git clone https://github.com/illancillotto/ProgettoSmartAgenda.git

Accedere alla cartella del progetto:
cd ProgettoSmartAgenda

Avviare i servizi:
docker-compose up

In questo modo l’applicazione sarà disponibile senza necessità di configurazioni manuali.

Avvio manuale in Eclipse
In alternativa, il progetto può essere importato direttamente in Eclipse come progetto Maven.
Per l’avvio manuale occorre:

Importare il progetto come “Existing Maven Project” in Eclipse.

Configurare un server Tomcat in locale.

Configurare un database MySQL accessibile localmente.

Creare le tabelle necessarie eseguendo lo script SQL fornito nel progetto:

Il file da utilizzare è setup_smartagenda.sql (presente nella repository).

Basta eseguire questo script all’interno dell’istanza MySQL per creare tutte le tabelle richieste dall’applicazione.

Repository ufficiale
Tutta la documentazione, i file per Docker e lo script SQL sono disponibili nel repository:

https://github.com/illancillotto/ProgettoSmartAgenda
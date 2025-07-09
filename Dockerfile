# Dockerfile per SmartAgenda
FROM tomcat:9.0-jdk11-openjdk

# Informazioni sul maintainer
LABEL maintainer="SmartAgenda Team"
LABEL description="SmartAgenda - Sistema di Gestione Appuntamenti"
LABEL version="1.0"

# Variabili di ambiente
ENV CATALINA_HOME /usr/local/tomcat
ENV PATH $CATALINA_HOME/bin:$PATH

# Rimuove le app di default di Tomcat
RUN rm -rf $CATALINA_HOME/webapps/*

# Copia il file WAR dell'applicazione
COPY target/SmartAgenda.war $CATALINA_HOME/webapps/ROOT.war

# Estrai il WAR per poter modificare la configurazione
RUN cd $CATALINA_HOME/webapps && unzip -q ROOT.war -d ROOT && rm ROOT.war

# Modifica la configurazione del database per Docker (sostituisce localhost con mysql)
RUN sed -i 's/localhost/mysql/g' $CATALINA_HOME/webapps/ROOT/WEB-INF/db.properties

# Copia la configurazione del database specifica per Docker
COPY docker/context.xml $CATALINA_HOME/conf/context.xml

# Crea directory per i log
RUN mkdir -p /var/log/smartagenda

# Espone la porta 8080
EXPOSE 8080

# Comando di avvio
CMD ["catalina.sh", "run"] 
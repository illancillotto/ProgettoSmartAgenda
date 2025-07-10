@echo off
echo ============================================================
echo        BUILD SMARTAGENDA PER DOCKER - WINDOWS
echo ============================================================
echo.

REM Controlla se esiste la directory src
if not exist "src" (
    echo ERRORE: Directory src non trovata! Assicurati di essere nella root del progetto.
    pause
    exit /b 1
)

REM Crea directory target se non esiste
if not exist "target" mkdir target
if not exist "target\WEB-INF" mkdir target\WEB-INF
if not exist "target\WEB-INF\classes" mkdir target\WEB-INF\classes
if not exist "target\WEB-INF\lib" mkdir target\WEB-INF\lib

echo ✓ Directory target create

REM Copia le librerie necessarie
if exist "src\main\webapp\WEB-INF\lib" (
    xcopy "src\main\webapp\WEB-INF\lib\*" "target\WEB-INF\lib\" /E /I /Y >nul
    echo ✓ Librerie copiate
) else (
    echo ⚠ Directory lib non trovata, potresti dover aggiungere manualmente le librerie
)

REM Copia web.xml
if exist "src\main\webapp\WEB-INF\web.xml" (
    copy "src\main\webapp\WEB-INF\web.xml" "target\WEB-INF\" >nul
    echo ✓ web.xml copiato
)

REM Copia le risorse webapp
xcopy "src\main\webapp\*" "target\" /E /I /Y >nul
echo ✓ Risorse webapp copiate

echo.
echo 🔨 Compilazione file Java...

REM Trova tutti i file Java
dir /s /b "src\main\java\*.java" > sources.txt

REM Costruisce il classpath
set CLASSPATH=.
for %%f in (target\WEB-INF\lib\*.jar) do (
    set CLASSPATH=!CLASSPATH!;%%f
)

REM Compila i file Java
javac -cp "%CLASSPATH%" -d target\WEB-INF\classes @sources.txt

if %errorlevel% equ 0 (
    echo ✓ Compilazione completata con successo
) else (
    echo ❌ Errore durante la compilazione
    del sources.txt
    pause
    exit /b 1
)

REM Pulisci file temporanei
del sources.txt

echo.
echo 📦 Creazione file WAR...

REM Controlla se il comando jar è disponibile
jar --version >nul 2>&1
if errorlevel 1 (
    echo ❌ ERRORE: Comando 'jar' non trovato!
    echo.
    echo 🔧 SOLUZIONE: Devi aggiungere il JDK al PATH di sistema
    echo.
    echo 📋 PASSI DA SEGUIRE:
    echo    1. Trova la cartella del JDK (es: C:\Program Files\Java\jdk-24\bin)
    echo    2. Apri "Variabili d'ambiente" dal menu Start
    echo    3. In "Variabili di sistema" trova "Path" e clicca "Modifica"
    echo    4. Clicca "Nuovo" e aggiungi il percorso del JDK\bin
    echo    5. Salva e riavvia il terminale
    echo.
    echo 💡 ALTERNATIVA: Se non hai il JDK, scaricalo da:
    echo    https://www.oracle.com/it/java/technologies/downloads/#jdk24-windows
    echo.
    echo ⚠ Dopo aver risolto, riavvia questo script.
    echo.
    pause
    exit /b 1
)

echo ✓ Comando jar trovato

REM Crea il file WAR
cd target
jar -cvf SmartAgenda.war *
cd ..

if exist "target\SmartAgenda.war" (
    echo ✓ File WAR creato: target\SmartAgenda.war
    
    REM Mostra dimensione del file
    for %%A in (target\SmartAgenda.war) do echo    Dimensione: %%~zA bytes
) else (
    echo ❌ Errore nella creazione del file WAR
    echo.
    echo 🔍 Possibili cause:
    echo    - Permessi insufficienti nella cartella target
    echo    - Spazio su disco insufficiente
    echo    - Problemi con il comando jar
    echo.
    pause
    exit /b 1
)

echo.
echo ============================================================
echo        BUILD COMPLETATO CON SUCCESSO!
echo ============================================================
echo.
echo 📁 File creati:
echo    - target\SmartAgenda.war
echo.
echo 🚀 Prossimi passi:
echo    1. docker-compose up --build
echo    2. Accedi a http://localhost:8080
echo.
echo 👥 Utenti demo disponibili:
echo    - admin / admin123
echo    - tizio / tizio123
echo    - caio / caio123
echo    - sempronio / sempronio123
echo.
pause 
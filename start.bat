@echo off
echo ============================================================
echo        SMARTAGENDA - AVVIO COMPLETO WINDOWS
echo ============================================================
echo.

REM Controlla se Docker è installato
docker --version >nul 2>&1
if errorlevel 1 (
    echo ERRORE: Docker non è installato!
    echo.
    echo Installa Docker Desktop da: https://www.docker.com/products/docker-desktop
    echo.
    pause
    exit /b 1
)

REM Controlla se Docker Compose è installato
docker-compose --version >nul 2>&1
if errorlevel 1 (
    echo ERRORE: Docker Compose non è installato!
    echo.
    echo Installa Docker Compose da: https://docs.docker.com/compose/install/
    echo.
    pause
    exit /b 1
)

echo ✓ Docker e Docker Compose trovati

REM Controlla se il file docker-compose.yml esiste
if not exist "docker-compose.yml" (
    echo ERRORE: File docker-compose.yml non trovato!
    pause
    exit /b 1
)

REM Ferma eventuali container in esecuzione
echo ℹ Fermando eventuali container in esecuzione...
docker-compose down >nul 2>&1

REM Chiede se rimuovere le immagini esistenti
set /p cleanup="Vuoi rimuovere le immagini Docker esistenti? (s/N): "
if /i "%cleanup%"=="s" (
    echo ℹ Rimozione immagini esistenti...
    docker-compose down --rmi all --volumes >nul 2>&1
)

REM Build del progetto
echo ℹ Build del progetto Java...
if exist "build.bat" (
    call build.bat
    if errorlevel 1 (
        echo ERRORE: Errore durante il build del progetto
        pause
        exit /b 1
    )
) else (
    echo ERRORE: Script build.bat non trovato!
    pause
    exit /b 1
)

REM Verifica che il file WAR sia stato creato
if not exist "target\SmartAgenda.war" (
    echo ERRORE: File WAR non trovato! Il build potrebbe essere fallito.
    pause
    exit /b 1
)

echo ✓ File WAR creato con successo

REM Avvia i container
echo ℹ Avvio dei container Docker...
echo.
docker-compose up --build -d

if %errorlevel% equ 0 (
    echo ✓ Container avviati con successo!
    echo.
    
    REM Aspetta che i servizi siano pronti
    echo ℹ Attendo che i servizi siano pronti...
    
    REM Attende MySQL (semplificato per Windows)
    echo Attendo MySQL...
    timeout /t 10 /nobreak >nul
    
    REM Attende l'applicazione
    echo Attendo SmartAgenda...
    timeout /t 15 /nobreak >nul
    
    echo.
    echo ============================================================
    echo        SMARTAGENDA AVVIATO CON SUCCESSO!
    echo ============================================================
    echo.
    echo 🌐 Accesso all'applicazione:
    echo    URL: http://localhost:8080
    echo.
    echo 👥 Utenti demo disponibili:
    echo    ┌─────────────┬─────────────┬─────────────────────────┐
    echo    │ Username    │ Password    │ Ruolo                   │
    echo    ├─────────────┼─────────────┼─────────────────────────┤
    echo    │ admin       │ admin123    │ Amministratore          │
    echo    │ tizio       │ tizio123    │ Utente (Professionista) │
    echo    │ caio        │ caio123     │ Utente (Studente)       │
    echo    │ sempronio   │ sempronio123│ Utente (Manager)        │
    echo    └─────────────┴─────────────┴─────────────────────────┘
    echo.
    echo 🔧 Comandi utili:
    echo    - Visualizza log: docker-compose logs -f
    echo    - Ferma i container: docker-compose down
    echo    - Riavvia: docker-compose restart
    echo.
    echo 📊 Stato dei container:
    docker-compose ps
    echo.
    
    REM Chiede se aprire il browser
    set /p openbrowser="Vuoi aprire l'applicazione nel browser? (s/N): "
    if /i "%openbrowser%"=="s" (
        start http://localhost:8080
    )
    
) else (
    echo ERRORE: Errore durante l'avvio dei container
    echo.
    echo Controlla i log con: docker-compose logs
    pause
    exit /b 1
)

echo.
echo Premi un tasto per chiudere...
pause >nul 
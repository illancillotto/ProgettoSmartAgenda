<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ page
        import="java.util.*, model.Utente, model.Appuntamento, model.Notifica, dao.UtenteDAO, dao.AppuntamentoDAO, dao.NotificaDAO"
        %>
        <% Utente utente=(Utente) session.getAttribute("utente"); if (utente==null ||
            !utente.getRuolo().equals("admin")) { response.sendRedirect(request.getContextPath() + "/jsp/login.jsp" );
            return; } String currentSection=(String) request.getAttribute("currentSection"); if (currentSection==null)
            currentSection="dashboard" ; UtenteDAO utenteDAO=new UtenteDAO(); List<Utente> utenti = utenteDAO.findAll();
            Map<String, Integer>statsUtenti = utenteDAO.getStatisticheUtenti();

                // Statistichedashboard
                int totalUtenti = utenti.size();
                int utentiAttivi =statsUtenti.getOrDefault("attivi", 0);
                int utentiInattivi =statsUtenti.getOrDefault("inattivi", 0);
                int adminCount =statsUtenti.getOrDefault("admin", 0);

                // Dati per sezioni specifiche
                List<Appuntamento> appuntamentiCondivisi = null;
                    List<Notifica> notifiche = null;

                        if ("appointments".equals(currentSection)) {
                        AppuntamentoDAO appDAO = new AppuntamentoDAO();
                        appuntamentiCondivisi = appDAO.findCondivisi();
                        } else if ("notifications".equals(currentSection)) {
                        NotificaDAO notifDAO = new NotificaDAO();
                        notifiche = notifDAO.findAll();
                        }
                        %>
                        <!DOCTYPE html>
                        <html lang="it">

                        <head>
                            <meta charset="UTF-8">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <title>Pannello Admin - SmartAgenda</title>
                            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
                                rel="stylesheet">
                            <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"
                                rel="stylesheet">
                            <style>
                                .status-badge {
                                    font-size: 0.8em;
                                }

                                .action-buttons {
                                    white-space: nowrap;
                                }

                                .table-responsive {
                                    max-height: 600px;
                                    overflow-y: auto;
                                }
                            </style>
                        </head>

                        <body>
                            <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
                                <div class="container">
                                    <a class="navbar-brand" href="#">
                                        <i class="fas fa-user-shield"></i> Admin Panel
                                    </a>
                                    <div class="navbar-nav ms-auto">
                                        <span class="navbar-text me-3">
                                            <i class="fas fa-user"></i>
                                            <%= utente.getUsername() %>
                                        </span>
                                        <a class="nav-link" href="<%= request.getContextPath() %>/logout">
                                            <i class="fas fa-sign-out-alt"></i> Logout
                                        </a>
                                    </div>
                                </div>
                            </nav>

                            <div class="container mt-4">
                                <!-- Messaggi di feedback -->
                                <% if (request.getAttribute("successo") !=null) { %>
                                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                                        <i class="fas fa-check-circle"></i>
                                        <%= request.getAttribute("successo") %>
                                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                                    </div>
                                    <% } %>
                                        <% if (request.getAttribute("errore") !=null) { %>
                                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                                <i class="fas fa-exclamation-triangle"></i>
                                                <%= request.getAttribute("errore") %>
                                                    <button type="button" class="btn-close"
                                                        data-bs-dismiss="alert"></button>
                                            </div>
                                            <% } %>

                                                <!-- Navigazione sezioni -->
                                                <ul class="nav nav-tabs mb-4">
                                                    <li class="nav-item">
                                                        <a class="nav-link <%= " dashboard".equals(currentSection)
                                                            ? "active" : "" %>"
                                                            href="<%= request.getContextPath() %>
                                                                /admin?action=dashboard">
                                                                <i class="fas fa-tachometer-alt"></i>dashboard
                                                        </a>
                                                    </li>
                                                    <li class="nav-item">
                                                        <a class="nav-link <%= " users".equals(currentSection)
                                                            ? "active" : "" %>"
                                                            href="<%= request.getContextPath() %>/admin?action=users">
                                                                <i class="fas fa-users"></i> Gestione Utenti
                                                        </a>
                                                    </li>
                                                    <li class="nav-item">
                                                        <a class="nav-link <%= " appointments".equals(currentSection)
                                                            ? "active" : "" %>"
                                                            href="<%= request.getContextPath() %>
                                                                /admin?action=appointments">
                                                                <i class="fas fa-calendar-check"></i> Appuntamenti
                                                                Condivisi
                                                        </a>
                                                    </li>
                                                    <li class="nav-item">
                                                        <a class="nav-link <%= " notifications".equals(currentSection)
                                                            ? "active" : "" %>"
                                                            href="<%= request.getContextPath() %>
                                                                /admin?action=notifications">
                                                                <i class="fas fa-bell"></i> Notifiche Sistema
                                                        </a>
                                                    </li>
                                                    <li class="nav-item">
                                                        <a class="nav-link <%= " stats".equals(currentSection)
                                                            ? "active" : "" %>"
                                                            href="<%= request.getContextPath() %>/admin?action=stats">
                                                                <i class="fas fa-chart-bar"></i> Statistiche
                                                        </a>
                                                    </li>
                                                </ul>

                                                <% if ("dashboard".equals(currentSection)) { %>
                                                    <!--dashboard -->
                                                    <div class="row mb-4">
                                                        <div class="col-md-3">
                                                            <div class="card bg-primary text-white">
                                                                <div class="card-body">
                                                                    <div class="d-flex justify-content-between">
                                                                        <div>
                                                                            <h4>
                                                                                <%= totalUtenti %>
                                                                            </h4>
                                                                            <p class="mb-0">Utenti Totali</p>
                                                                        </div>
                                                                        <div class="align-self-center">
                                                                            <i class="fas fa-users fa-2x"></i>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="col-md-3">
                                                            <div class="card bg-success text-white">
                                                                <div class="card-body">
                                                                    <div class="d-flex justify-content-between">
                                                                        <div>
                                                                            <h4>
                                                                                <%= utentiAttivi %>
                                                                            </h4>
                                                                            <p class="mb-0">Utenti Attivi</p>
                                                                        </div>
                                                                        <div class="align-self-center">
                                                                            <i class="fas fa-user-check fa-2x"></i>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="col-md-3">
                                                            <div class="card bg-warning text-white">
                                                                <div class="card-body">
                                                                    <div class="d-flex justify-content-between">
                                                                        <div>
                                                                            <h4>
                                                                                <%= utentiInattivi %>
                                                                            </h4>
                                                                            <p class="mb-0">Utenti Inattivi</p>
                                                                        </div>
                                                                        <div class="align-self-center">
                                                                            <i class="fas fa-user-times fa-2x"></i>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="col-md-3">
                                                            <div class="card bg-info text-white">
                                                                <div class="card-body">
                                                                    <div class="d-flex justify-content-between">
                                                                        <div>
                                                                            <h4>
                                                                                <%= adminCount %>
                                                                            </h4>
                                                                            <p class="mb-0">Amministratori</p>
                                                                        </div>
                                                                        <div class="align-self-center">
                                                                            <i class="fas fa-user-shield fa-2x"></i>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>

                                                    <!-- Azioni Rapide -->
                                                    <div class="row">
                                                        <div class="col-md-6">
                                                            <div class="card">
                                                                <div class="card-header">
                                                                    <h6><i class="fas fa-tools"></i> Azioni Rapide</h6>
                                                                </div>
                                                                <div class="card-body">
                                                                    <a href="<%= request.getContextPath() %>/admin?action=users"
                                                                        class="btn btn-primary mb-2">
                                                                        <i class="fas fa-users-cog"></i> Gestisci Utenti
                                                                    </a><br>
                                                                    <a href="<%= request.getContextPath() %>/admin?action=notifications"
                                                                        class="btn btn-warning mb-2">
                                                                        <i class="fas fa-bell"></i> Invia Notifiche
                                                                    </a><br>
                                                                    <a href="<%= request.getContextPath() %>/admin?action=stats"
                                                                        class="btn btn-info">
                                                                        <i class="fas fa-chart-bar"></i> Statistiche
                                                                        Dettagliate
                                                                    </a>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="col-md-6">
                                                            <div class="card">
                                                                <div class="card-header">
                                                                    <h6><i class="fas fa-navigation"></i> Navigazione
                                                                    </h6>
                                                                </div>
                                                                <div class="card-body">
                                                                    <a href="<%= request.getContextPath() %>/jsp/home.jsp"
                                                                        class="btn btn-secondary mb-2">
                                                                        <i class="fas fa-home"></i> Vai alla Home
                                                                    </a><br>
                                                                    <a href="<%= request.getContextPath() %>/jsp/agenda.jsp"
                                                                        class="btn btn-primary mb-2">
                                                                        <i class="fas fa-calendar"></i> Visualizza
                                                                        Agenda
                                                                    </a><br>
                                                                    <a href="<%= request.getContextPath() %>/jsp/inviti.jsp"
                                                                        class="btn btn-success">
                                                                        <i class="fas fa-envelope"></i> Gestisci Inviti
                                                                    </a>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>

                                                    <% } else if ("users".equals(currentSection)) { %>
                                                        <!-- Gestione Utenti -->
                                                        <div class="card">
                                                            <div
                                                                class="card-header d-flex justify-content-between align-items-center">
                                                                <h5><i class="fas fa-users-cog"></i> Gestione Utenti
                                                                </h5>
                                                                <button class="btn btn-success btn-sm"
                                                                    data-bs-toggle="modal"
                                                                    data-bs-target="#createUserModal">
                                                                    <i class="fas fa-plus"></i> Nuovo Utente
                                                                </button>
                                                            </div>
                                                            <div class="card-body">
                                                                <!-- Ricerca utenti -->
                                                                <form method="GET"
                                                                    action="<%= request.getContextPath() %>/admin"
                                                                    class="mb-3">
                                                                    <input type="hidden" name="action"
                                                                        value="searchUsers">
                                                                    <div class="input-group">
                                                                        <input type="text" class="form-control"
                                                                            name="termine"
                                                                            placeholder="Cerca per username o email..."
                                                                            value="<%= request.getAttribute("
                                                                            searchTerm") !=null ?
                                                                            request.getAttribute("searchTerm") : "" %>">
                                                                        <button class="btn btn-outline-secondary"
                                                                            type="submit">
                                                                            <i class="fas fa-search"></i>
                                                                        </button>
                                                                    </div>
                                                                </form>

                                                                <div class="table-responsive">
                                                                    <table class="table table-striped">
                                                                        <thead>
                                                                            <tr>
                                                                                <th>ID</th>
                                                                                <th>Username</th>
                                                                                <th>Email</th>
                                                                                <th>Ruolo</th>
                                                                                <th>Stato</th>
                                                                                <th>Registrazione</th>
                                                                                <th>Ultimo Accesso</th>
                                                                                <th>Azioni</th>
                                                                            </tr>
                                                                        </thead>
                                                                        <tbody>
                                                                            <% for (Utente u : utenti) { %>
                                                                                <tr>
                                                                                    <td>
                                                                                        <%= u.getId() %>
                                                                                    </td>
                                                                                    <td>
                                                                                        <%= u.getUsername() %>
                                                                                    </td>
                                                                                    <td>
                                                                                        <%= u.getEmail() %>
                                                                                    </td>
                                                                                    <td>
                                                                                        <% if (u.isAdmin()) { %>
                                                                                            <span
                                                                                                class="badge bg-danger">Admin</span>
                                                                                            <% } else if (u.isUtente())
                                                                                                { %>
                                                                                                <span
                                                                                                    class="badge bg-success">Utente</span>
                                                                                                <% } else { %>
                                                                                                    <span
                                                                                                        class="badge bg-warning">Ospite</span>
                                                                                                    <% } %>
                                                                                    </td>
                                                                                    <td>
                                                                                        <% if (u.isAttivo()) { %>
                                                                                            <span
                                                                                                class="badge bg-success status-badge">Attivo</span>
                                                                                            <% } else { %>
                                                                                                <span
                                                                                                    class="badge bg-danger status-badge">Inattivo</span>
                                                                                                <% } %>
                                                                                    </td>
                                                                                    <td>
                                                                                        <% if (u.getDataRegistrazione()
                                                                                            !=null) { %>
                                                                                            <%= new
                                                                                                java.text.SimpleDateFormat("dd/MM/yyyy").format(u.getDataRegistrazione())
                                                                                                %>
                                                                                                <% } else { %>
                                                                                                    -
                                                                                                    <% } %>
                                                                                    </td>
                                                                                    <td>
                                                                                        <% if (u.getUltimoAccesso()
                                                                                            !=null) { %>
                                                                                            <%= new
                                                                                                java.text.SimpleDateFormat("dd/MM/yyyy
                                                                                                HH:mm").format(u.getUltimoAccesso())
                                                                                                %>
                                                                                                <% } else { %>
                                                                                                    Mai
                                                                                                    <% } %>
                                                                                    </td>
                                                                                    <td class="action-buttons">
                                                                                        <% if (!u.isAdmin() || u.getId()
                                                                                            !=utente.getId()) { %>
                                                                                            <div class="btn-group"
                                                                                                role="group">
                                                                                                <% if (u.isOspite()) {
                                                                                                    %>
                                                                                                    <a href="<%= request.getContextPath() %>/admin?action=activateUser&id=<%= u.getId() %>"
                                                                                                        class="btn btn-sm btn-success"
                                                                                                        title="Attiva utente">
                                                                                                        <i
                                                                                                            class="fas fa-check"></i>
                                                                                                    </a>
                                                                                                    <% } %>
                                                                                                        <% if
                                                                                                            (u.isAttivo())
                                                                                                            { %>
                                                                                                            <a href="<%= request.getContextPath() %>/admin?action=blockUser&id=<%= u.getId() %>"
                                                                                                                class="btn btn-sm btn-warning"
                                                                                                                title="Blocca utente"
                                                                                                                onclick="return confirm('Sei sicuro di voler bloccare questo utente?')">
                                                                                                                <i
                                                                                                                    class="fas fa-ban"></i>
                                                                                                            </a>
                                                                                                            <% } else {
                                                                                                                %>
                                                                                                                <a href="<%= request.getContextPath() %>/admin?action=unblockUser&id=<%= u.getId() %>"
                                                                                                                    class="btn btn-sm btn-success"
                                                                                                                    title="Sblocca utente">
                                                                                                                    <i
                                                                                                                        class="fas fa-unlock"></i>
                                                                                                                </a>
                                                                                                                <% } %>
                                                                                                                    <button
                                                                                                                        class="btn btn-sm btn-info"
                                                                                                                        title="Modifica utente"
                                                                                                                        onclick="editUser(<%= u.getId() %>, '<%= u.getUsername() %>', '<%= u.getEmail() %>', '<%= u.getRuolo() %>')">
                                                                                                                        <i
                                                                                                                            class="fas fa-edit"></i>
                                                                                                                    </button>
                                                                                                                    <button
                                                                                                                        class="btn btn-sm btn-secondary"
                                                                                                                        title="Reset password"
                                                                                                                        onclick="resetPassword(<%= u.getId() %>, '<%= u.getUsername() %>')">
                                                                                                                        <i
                                                                                                                            class="fas fa-key"></i>
                                                                                                                    </button>
                                                                                                                    <% if
                                                                                                                        (u.getId()
                                                                                                                        !=utente.getId())
                                                                                                                        {
                                                                                                                        %>
                                                                                                                        <a href="<%= request.getContextPath() %>/admin?action=deleteUser&id=<%= u.getId() %>"
                                                                                                                            class="btn btn-sm btn-danger"
                                                                                                                            title="Elimina utente"
                                                                                                                            onclick="return confirm('Sei sicuro di voler eliminare questo utente? Questa azione è irreversibile.')">
                                                                                                                            <i
                                                                                                                                class="fas fa-trash"></i>
                                                                                                                        </a>
                                                                                                                        <% }
                                                                                                                            %>
                                                                                            </div>
                                                                                            <% } %>
                                                                                    </td>
                                                                                </tr>
                                                                                <% } %>
                                                                        </tbody>
                                                                    </table>
                                                                </div>
                                                            </div>
                                                        </div>

                                                        <% } else if ("appointments".equals(currentSection)) { %>
                                                            <!-- Monitoraggio Appuntamenti Condivisi -->
                                                            <div class="card">
                                                                <div class="card-header">
                                                                    <h5><i class="fas fa-calendar-check"></i>
                                                                        Monitoraggio Appuntamenti Condivisi</h5>
                                                                </div>
                                                                <div class="card-body">
                                                                    <div class="table-responsive">
                                                                        <table class="table table-striped">
                                                                            <thead>
                                                                                <tr>
                                                                                    <th>ID</th>
                                                                                    <th>Titolo</th>
                                                                                    <th>Descrizione</th>
                                                                                    <th>Data</th>
                                                                                    <th>Utente</th>
                                                                                    <th>Condiviso</th>
                                                                                    <th>Data Creazione</th>
                                                                                </tr>
                                                                            </thead>
                                                                            <tbody>
                                                                                <% if (appuntamentiCondivisi !=null) {
                                                                                    %>
                                                                                    <% for (Appuntamento a :
                                                                                        appuntamentiCondivisi) { %>
                                                                                        <tr>
                                                                                            <td>
                                                                                                <%= a.getId() %>
                                                                                            </td>
                                                                                            <td>
                                                                                                <%= a.getTitolo() %>
                                                                                            </td>
                                                                                            <td>
                                                                                                <%= a.getDescrizione()
                                                                                                    !=null ?
                                                                                                    a.getDescrizione()
                                                                                                    : "-" %>
                                                                                            </td>
                                                                                            <td>
                                                                                                <%= new
                                                                                                    java.text.SimpleDateFormat("dd/MM/yyyy
                                                                                                    HH:mm").format(a.getDataOra())
                                                                                                    %>
                                                                                            </td>
                                                                                            <td>
                                                                                                <%= a.getUsername()
                                                                                                    !=null ?
                                                                                                    a.getUsername()
                                                                                                    : "N/A" %>
                                                                                            </td>
                                                                                            <td>
                                                                                                <% if (a.isCondiviso())
                                                                                                    { %>
                                                                                                    <span
                                                                                                        class="badge bg-success">Sì</span>
                                                                                                    <% } else { %>
                                                                                                        <span
                                                                                                            class="badge bg-secondary">No</span>
                                                                                                        <% } %>
                                                                                            </td>
                                                                                            <td>
                                                                                                <% if
                                                                                                    (a.getDataCreazione()
                                                                                                    !=null) { %>
                                                                                                    <%= new
                                                                                                        java.text.SimpleDateFormat("dd/MM/yyyy").format(a.getDataCreazione())
                                                                                                        %>
                                                                                                        <% } else { %>
                                                                                                            -
                                                                                                            <% } %>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <% } %>
                                                                                            <% } %>
                                                                            </tbody>
                                                                        </table>
                                                                    </div>
                                                                </div>
                                                            </div>

                                                            <% } else if ("notifications".equals(currentSection)) { %>
                                                                <!-- Gestione Notifiche di Sistema -->
                                                                <div class="card">
                                                                    <div
                                                                        class="card-header d-flex justify-content-between align-items-center">
                                                                        <h5><i class="fas fa-bell"></i> Gestione
                                                                            Notifiche di Sistema</h5>
                                                                        <button class="btn btn-primary btn-sm"
                                                                            data-bs-toggle="modal"
                                                                            data-bs-target="#sendNotificationModal">
                                                                            <i class="fas fa-paper-plane"></i> Invia
                                                                            Notifica
                                                                        </button>
                                                                    </div>
                                                                    <div class="card-body">
                                                                        <div class="table-responsive">
                                                                            <table class="table table-striped">
                                                                                <thead>
                                                                                    <tr>
                                                                                        <th>ID</th>
                                                                                        <th>Titolo</th>
                                                                                        <th>Messaggio</th>
                                                                                        <th>Tipo</th>
                                                                                        <th>Utente</th>
                                                                                        <th>Letta</th>
                                                                                        <th>Data Creazione</th>
                                                                                        <th>Scadenza</th>
                                                                                    </tr>
                                                                                </thead>
                                                                                <tbody>
                                                                                    <% if (notifiche !=null) { %>
                                                                                        <% for (Notifica n : notifiche)
                                                                                            { %>
                                                                                            <tr>
                                                                                                <td>
                                                                                                    <%= n.getId() %>
                                                                                                </td>
                                                                                                <td>
                                                                                                    <%= n.getTitolo() %>
                                                                                                </td>
                                                                                                <td>
                                                                                                    <%= n.getMessaggio()
                                                                                                        %>
                                                                                                </td>
                                                                                                <td>
                                                                                                    <% if
                                                                                                        ("info".equals(n.getTipo()))
                                                                                                        { %>
                                                                                                        <span
                                                                                                            class="badge bg-info">Info</span>
                                                                                                        <% } else if
                                                                                                            ("warning".equals(n.getTipo()))
                                                                                                            { %>
                                                                                                            <span
                                                                                                                class="badge bg-warning">Warning</span>
                                                                                                            <% } else if
                                                                                                                ("error".equals(n.getTipo()))
                                                                                                                { %>
                                                                                                                <span
                                                                                                                    class="badge bg-danger">Error</span>
                                                                                                                <% } else
                                                                                                                    { %>
                                                                                                                    <span
                                                                                                                        class="badge bg-secondary">
                                                                                                                        <%= n.getTipo()
                                                                                                                            %>
                                                                                                                    </span>
                                                                                                                    <% }
                                                                                                                        %>
                                                                                                </td>
                                                                                                <td>
                                                                                                    <%= n.getIdUtente()
                                                                                                        %>
                                                                                                </td>
                                                                                                <td>
                                                                                                    <% if (n.isLetta())
                                                                                                        { %>
                                                                                                        <span
                                                                                                            class="badge bg-success">Sì</span>
                                                                                                        <% } else { %>
                                                                                                            <span
                                                                                                                class="badge bg-warning">No</span>
                                                                                                            <% } %>
                                                                                                </td>
                                                                                                <td>
                                                                                                    <% if
                                                                                                        (n.getDataCreazione()
                                                                                                        !=null) { %>
                                                                                                        <%= new
                                                                                                            java.text.SimpleDateFormat("dd/MM/yyyy
                                                                                                            HH:mm").format(n.getDataCreazione())
                                                                                                            %>
                                                                                                            <% } else {
                                                                                                                %>
                                                                                                                -
                                                                                                                <% } %>
                                                                                                </td>
                                                                                                <td>
                                                                                                    <% if
                                                                                                        (n.getDataScadenza()
                                                                                                        !=null) { %>
                                                                                                        <%= new
                                                                                                            java.text.SimpleDateFormat("dd/MM/yyyy").format(n.getDataScadenza())
                                                                                                            %>
                                                                                                            <% } else {
                                                                                                                %>
                                                                                                                -
                                                                                                                <% } %>
                                                                                                </td>
                                                                                            </tr>
                                                                                            <% } %>
                                                                                                <% } %>
                                                                                </tbody>
                                                                            </table>
                                                                        </div>
                                                                    </div>
                                                                </div>

                                                                <% } else if ("stats".equals(currentSection)) { %>
                                                                    <!-- Statistiche Dettagliate -->
                                                                    <div class="row">
                                                                        <div class="col-md-6">
                                                                            <div class="card">
                                                                                <div class="card-header">
                                                                                    <h6><i class="fas fa-users"></i>
                                                                                        Statistiche Utenti</h6>
                                                                                </div>
                                                                                <div class="card-body">
                                                                                    <ul
                                                                                        class="list-group list-group-flush">
                                                                                        <li
                                                                                            class="list-group-item d-flex justify-content-between">
                                                                                            <span>Utenti Totali:</span>
                                                                                            <span
                                                                                                class="badge bg-primary">
                                                                                                <%= totalUtenti %>
                                                                                            </span>
                                                                                        </li>
                                                                                        <li
                                                                                            class="list-group-item d-flex justify-content-between">
                                                                                            <span>Utenti Attivi:</span>
                                                                                            <span
                                                                                                class="badge bg-success">
                                                                                                <%= utentiAttivi %>
                                                                                            </span>
                                                                                        </li>
                                                                                        <li
                                                                                            class="list-group-item d-flex justify-content-between">
                                                                                            <span>Utenti
                                                                                                Inattivi:</span>
                                                                                            <span
                                                                                                class="badge bg-warning">
                                                                                                <%= utentiInattivi %>
                                                                                            </span>
                                                                                        </li>
                                                                                        <li
                                                                                            class="list-group-item d-flex justify-content-between">
                                                                                            <span>Amministratori:</span>
                                                                                            <span
                                                                                                class="badge bg-danger">
                                                                                                <%= adminCount %>
                                                                                            </span>
                                                                                        </li>
                                                                                    </ul>
                                                                                </div>
                                                                            </div>
                                                                        </div>
                                                                        <div class="col-md-6">
                                                                            <div class="card">
                                                                                <div class="card-header">
                                                                                    <h6><i class="fas fa-calendar"></i>
                                                                                        Statistiche Appuntamenti</h6>
                                                                                </div>
                                                                                <div class="card-body">
                                                                                    <% if
                                                                                        (request.getAttribute("appuntamentiCondivisi")
                                                                                        !=null) { %>
                                                                                        <% List<Appuntamento> apps =
                                                                                            (List<Appuntamento>)
                                                                                                request.getAttribute("appuntamentiCondivisi");
                                                                                                %>
                                                                                                <ul
                                                                                                    class="list-group list-group-flush">
                                                                                                    <li
                                                                                                        class="list-group-item d-flex justify-content-between">
                                                                                                        <span>Appuntamenti
                                                                                                            Condivisi:</span>
                                                                                                        <span
                                                                                                            class="badge bg-info">
                                                                                                            <%= apps.size()
                                                                                                                %>
                                                                                                        </span>
                                                                                                    </li>
                                                                                                </ul>
                                                                                                <% } %>
                                                                                </div>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                    <% } %>
                            </div>

                            <!-- Modal per creazione utente -->
                            <div class="modal fade" id="createUserModal" tabindex="-1">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title">Crea Nuovo Utente</h5>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                        </div>
                                        <form method="POST" action="<%= request.getContextPath() %>/admin">
                                            <input type="hidden" name="action" value="createUser">
                                            <div class="modal-body">
                                                <div class="mb-3">
                                                    <label for="username" class="form-label">Username</label>
                                                    <input type="text" class="form-control" id="username"
                                                        name="username" required>
                                                </div>
                                                <div class="mb-3">
                                                    <label for="email" class="form-label">Email</label>
                                                    <input type="email" class="form-control" id="email" name="email"
                                                        required>
                                                </div>
                                                <div class="mb-3">
                                                    <label for="password" class="form-label">Password</label>
                                                    <input type="password" class="form-control" id="password"
                                                        name="password" required>
                                                </div>
                                                <div class="mb-3">
                                                    <label for="ruolo" class="form-label">Ruolo</label>
                                                    <select class="form-select" id="ruolo" name="ruolo" required>
                                                        <option value="utente">Utente</option>
                                                        <option value="admin">Amministratore</option>
                                                        <option value="ospite">Ospite</option>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-secondary"
                                                    data-bs-dismiss="modal">Annulla</button>
                                                <button type="submit" class="btn btn-primary">Crea Utente</button>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>

                            <!-- Modal per invio notifiche -->
                            <div class="modal fade" id="sendNotificationModal" tabindex="-1">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title">Invia Notifica di Sistema</h5>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                        </div>
                                        <form method="POST" action="<%= request.getContextPath() %>/admin">
                                            <input type="hidden" name="action" value="sendSystemNotification">
                                            <div class="modal-body">
                                                <div class="mb-3">
                                                    <label for="titolo" class="form-label">Titolo</label>
                                                    <input type="text" class="form-control" id="titolo" name="titolo"
                                                        required>
                                                </div>
                                                <div class="mb-3">
                                                    <label for="messaggio" class="form-label">Messaggio</label>
                                                    <textarea class="form-control" id="messaggio" name="messaggio"
                                                        rows="3" required></textarea>
                                                </div>
                                                <div class="mb-3">
                                                    <label for="tipo" class="form-label">Tipo</label>
                                                    <select class="form-select" id="tipo" name="tipo" required>
                                                        <option value="info">Informazione</option>
                                                        <option value="warning">Avviso</option>
                                                        <option value="error">Errore</option>
                                                        <option value="success">Successo</option>
                                                    </select>
                                                </div>
                                                <div class="mb-3">
                                                    <label for="targetUsers" class="form-label">Destinatari</label>
                                                    <select class="form-select" id="targetUsers" name="targetUsers"
                                                        required>
                                                        <option value="all">Tutti gli utenti</option>
                                                        <option value="active">Solo utenti attivi</option>
                                                        <option value="inactive">Solo utenti inattivi</option>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-secondary"
                                                    data-bs-dismiss="modal">Annulla</button>
                                                <button type="submit" class="btn btn-primary">Invia Notifica</button>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>

                            <!-- Modal per modifica utente -->
                            <div class="modal fade" id="editUserModal" tabindex="-1">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title">Modifica Utente</h5>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                        </div>
                                        <form method="POST" action="<%= request.getContextPath() %>/admin">
                                            <input type="hidden" name="action" value="updateUser">
                                            <input type="hidden" id="editUserId" name="id">
                                            <div class="modal-body">
                                                <div class="mb-3">
                                                    <label for="editUsername" class="form-label">Username</label>
                                                    <input type="text" class="form-control" id="editUsername"
                                                        name="username" required>
                                                </div>
                                                <div class="mb-3">
                                                    <label for="editEmail" class="form-label">Email</label>
                                                    <input type="email" class="form-control" id="editEmail" name="email"
                                                        required>
                                                </div>
                                                <div class="mb-3">
                                                    <label for="editRuolo" class="form-label">Ruolo</label>
                                                    <select class="form-select" id="editRuolo" name="ruolo" required>
                                                        <option value="utente">Utente</option>
                                                        <option value="admin">Amministratore</option>
                                                        <option value="ospite">Ospite</option>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-secondary"
                                                    data-bs-dismiss="modal">Annulla</button>
                                                <button type="submit" class="btn btn-primary">Aggiorna</button>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>

                            <!-- Modal per reset password -->
                            <div class="modal fade" id="resetPasswordModal" tabindex="-1">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title">Reset Password</h5>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                        </div>
                                        <form method="POST" action="<%= request.getContextPath() %>/admin">
                                            <input type="hidden" name="action" value="resetPassword">
                                            <input type="hidden" id="resetUserId" name="id">
                                            <div class="modal-body">
                                                <p>Reset password per l'utente: <strong id="resetUsername"></strong></p>
                                                <div class="mb-3">
                                                    <label for="newPassword" class="form-label">Nuova Password</label>
                                                    <input type="password" class="form-control" id="newPassword"
                                                        name="newPassword" required>
                                                </div>
                                            </div>
                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-secondary"
                                                    data-bs-dismiss="modal">Annulla</button>
                                                <button type="submit" class="btn btn-primary">Reset Password</button>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>

                            <script
                                src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
                            <script>
                                function editUser(id, username, email, ruolo) {
                                    document.getElementById('editUserId').value = id;
                                    document.getElementById('editUsername').value = username;
                                    document.getElementById('editEmail').value = email;
                                    document.getElementById('editRuolo').value = ruolo;
                                    new bootstrap.Modal(document.getElementById('editUserModal')).show();
                                }

                                function resetPassword(id, username) {
                                    document.getElementById('resetUserId').value = id;
                                    document.getElementById('resetUsername').textContent = username;
                                    new bootstrap.Modal(document.getElementById('resetPasswordModal')).show();
                                }
                            </script>
                        </body>

                        </html>
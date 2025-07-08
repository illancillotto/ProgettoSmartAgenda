<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ page import="java.util.*, model.Utente, dao.UtenteDAO" %>
        <% Utente utente=(Utente) session.getAttribute("utente"); if (utente==null ||
            !utente.getRuolo().equals("admin")) { response.sendRedirect(request.getContextPath() + "/jsp/login.jsp" );
            return; } UtenteDAO utenteDAO=new UtenteDAO(); List<Utente> utenti = utenteDAO.findAll();
            int totalUtenti = utenti.size();
            int utentiAttivi = (int) utenti.stream().filter(u -> u.getRuolo().equals("utente")).count();
            %>
            <!DOCTYPE html>
            <html lang="it">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Pannello Admin - SmartAgenda</title>
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
                <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
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
                    <!-- Statistiche -->
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
                                                <%= totalUtenti - utentiAttivi - 1 %>
                                            </h4>
                                            <p class="mb-0">In Attesa</p>
                                        </div>
                                        <div class="align-self-center">
                                            <i class="fas fa-clock fa-2x"></i>
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
                                            <h4>1</h4>
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

                    <!-- Gestione Utenti -->
                    <div class="card">
                        <div class="card-header">
                            <h5><i class="fas fa-users-cog"></i> Gestione Utenti</h5>
                        </div>
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-striped">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Username</th>
                                            <th>Email</th>
                                            <th>Ruolo</th>
                                            <th>Registrazione</th>
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
                                                    <% if (u.getRuolo().equals("admin")) { %>
                                                        <span class="badge bg-danger">Admin</span>
                                                        <% } else if (u.getRuolo().equals("utente")) { %>
                                                            <span class="badge bg-success">Utente</span>
                                                            <% } else { %>
                                                                <span class="badge bg-warning">Ospite</span>
                                                                <% } %>
                                                </td>
                                                <td>
                                                    <% if (u.getDataRegistrazione() !=null) { %>
                                                        <%= new
                                                            java.text.SimpleDateFormat("dd/MM/yyyy").format(u.getDataRegistrazione())
                                                            %>
                                                            <% } else { %>
                                                                -
                                                                <% } %>
                                                </td>
                                                <td>
                                                    <% if (!u.getRuolo().equals("admin")) { %>
                                                        <% if (u.getRuolo().equals("ospite")) { %>
                                                            <a href="<%= request.getContextPath() %>/admin?action=activate&id=<%= u.getId() %>"
                                                                class="btn btn-sm btn-success" title="Attiva utente">
                                                                <i class="fas fa-check"></i>
                                                            </a>
                                                            <% } %>
                                                                <a href="<%= request.getContextPath() %>/admin?action=delete&id=<%= u.getId() %>"
                                                                    class="btn btn-sm btn-danger" title="Elimina utente"
                                                                    onclick="return confirm('Sei sicuro di voler eliminare questo utente?')">
                                                                    <i class="fas fa-trash"></i>
                                                                </a>
                                                                <% } %>
                                                </td>
                                            </tr>
                                            <% } %>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                    <!-- Azioni Rapide -->
                    <div class="row mt-4">
                        <div class="col-md-6">
                            <div class="card">
                                <div class="card-header">
                                    <h6><i class="fas fa-tools"></i> Azioni Rapide</h6>
                                </div>
                                <div class="card-body">
                                    <a href="<%= request.getContextPath() %>/admin?action=broadcast"
                                        class="btn btn-primary mb-2">
                                        <i class="fas fa-bullhorn"></i> Invia Notifica Globale
                                    </a><br>
                                    <a href="<%= request.getContextPath() %>/admin?action=cleanup"
                                        class="btn btn-warning mb-2">
                                        <i class="fas fa-broom"></i> Pulisci Dati Vecchi
                                    </a><br>
                                    <a href="<%= request.getContextPath() %>/admin?action=stats" class="btn btn-info">
                                        <i class="fas fa-chart-bar"></i> Statistiche Dettagliate
                                    </a>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="card">
                                <div class="card-header">
                                    <h6><i class="fas fa-navigation"></i> Navigazione</h6>
                                </div>
                                <div class="card-body">
                                    <a href="<%= request.getContextPath() %>/jsp/home.jsp"
                                        class="btn btn-secondary mb-2">
                                        <i class="fas fa-home"></i> Vai alla Home
                                    </a><br>
                                    <a href="<%= request.getContextPath() %>/jsp/agenda.jsp"
                                        class="btn btn-primary mb-2">
                                        <i class="fas fa-calendar"></i> Visualizza Agenda
                                    </a><br>
                                    <a href="<%= request.getContextPath() %>/jsp/inviti.jsp" class="btn btn-success">
                                        <i class="fas fa-envelope"></i> Gestisci Inviti
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
            </body>

            </html>
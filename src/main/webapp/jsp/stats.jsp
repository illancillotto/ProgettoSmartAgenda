<%@ page import="java.util.*, model.Utente" %>
    <% Utente utente=(Utente) session.getAttribute("utente"); if (utente==null || !utente.getRuolo().equals("admin")) {
        response.sendRedirect(request.getContextPath() + "/jsp/login.jsp" ); return; } List<Utente> utenti = (List
        <Utente>) request.getAttribute("utenti");
            Map<String, Integer> statistiche = (Map<String, Integer>) request.getAttribute("statistiche");
                    Integer totalAppuntamenti = (Integer) request.getAttribute("totalAppuntamenti");
                    Integer appuntamentiCondivisi = (Integer) request.getAttribute("appuntamentiCondivisi");
                    %>
                    <!DOCTYPE html>
                    <html>

                    <head>
                        <meta charset="UTF-8">
                        <title>Statistiche Appuntamenti - SmartAgenda</title>
                        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
                            rel="stylesheet">
                        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"
                            rel="stylesheet">
                    </head>

                    <body>
                        <div class="container mt-4">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="d-flex justify-content-between align-items-center mb-4">
                                        <h2><i class="fas fa-chart-bar"></i> Statistiche Appuntamenti</h2>
                                        <div>
                                            <a href="<%= request.getContextPath() %>/jsp/home.jsp"
                                                class="btn btn-secondary">
                                                <i class="fas fa-home"></i> Home
                                            </a>
                                            <a href="<%= request.getContextPath() %>/agenda" class="btn btn-secondary">
                                                <i class="fas fa-calendar-alt"></i> Torna all'Agenda
                                            </a>
                                            <a href="<%= request.getContextPath() %>/admin" class="btn btn-primary">
                                                <i class="fas fa-cogs"></i> Pannello Admin
                                            </a>
                                            <a href="<%= request.getContextPath() %>/logout"
                                                class="btn btn-outline-danger">
                                                <i class="fas fa-sign-out-alt"></i> Logout
                                            </a>
                                        </div>
                                    </div>

                                    <!-- Statistiche Generali -->
                                    <div class="row mb-4">
                                        <div class="col-md-3">
                                            <div class="card text-white bg-primary">
                                                <div class="card-body">
                                                    <div class="d-flex justify-content-between">
                                                        <div>
                                                            <h5 class="card-title">Totale Appuntamenti</h5>
                                                            <h2>
                                                                <%= totalAppuntamenti !=null ? totalAppuntamenti : 0 %>
                                                            </h2>
                                                        </div>
                                                        <div class="align-self-center">
                                                            <i class="fas fa-calendar-alt fa-2x"></i>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-3">
                                            <div class="card text-white bg-success">
                                                <div class="card-body">
                                                    <div class="d-flex justify-content-between">
                                                        <div>
                                                            <h5 class="card-title">Appuntamenti Condivisi</h5>
                                                            <h2>
                                                                <%= appuntamentiCondivisi !=null ? appuntamentiCondivisi
                                                                    : 0 %>
                                                            </h2>
                                                        </div>
                                                        <div class="align-self-center">
                                                            <i class="fas fa-share-alt fa-2x"></i>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-3">
                                            <div class="card text-white bg-info">
                                                <div class="card-body">
                                                    <div class="d-flex justify-content-between">
                                                        <div>
                                                            <h5 class="card-title">Utenti Registrati</h5>
                                                            <h2>
                                                                <%= utenti !=null ? utenti.size() : 0 %>
                                                            </h2>
                                                        </div>
                                                        <div class="align-self-center">
                                                            <i class="fas fa-users fa-2x"></i>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-3">
                                            <div class="card text-white bg-warning">
                                                <div class="card-body">
                                                    <div class="d-flex justify-content-between">
                                                        <div>
                                                            <h5 class="card-title">Media per Utente</h5>
                                                            <h2>
                                                                <%= utenti !=null && utenti.size()> 0 &&
                                                                    totalAppuntamenti != null ?
                                                                    String.format("%.1f", (double) totalAppuntamenti /
                                                                    utenti.size()) : "0" %>
                                                            </h2>
                                                        </div>
                                                        <div class="align-self-center">
                                                            <i class="fas fa-calculator fa-2x"></i>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- Statistiche per Utente -->
                                    <div class="card">
                                        <div class="card-header">
                                            <h5><i class="fas fa-user-chart"></i> Appuntamenti per Utente</h5>
                                        </div>
                                        <div class="card-body">
                                            <% if (utenti !=null && !utenti.isEmpty()) { %>
                                                <div class="table-responsive">
                                                    <table class="table table-striped">
                                                        <thead>
                                                            <tr>
                                                                <th><i class="fas fa-user"></i> Username</th>
                                                                <th><i class="fas fa-envelope"></i> Email</th>
                                                                <th><i class="fas fa-user-tag"></i> Ruolo</th>
                                                                <th><i class="fas fa-calendar-check"></i> N°
                                                                    Appuntamenti</th>
                                                                <th><i class="fas fa-calendar-plus"></i> Azioni</th>
                                                            </tr>
                                                        </thead>
                                                        <tbody>
                                                            <% for (Utente u : utenti) { %>
                                                                <tr>
                                                                    <td>
                                                                        <strong>
                                                                            <%= u.getUsername() %>
                                                                        </strong>
                                                                    </td>
                                                                    <td>
                                                                        <%= u.getEmail() %>
                                                                    </td>
                                                                    <td>
                                                                        <% if (u.getRuolo().equals("admin")) { %>
                                                                            <span class="badge bg-danger">Admin</span>
                                                                            <% } else { %>
                                                                                <span
                                                                                    class="badge bg-primary">Utente</span>
                                                                                <% } %>
                                                                    </td>
                                                                    <td>
                                                                        <span class="badge bg-success fs-6">
                                                                            <%= statistiche !=null ?
                                                                                statistiche.get(u.getUsername()) : 0 %>
                                                                        </span>
                                                                    </td>
                                                                    <td>
                                                                        <a href="<%= request.getContextPath() %>/agenda"
                                                                            class="btn btn-sm btn-primary"
                                                                            title="Crea appuntamento per questo utente">
                                                                            <i class="fas fa-calendar-plus"></i>
                                                                        </a>
                                                                    </td>
                                                                </tr>
                                                                <% } %>
                                                        </tbody>
                                                    </table>
                                                </div>
                                                <% } else { %>
                                                    <div class="alert alert-info">
                                                        <i class="fas fa-info-circle"></i> Nessun utente trovato.
                                                    </div>
                                                    <% } %>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <script
                            src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
                    </body>

                    </html>
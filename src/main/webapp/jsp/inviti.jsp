<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ page import="java.util.*, model.Utente, model.Invito, dao.InvitoDAO" %>
        <% Utente utente=(Utente) session.getAttribute("utente"); if (utente==null) {
            response.sendRedirect(request.getContextPath() + "/jsp/login.jsp" ); return; } InvitoDAO invitoDAO=new
            InvitoDAO(); List<Invito>
            invitiRicevuti = invitoDAO.findByDestinatario(utente.getId());
            List<Invito> invitiInviati = invitoDAO.findByMittente(utente.getId());
                %>
                <!DOCTYPE html>
                <html lang="it">

                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Inviti - SmartAgenda</title>
                    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
                        rel="stylesheet">
                    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"
                        rel="stylesheet">
                </head>

                <body>
                    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
                        <div class="container">
                            <a class="navbar-brand" href="<%= request.getContextPath() %>/jsp/home.jsp">
                                <i class="fas fa-envelope"></i> Inviti
                            </a>
                            <div class="navbar-nav ms-auto">
                                <span class="navbar-text me-3">
                                    <i class="fas fa-user"></i>
                                    <%= utente.getUsername() %>
                                </span>
                                <a class="nav-link" href="<%= request.getContextPath() %>/jsp/home.jsp">
                                    <i class="fas fa-home"></i> Home
                                </a>
                                <a class="nav-link" href="<%= request.getContextPath() %>/logout">
                                    <i class="fas fa-sign-out-alt"></i> Logout
                                </a>
                            </div>
                        </div>
                    </nav>

                    <div class="container mt-4">
                        <div class="row">
                            <!-- Inviti Ricevuti -->
                            <div class="col-md-6">
                                <div class="card">
                                    <div class="card-header bg-success text-white">
                                        <h5><i class="fas fa-inbox"></i> Inviti Ricevuti</h5>
                                    </div>
                                    <div class="card-body">
                                        <% if (invitiRicevuti.isEmpty()) { %>
                                            <div class="alert alert-info">
                                                <i class="fas fa-info-circle"></i> Non hai inviti ricevuti.
                                            </div>
                                            <% } else { %>
                                                <% for (Invito invito : invitiRicevuti) { %>
                                                    <div class="card mb-3 border-
													  <%= invito.getStato().equals("accettato") ? "success"
													      : invito.getStato().equals("rifiutato") ? "danger"
													      : "warning"
													  %>">

                                                        <div class="card-body">
                                                            <h6 class="card-title">
                                                                <%= invito.getTitoloAppuntamento() %>
                                                                    <span class="badge bg-
                                                                    	<%= invito.getStato().equals("accettato") ? "success" :
                                                                        (invito.getStato().equals("rifiutato")
                                                                        ? "danger" : "warning" ) %>">
                                                                        <%= invito.getStato().toUpperCase() %>
                                                                    </span>
                                                            </h6>
                                                            <p class="card-text">
                                                                <strong>Da:</strong>
                                                                <%= invito.getUsernameMittente() %><br>
                                                                    <strong>Data:</strong>
                                                                    <%= invito.getDataAppuntamento() !=null ? new
                                                                        java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(invito.getDataAppuntamento())
                                                                        : "N/A" %><br>
                                                                        <% if (invito.getDescrizione() !=null &&
                                                                            !invito.getDescrizione().isEmpty()) { %>
                                                                            <strong>Descrizione:</strong>
                                                                            <%= invito.getDescrizione() %><br>
                                                                                <% } %>
                                                                                    <% if (invito.getMessaggio() !=null
                                                                                        &&
                                                                                        !invito.getMessaggio().isEmpty())
                                                                                        { %>
                                                                                        <strong>Messaggio:</strong>
                                                                                        <%= invito.getMessaggio() %><br>
                                                                                            <% } %>
                                                            </p>
                                                            <% if (invito.getStato().equals("in_attesa")) { %>
                                                                <div class="btn-group" role="group">
                                                                    <a href="<%= request.getContextPath() %>/inviti?action=accept&id=<%= invito.getId() %>"
                                                                        class="btn btn-success btn-sm">
                                                                        <i class="fas fa-check"></i> Accetta
                                                                    </a>
                                                                    <a href="<%= request.getContextPath() %>/inviti?action=reject&id=<%= invito.getId() %>"
                                                                        class="btn btn-danger btn-sm">
                                                                        <i class="fas fa-times"></i> Rifiuta
                                                                    </a>
                                                                </div>
                                                                <% } %>
                                                        </div>
                                                    </div>
                                                    <% } %>
                                                        <% } %>
                                    </div>
                                </div>
                            </div>

                            <!-- Inviti Inviati -->
                            <div class="col-md-6">
                                <div class="card">
                                    <div class="card-header bg-info text-white">
                                        <h5><i class="fas fa-paper-plane"></i> Inviti Inviati</h5>
                                    </div>
                                    <div class="card-body">
                                        <% if (invitiInviati.isEmpty()) { %>
                                            <div class="alert alert-info">
                                                <i class="fas fa-info-circle"></i> Non hai inviti inviati.
                                            </div>
                                            <% } else { %>
                                                <% for (Invito invito : invitiInviati) { %>
                                                    <div class="card mb-3 border-<%= invito.getStato().equals("accettato") ? "success" : (invito.getStato().equals("rifiutato")
                                                        ? "danger" : "warning" ) %>">
                                                        <div class="card-body">
                                                            <h6 class="card-title">
                                                                <%= invito.getTitoloAppuntamento() %>
                                                                    <span class="badge bg-<%= invito.getStato().equals("accettato") ? "success" :
                                                                        (invito.getStato().equals("rifiutato")
                                                                        ? "danger" : "warning" ) %>">
                                                                        <%= invito.getStato().toUpperCase() %>
                                                                    </span>
                                                            </h6>
                                                            <p class="card-text">
                                                                <strong>A:</strong>
                                                                <%= invito.getUsernameDestinatario() %><br>
                                                                    <strong>Data:</strong>
                                                                    <%= invito.getDataAppuntamento() !=null ? new
                                                                        java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(invito.getDataAppuntamento())
                                                                        : "N/A" %><br>
                                                                        <% if (invito.getDescrizione() !=null &&
                                                                            !invito.getDescrizione().isEmpty()) { %>
                                                                            <strong>Descrizione:</strong>
                                                                            <%= invito.getDescrizione() %><br>
                                                                                <% } %>
                                                                                    <% if (invito.getMessaggio() !=null
                                                                                        &&
                                                                                        !invito.getMessaggio().isEmpty())
                                                                                        { %>
                                                                                        <strong>Messaggio:</strong>
                                                                                        <%= invito.getMessaggio() %><br>
                                                                                            <% } %>
                                                            </p>
                                                            <% if (invito.getStato().equals("in_attesa")) { %>
                                                                <a href="<%= request.getContextPath() %>/inviti?action=cancel&id=<%= invito.getId() %>"
                                                                    class="btn btn-outline-danger btn-sm"
                                                                    onclick="return confirm('Sei sicuro di voler annullare questo invito?')">
                                                                    <i class="fas fa-times"></i> Annulla
                                                                </a>
                                                                <% } %>
                                                        </div>
                                                    </div>
                                                    <% } %>
                                                        <% } %>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Nuovo Invito -->
                        <div class="row mt-4">
                            <div class="col-md-12">
                                <div class="card">
                                    <div class="card-header bg-primary text-white">
                                        <h5><i class="fas fa-plus"></i> Nuovo Invito</h5>
                                    </div>
                                    <div class="card-body">
                                        <form method="post" action="<%= request.getContextPath() %>/inviti">
                                            <input type="hidden" name="action" value="create">
                                            <div class="row">
                                                <div class="col-md-6">
                                                    <div class="mb-3">
                                                        <label for="username" class="form-label">Username
                                                            Destinatario</label>
                                                        <input type="text" class="form-control" name="username"
                                                            id="username" placeholder="Inserisci username" required>
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="mb-3">
                                                        <label for="titolo" class="form-label">Titolo
                                                            Appuntamento</label>
                                                        <input type="text" class="form-control" name="titolo"
                                                            id="titolo" placeholder="Titolo dell'appuntamento" required>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="col-md-6">
                                                    <div class="mb-3">
                                                        <label for="data" class="form-label">Data</label>
                                                        <input type="date" class="form-control" name="data" id="data"
                                                            required>
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="mb-3">
                                                        <label for="ora" class="form-label">Ora</label>
                                                        <input type="time" class="form-control" name="ora" id="ora"
                                                            required>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="mb-3">
                                                <label for="descrizione" class="form-label">Descrizione</label>
                                                <textarea class="form-control" name="descrizione" id="descrizione"
                                                    rows="3" placeholder="Descrizione dell'appuntamento"></textarea>
                                            </div>
                                            <div class="mb-3">
                                                <label for="messaggio" class="form-label">Messaggio Personale</label>
                                                <textarea class="form-control" name="messaggio" id="messaggio" rows="2"
                                                    placeholder="Messaggio opzionale per il destinatario"></textarea>
                                            </div>
                                            <button type="submit" class="btn btn-primary">
                                                <i class="fas fa-paper-plane"></i> Invia Invito
                                            </button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
                </body>

                </html>
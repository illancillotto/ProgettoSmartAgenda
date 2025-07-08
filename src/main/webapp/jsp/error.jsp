<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
    <%@ page import="java.io.StringWriter, java.io.PrintWriter" %>
		<%
		    // Ottieni informazioni sull'errore
		    String errorMessage = (String) request.getAttribute("javax.servlet.error.message");
		    Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		    String requestURI = (String) request.getAttribute("javax.servlet.error.request_uri");
		    Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
		
		    if (errorMessage == null) errorMessage = "Errore sconosciuto";
		    if (statusCode == null) statusCode = 500;
		    if (requestURI == null) requestURI = "N/A";
		    String errorTitle = "Errore";
		    String errorIcon = "fas fa-exclamation-triangle";
		    String errorColor = "danger";
		    switch (statusCode) {
		        case 404: errorTitle = "Pagina Non Trovata"; errorIcon = "fas fa-search"; errorColor = "warning"; break;
		        case 403: errorTitle = "Accesso Negato"; errorIcon = "fas fa-lock"; errorColor = "danger"; break;
		        case 500: errorTitle = "Errore del Server"; errorIcon = "fas fa-server"; errorColor = "danger"; break;
		    }
		%>
            <!DOCTYPE html>
            <html lang="it">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>
                    <%= errorTitle %> - SmartAgenda
                </title>
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
                <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
                <style>
                    .error-container {
                        min-height: 100vh;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                    }

                    .error-card {
                        background: white;
                        border-radius: 20px;
                        box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
                        padding: 2rem;
                        max-width: 600px;
                        width: 90%;
                    }

                    .error-icon {
                        font-size: 4rem;
                        margin-bottom: 1rem;
                    }

                    .error-code {
                        font-size: 6rem;
                        font-weight: bold;
                        opacity: 0.1;
                        position: absolute;
                        top: 10px;
                        right: 20px;
                    }

                    .position-relative {
                        position: relative;
                    }
                </style>
            </head>

            <body>
                <div class="error-container">
                    <div class="error-card text-center position-relative">
                        <div class="error-code text-<%= errorColor %>">
                            <%= statusCode %>
                        </div>

                        <div class="error-icon text-<%= errorColor %>">
                            <i class="<%= errorIcon %>"></i>
                        </div>

                        <h1 class="h2 mb-3 text-<%= errorColor %>">
                            <%= errorTitle %>
                        </h1>

                        <p class="lead mb-4">
                            <% if (statusCode==404) { %>
                                La pagina che stai cercando non esiste o è stata spostata.
                                <% } else if (statusCode==403) { %>
                                    Non hai i permessi necessari per accedere a questa risorsa.
                                    <% } else if (statusCode==500) { %>
                                        Si è verificato un errore interno del server. Riprova più tardi.
                                        <% } else { %>
                                            Si è verificato un errore imprevisto.
                                            <% } %>
                        </p>

                        <div class="alert alert-light border mb-4">
                            <div class="row">
                                <div class="col-md-4">
                                    <strong>Codice Errore:</strong><br>
                                    <span class="text-<%= errorColor %>">
                                        <%= statusCode %>
                                    </span>
                                </div>
                                <div class="col-md-8">
                                    <strong>Risorsa:</strong><br>
                                    <code><%= requestURI %></code>
                                </div>
                            </div>
                        </div>

                        <div class="d-grid gap-2 d-md-flex justify-content-md-center">
                            <a href="<%= request.getContextPath() %>/" class="btn btn-primary">
                                <i class="fas fa-home"></i> Torna alla Home
                            </a>
                            <a href="<%= request.getContextPath() %>/jsp/login.jsp" class="btn btn-outline-secondary">
                                <i class="fas fa-sign-in-alt"></i> Login
                            </a>
                            <button type="button" class="btn btn-outline-info" onclick="window.history.back()">
                                <i class="fas fa-arrow-left"></i> Indietro
                            </button>
                        </div>

                        <% if (throwable !=null && request.getParameter("debug") !=null) { %>
                            <div class="mt-4">
                                <button class="btn btn-sm btn-outline-danger" type="button" data-bs-toggle="collapse"
                                    data-bs-target="#errorDetails" aria-expanded="false">
                                    <i class="fas fa-bug"></i> Dettagli Tecnici
                                </button>
                                <div class="collapse mt-3" id="errorDetails">
                                    <div class="alert alert-danger text-start">
                                        <h6>Stack Trace:</h6>
                                        <pre class="small"><%
                                StringWriter sw = new StringWriter();
                                PrintWriter pw = new PrintWriter(sw);
                                throwable.printStackTrace(pw);
                                out.print(sw.toString());
                            %></pre>
                                    </div>
                                </div>
                            </div>
                            <% } %>

                                <hr class="my-4">

                                <div class="text-muted">
                                    <small>
                                        <i class="fas fa-calendar-alt"></i> SmartAgenda - Gestione Appuntamenti
                                        Intelligente<br>
                                        Se il problema persiste, contatta l'amministratore del sistema.
                                    </small>
                                </div>
                    </div>
                </div>

                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
                <script>
        // Auto-refresh per errori temporanei
        <% if (statusCode == 500 || statusCode == 503) { %>
                        setTimeout(function () {
                            if (confirm('Vuoi ricaricare la pagina per riprovare?')) {
                                window.location.reload();
                            }
                        }, 10000);
        <% } %>
                </script>
            </body>

            </html>
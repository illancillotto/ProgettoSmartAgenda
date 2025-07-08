<%@ page import="java.util.*, model.Appuntamento, model.Utente, model.Categoria" %>
    <% Utente utente=(Utente) session.getAttribute("utente"); if (utente==null) {
        response.sendRedirect(request.getContextPath() + "/jsp/login.jsp" ); return; } // Usa gli attributi impostati dal servlet invece di fare query dirette 
        	List<Appuntamento> lista = (List<Appuntamento>)
            request.getAttribute("appuntamenti");
            List<Categoria> categorie = (List<Categoria>) request.getAttribute("categorie");
                    Appuntamento appuntamentoEdit = (Appuntamento) request.getAttribute("appuntamento");
                    boolean editMode = request.getAttribute("editMode") != null;

                    // Se non ci sono attributi, significa che siamo arrivati direttamente alla JSP
                    if (lista == null) {
                    response.sendRedirect(request.getContextPath() + "/agenda");
                    return;
                    }
                    %>
                    <!DOCTYPE html>
                    <html>

                    <head>
                        <meta charset="UTF-8">
                        <title>Agenda - SmartAgenda</title>
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
                                        <h2><i class="fas fa-calendar-alt"></i> Agenda di <%= utente.getUsername() %>
                                        </h2>
                                        <div>
                                            <a href="home.jsp" class="btn btn-secondary">
                                                <i class="fas fa-home"></i> Home
                                            </a>
                                            <a href="<%= request.getContextPath() %>/logout"
                                                class="btn btn-outline-danger">
                                                <i class="fas fa-sign-out-alt"></i> Logout
                                            </a>
                                        </div>
                                    </div>

                                    <!-- Messaggi di errore/successo -->
                                    <% String errore=(String) request.getAttribute("errore"); %>
                                        <% String successo=(String) request.getAttribute("successo"); %>
                                            <% if (errore !=null) { %>
                                                <div class="alert alert-danger alert-dismissible fade show"
                                                    role="alert">
                                                    <i class="fas fa-exclamation-circle"></i>
                                                    <%= errore %>
                                                        <button type="button" class="btn-close"
                                                            data-bs-dismiss="alert"></button>
                                                </div>
                                                <% } %>
                                                    <% if (successo !=null) { %>
                                                        <div class="alert alert-success alert-dismissible fade show"
                                                            role="alert">
                                                            <i class="fas fa-check-circle"></i>
                                                            <%= successo %>
                                                                <button type="button" class="btn-close"
                                                                    data-bs-dismiss="alert"></button>
                                                        </div>
                                                        <% } %>

                                                            <!-- Lista Appuntamenti -->
                                                            <div class="card mb-4">
                                                                <div class="card-header">
                                                                    <h5><i class="fas fa-list"></i> I tuoi appuntamenti
                                                                    </h5>
                                                                </div>
                                                                <div class="card-body">
                                                                    <% if (lista.isEmpty()) { %>
                                                                        <div class="alert alert-info">
                                                                            <i class="fas fa-info-circle"></i> Non hai
                                                                            ancora appuntamenti.
                                                                        </div>
                                                                        <% } else { %>
                                                                            <div class="table-responsive">
                                                                                <table class="table table-striped">
                                                                                    <thead>
                                                                                        <tr>
                                                                                            <th><i
                                                                                                    class="fas fa-calendar"></i>
                                                                                                Data e Ora</th>
                                                                                            <th><i
                                                                                                    class="fas fa-heading"></i>
                                                                                                Titolo</th>
                                                                                            <th><i
                                                                                                    class="fas fa-align-left"></i>
                                                                                                Descrizione
                                                                                            </th>
                                                                                            <th><i
                                                                                                    class="fas fa-share-alt"></i>
                                                                                                Condiviso</th>
                                                                                            <th><i
                                                                                                    class="fas fa-cogs"></i>
                                                                                                Azioni</th>
                                                                                        </tr>
                                                                                    </thead>
                                                                                    <tbody>
                                                                                        <% for(Appuntamento a : lista) {
                                                                                            %>
																							<tr class="<%= a.isPassato() ? "table-secondary" : (a.isOggi() ? "table-warning" : "") %>">
                                                                                                <td>
                                                                                                    <%= a.getDataFormatted()
                                                                                                        %>
                                                                                                        <% if
                                                                                                            (a.isOggi())
                                                                                                            { %>
                                                                                                            <span
                                                                                                                class="badge bg-warning">Oggi</span>
                                                                                                            <% } else if
                                                                                                                (a.isDomani())
                                                                                                                { %>
                                                                                                                <span
                                                                                                                    class="badge bg-info">Domani</span>
                                                                                                                <% } else
                                                                                                                    if
                                                                                                                    (a.isPassato())
                                                                                                                    { %>
                                                                                                                    <span
                                                                                                                        class="badge bg-secondary">Passato</span>
                                                                                                                    <% }
                                                                                                                        %>
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
                                                                                                    <% if
                                                                                                        (a.isCondiviso())
                                                                                                        { %>
                                                                                                        <span
                                                                                                            class="badge bg-success">Sì</span>
                                                                                                        <% } else { %>
                                                                                                            <span
                                                                                                                class="badge bg-secondary">No</span>
                                                                                                            <% } %>
                                                                                                </td>
                                                                                                <td>
                                                                                                    <a href="<%= request.getContextPath() %>/agenda?action=edit&id=<%= a.getId() %>"
                                                                                                        class="btn btn-sm btn-primary">
                                                                                                        <i
                                                                                                            class="fas fa-edit"></i>
                                                                                                    </a>
                                                                                                    <a href="<%= request.getContextPath() %>/agenda?action=delete&id=<%= a.getId() %>"
                                                                                                        class="btn btn-sm btn-danger"
                                                                                                        onclick="return confirm('Sei sicuro di voler eliminare questo appuntamento?')">
                                                                                                        <i
                                                                                                            class="fas fa-trash"></i>
                                                                                                    </a>
                                                                                                </td>
                                                                                            </tr>
                                                                                            <% } %>
                                                                                    </tbody>
                                                                                </table>
                                                                            </div>
                                                                            <% } %>
                                                                </div>
                                                            </div>

                                                            <!-- Nuovo/Modifica Appuntamento -->
                                                            <div class="card">
                                                                <div class="card-header">
                                                                    <h5><i class="fas fa-<%= editMode ? " edit" : "plus"
                                                                            %>"></i>
                                                                        <%= editMode ? "Modifica Appuntamento"
                                                                            : "Nuovo Appuntamento" %>
                                                                    </h5>
                                                                </div>
                                                                <div class="card-body">
                                                                    <form method="post"
                                                                        action="<%= request.getContextPath() %>/agenda">
                                                                        <input type="hidden" name="action"
                                                                            value="<%= editMode ? " update" : "create"
                                                                            %>">
                                                                        <% if (editMode) { %>
                                                                            <input type="hidden" name="id"
                                                                                value="<%= appuntamentoEdit.getId() %>">
                                                                            <% } %>
                                                                                <div class="row">
                                                                                    <div class="col-md-6">
                                                                                        <div class="mb-3">
                                                                                            <label for="data"
                                                                                                class="form-label">Data</label>
                                                                                            <input type="date"
                                                                                                class="form-control"
                                                                                                name="data" id="data"
                                                                                                value="<%= editMode ? appuntamentoEdit.getDataForHtmlInput() : "" %>"
                                                                                                required>
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="col-md-6">
                                                                                        <div class="mb-3">
                                                                                            <label for="ora"
                                                                                                class="form-label">Ora</label>
                                                                                            <input type="time"
                                                                                                class="form-control"
                                                                                                name="ora" id="ora"
                                                                                                value="<%= editMode ? appuntamentoEdit.getOraForHtmlInput() : "" %>"
                                                                                                required>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                                <div class="mb-3">
                                                                                    <label for="titolo"
                                                                                        class="form-label">Titolo</label>
                                                                                    <input type="text"
                                                                                        class="form-control"
                                                                                        name="titolo" id="titolo"
                                                                                        value="<%= editMode ? appuntamentoEdit.getTitolo() : "" %>"
                                                                                        required>
                                                                                </div>
                                                                                <div class="mb-3">
                                                                                    <label for="descrizione"
                                                                                        class="form-label">Descrizione</label>
                                                                                    <textarea class="form-control"
                                                                                        name="descrizione"
                                                                                        id="descrizione"
                                                                                        rows="3"><%= editMode && appuntamentoEdit.getDescrizione() != null ? appuntamentoEdit.getDescrizione() : "" %></textarea>
                                                                                </div>
                                                                                <div class="mb-3 form-check">
                                                                                    <input type="checkbox"
                                                                                        class="form-check-input"
                                                                                        name="condiviso" id="condiviso"
                                                                                        <%=editMode &&
                                                                                        appuntamentoEdit.isCondiviso()
                                                                                        ? "checked" : "" %>>
                                                                                    <label class="form-check-label"
                                                                                        for="condiviso">
                                                                                        Condividi questo appuntamento
                                                                                    </label>
                                                                                </div>
                                                                                <% if (categorie !=null &&
                                                                                    !categorie.isEmpty()) { %>
                                                                                    <div class="mb-3">
                                                                                        <label for="categoria"
                                                                                            class="form-label">Categoria</label>
                                                                                        <select class="form-control"
                                                                                            name="categoria"
                                                                                            id="categoria">
                                                                                            <option value="">Nessuna
                                                                                                categoria</option>
                                                                                            <% for (Categoria cat :
                                                                                                categorie) { %>
                                                                                                <option
                                                                                                    value="<%= cat.getId() %>"
                                                                                                    <%=editMode &&
                                                                                                    appuntamentoEdit.getIdCategoria()==cat.getId()
                                                                                                    ? "selected" : "" %>
                                                                                                    >
                                                                                                    <%= cat.getNome() %>
                                                                                                </option>
                                                                                                <% } %>
                                                                                        </select>
                                                                                    </div>
                                                                                    <% } %>
                                                                                        <div class="d-flex gap-2">
                                                                                            <button type="submit"
                                                                                                class="btn btn-primary">
                                                                                                <i class="fas fa-<%= editMode ? "save" : "plus"
                                                                                                    %>"></i>
                                                                                                <%= editMode
                                                                                                    ? "Salva Modifiche"
                                                                                                    : "Aggiungi Appuntamento"
                                                                                                    %>
                                                                                            </button>
                                                                                            <% if (editMode) { %>
                                                                                                <a href="<%= request.getContextPath() %>/agenda"
                                                                                                    class="btn btn-secondary">
                                                                                                    <i
                                                                                                        class="fas fa-times"></i>
                                                                                                    Annulla
                                                                                                </a>
                                                                                                <% } %>
                                                                                        </div>
                                                                    </form>
                                                                </div>
                                                            </div>
                                </div>
                            </div>
                        </div>

                        <script
                            src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
                    </body>

                    </html>
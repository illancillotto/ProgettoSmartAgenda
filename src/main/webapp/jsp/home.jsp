<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <% String username=(String) session.getAttribute("username"); if (username==null) {
        response.sendRedirect(request.getContextPath() + "/jsp/login.jsp" ); return; } %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>Home - SmartAgenda</title>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
            <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        </head>

        <body>
            <div class="container mt-5">
                <div class="row justify-content-center">
                    <div class="col-md-8">
                        <div class="card">
                            <div class="card-header bg-primary text-white">
                                <h3><i class="fas fa-home"></i> SmartAgenda - Home</h3>
                            </div>
                            <div class="card-body">
                                <h4>Benvenuto, <%= username %>!</h4>
                                <p class="text-muted">Gestisci i tuoi appuntamenti in modo intelligente</p>

                                <div class="row mt-4">
                                    <div class="col-md-6">
                                        <div class="card border-primary">
                                            <div class="card-body text-center">
                                                <i class="fas fa-calendar-alt fa-3x text-primary mb-3"></i>
                                                <h5>Agenda</h5>
                                                <p>Visualizza e gestisci i tuoi appuntamenti</p>
                                                <a href="<%= request.getContextPath() %>/jsp/agenda.jsp"
                                                    class="btn btn-primary">
                                                    <i class="fas fa-calendar"></i> Vai all'Agenda
                                                </a>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="card border-success">
                                            <div class="card-body text-center">
                                                <i class="fas fa-user-friends fa-3x text-success mb-3"></i>
                                                <h5>Inviti</h5>
                                                <p>Gestisci inviti e appuntamenti condivisi</p>
                                                <a href="<%= request.getContextPath() %>/jsp/inviti.jsp"
                                                    class="btn btn-success">
                                                    <i class="fas fa-envelope"></i> Gestisci Inviti
                                                </a>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="text-center mt-4">
                                    <a href="<%= request.getContextPath() %>/logout" class="btn btn-outline-danger">
                                        <i class="fas fa-sign-out-alt"></i> Logout
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
        </body>

        </html>
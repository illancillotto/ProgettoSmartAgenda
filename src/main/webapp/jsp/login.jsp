<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <!DOCTYPE html>
  <html lang="it">

  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - SmartAgenda</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
      body {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        min-height: 100vh;
        display: flex;
        align-items: center;
        justify-content: center;
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      }

      .login-container {
        background: white;
        border-radius: 20px;
        box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
        padding: 40px;
        width: 100%;
        max-width: 400px;
      }

      .logo {
        text-align: center;
        margin-bottom: 30px;
      }

      .logo i {
        font-size: 3rem;
        color: #667eea;
        margin-bottom: 10px;
      }

      .logo h2 {
        color: #333;
        font-weight: 600;
        margin: 0;
      }

      .form-group {
        margin-bottom: 20px;
      }

      .form-control {
        border: 2px solid #e9ecef;
        border-radius: 10px;
        padding: 12px 15px;
        font-size: 16px;
        transition: all 0.3s ease;
      }

      .form-control:focus {
        border-color: #667eea;
        box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
      }

      .btn-primary {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        border: none;
        border-radius: 10px;
        padding: 12px 30px;
        font-size: 16px;
        font-weight: 600;
        width: 100%;
        transition: all 0.3s ease;
      }

      .btn-primary:hover {
        transform: translateY(-2px);
        box-shadow: 0 10px 20px rgba(102, 126, 234, 0.3);
      }

      .alert {
        border-radius: 10px;
        border: none;
        margin-bottom: 20px;
      }

      .remember-me {
        display: flex;
        align-items: center;
        margin-bottom: 20px;
      }

      .remember-me input[type="checkbox"] {
        margin-right: 8px;
      }

      .divider {
        text-align: center;
        margin: 20px 0;
        position: relative;
      }

      .divider::before {
        content: '';
        position: absolute;
        top: 50%;
        left: 0;
        right: 0;
        height: 1px;
        background: #e9ecef;
      }

      .divider span {
        background: white;
        padding: 0 15px;
        color: #666;
        font-size: 14px;
      }

      .register-link {
        text-align: center;
        margin-top: 20px;
      }

      .register-link a {
        color: #667eea;
        text-decoration: none;
        font-weight: 600;
      }

      .register-link a:hover {
        text-decoration: underline;
      }

      .demo-section {
        text-align: center;
        margin-top: 20px;
        padding: 15px;
        background: #f8f9fa;
        border-radius: 10px;
      }

      .demo-section h6 {
        color: #333;
        margin-bottom: 10px;
      }

      .demo-credentials {
        font-size: 12px;
        color: #666;
        margin-bottom: 5px;
      }

      .btn-demo {
        background: #28a745;
        border: none;
        border-radius: 8px;
        padding: 8px 16px;
        color: white;
        font-size: 14px;
        margin: 2px;
        cursor: pointer;
        transition: all 0.3s ease;
      }

      .btn-demo:hover {
        background: #218838;
      }
    </style>
  </head>

  <body>
    <div class="login-container">
      <div class="logo">
        <i class="fas fa-calendar-alt"></i>
        <h2>SmartAgenda</h2>
        <p class="text-muted">Gestione Appuntamenti Intelligente</p>
      </div>

      <!-- Messaggi di errore/successo -->
      <% if (request.getAttribute("errore") !=null) { %>
        <div class="alert alert-danger">
          <i class="fas fa-exclamation-triangle"></i>
          <%= request.getAttribute("errore") %>
        </div>
        <% } %>

          <% if ("ok".equals(request.getParameter("registrazione"))) { %>
            <div class="alert alert-success">
              <i class="fas fa-check-circle"></i>
              Registrazione completata! Ora puoi fare il login.
            </div>
            <% } %>

              <!-- Form di login -->
              <form action="<%= request.getContextPath() %>/login" method="post">
                <div class="form-group">
                  <div class="input-group">
                    <span class="input-group-text">
                      <i class="fas fa-user"></i>
                    </span>
                    <input type="text" class="form-control" name="username" placeholder="Username"
                      value="<%= request.getAttribute(" rememberedUsername") !=null ?
                      request.getAttribute("rememberedUsername") : "" %>"
                    required>
                  </div>
                </div>

                <div class="form-group">
                  <div class="input-group">
                    <span class="input-group-text">
                      <i class="fas fa-lock"></i>
                    </span>
                    <input type="password" class="form-control" name="password" placeholder="Password" required>
                  </div>
                </div>

                <div class="remember-me">
                  <input type="checkbox" name="remember" id="remember">
                  <label for="remember">Ricordami</label>
                </div>

                <button type="submit" class="btn btn-primary">
                  <i class="fas fa-sign-in-alt"></i> Accedi
                </button>
              </form>

              <div class="divider">
                <span>oppure</span>
              </div>

              <div class="register-link">
                <p>Non hai un account? <a href="registration.jsp">Registrati qui</a></p>
              </div>

              <!-- Sezione demo -->
              <div class="demo-section">
                <h6><i class="fas fa-eye"></i> Demo</h6>
                <div class="demo-credentials">Admin: admin / admin123</div>
                <div class="demo-credentials">Utenti: tizio, caio, sempronio / (password uguale al nome + 123)</div>
                <button type="button" class="btn-demo" onclick="fillDemo('admin', 'admin123')">
                  Login Admin
                </button>
                <button type="button" class="btn-demo" onclick="fillDemo('tizio', 'tizio123')">
                  Login Tizio
                </button>
                <button type="button" class="btn-demo" onclick="fillDemo('caio', 'caio123')">
                  Login Caio
                </button>
              </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
      function fillDemo(username, password) {
        document.querySelector('input[name="username"]').value = username;
        document.querySelector('input[name="password"]').value = password;
      }

      // Auto-focus sul primo campo
      document.addEventListener('DOMContentLoaded', function () {
        const usernameField = document.querySelector('input[name="username"]');
        if (usernameField.value === '') {
          usernameField.focus();
        } else {
          document.querySelector('input[name="password"]').focus();
        }
      });

      // Gestione Enter per submit
      document.addEventListener('keypress', function (e) {
        if (e.key === 'Enter') {
          document.querySelector('form').submit();
        }
      });
    </script>
  </body>

  </html>
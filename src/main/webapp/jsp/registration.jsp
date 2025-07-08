<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html lang="it">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Registrazione - SmartAgenda</title>
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
                padding: 20px 0;
            }

            .registration-container {
                background: white;
                border-radius: 20px;
                box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
                padding: 40px;
                width: 100%;
                max-width: 500px;
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

            .form-control.is-valid {
                border-color: #28a745;
            }

            .form-control.is-invalid {
                border-color: #dc3545;
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

            .btn-secondary {
                background: #6c757d;
                border: none;
                border-radius: 10px;
                padding: 10px 20px;
                font-size: 14px;
                color: white;
                text-decoration: none;
                display: inline-block;
                transition: all 0.3s ease;
            }

            .btn-secondary:hover {
                background: #5a6268;
                color: white;
                text-decoration: none;
            }

            .alert {
                border-radius: 10px;
                border: none;
                margin-bottom: 20px;
            }

            .password-requirements {
                font-size: 12px;
                color: #666;
                margin-top: 5px;
            }

            .password-requirements ul {
                margin: 5px 0;
                padding-left: 20px;
            }

            .password-requirements li {
                margin-bottom: 2px;
            }

            .password-requirements li.valid {
                color: #28a745;
            }

            .password-requirements li.invalid {
                color: #dc3545;
            }

            .back-link {
                text-align: center;
                margin-top: 20px;
            }

            .input-group-text {
                background: #f8f9fa;
                border: 2px solid #e9ecef;
                border-right: none;
                border-radius: 10px 0 0 10px;
            }

            .input-group .form-control {
                border-left: none;
                border-radius: 0 10px 10px 0;
            }

            .input-group .form-control:focus {
                border-left: none;
            }

            .strength-meter {
                height: 4px;
                background: #e9ecef;
                border-radius: 2px;
                margin-top: 5px;
                overflow: hidden;
            }

            .strength-meter-fill {
                height: 100%;
                transition: all 0.3s ease;
                border-radius: 2px;
            }

            .strength-weak {
                background: #dc3545;
                width: 25%;
            }

            .strength-fair {
                background: #ffc107;
                width: 50%;
            }

            .strength-good {
                background: #fd7e14;
                width: 75%;
            }

            .strength-strong {
                background: #28a745;
                width: 100%;
            }
        </style>
    </head>

    <body>
        <div class="registration-container">
            <div class="logo">
                <i class="fas fa-user-plus"></i>
                <h2>Registrazione</h2>
                <p class="text-muted">Crea il tuo account SmartAgenda</p>
            </div>

            <!-- Messaggi di errore -->
            <% if (request.getAttribute("errore") !=null) { %>
                <div class="alert alert-danger">
                    <i class="fas fa-exclamation-triangle"></i>
                    <%= request.getAttribute("errore") %>
                </div>
                <% } %>

                    <!-- Form di registrazione -->
                    <form action="<%= request.getContextPath() %>/register" method="post" id="registrationForm"
                        novalidate>
                        <div class="form-group">
                            <div class="input-group">
                                <span class="input-group-text">
                                    <i class="fas fa-user"></i>
                                </span>
                                <input type="text" class="form-control" name="username" id="username"
                                    placeholder="Username (min 3 caratteri)" value="<%= request.getAttribute("
                                    username") !=null ? request.getAttribute("username") : "" %>"
                                required>
                            </div>
                            <div class="invalid-feedback"></div>
                        </div>

                        <div class="form-group">
                            <div class="input-group">
                                <span class="input-group-text">
                                    <i class="fas fa-envelope"></i>
                                </span>
                                <input type="email" class="form-control" name="email" id="email" placeholder="Email"
                                    value="<%= request.getAttribute(" email") !=null ? request.getAttribute("email")
                                    : "" %>"
                                required>
                            </div>
                            <div class="invalid-feedback"></div>
                        </div>

                        <div class="form-group">
                            <div class="input-group">
                                <span class="input-group-text">
                                    <i class="fas fa-lock"></i>
                                </span>
                                <input type="password" class="form-control" name="password" id="password"
                                    placeholder="Password (min 6 caratteri)" required>
                            </div>
                            <div class="strength-meter">
                                <div class="strength-meter-fill" id="strengthMeter"></div>
                            </div>
                            <div class="password-requirements">
                                <ul id="passwordRequirements">
                                    <li id="req-length">Almeno 6 caratteri</li>
                                    <li id="req-letter">Almeno una lettera</li>
                                    <li id="req-number">Almeno un numero</li>
                                </ul>
                            </div>
                            <div class="invalid-feedback"></div>
                        </div>

                        <div class="form-group">
                            <div class="input-group">
                                <span class="input-group-text">
                                    <i class="fas fa-lock"></i>
                                </span>
                                <input type="password" class="form-control" name="confirmPassword" id="confirmPassword"
                                    placeholder="Conferma Password" required>
                            </div>
                            <div class="invalid-feedback"></div>
                        </div>

                        <div class="form-group">
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" id="terms" required>
                                <label class="form-check-label" for="terms">
                                    Accetto i <a href="#" onclick="showTerms()">termini e condizioni</a>
                                </label>
                                <div class="invalid-feedback">
                                    Devi accettare i termini e condizioni
                                </div>
                            </div>
                        </div>

                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-user-plus"></i> Registrati
                        </button>
                    </form>

                    <div class="back-link">
                        <a href="login.jsp" class="btn-secondary">
                            <i class="fas fa-arrow-left"></i> Torna al Login
                        </a>
                    </div>
        </div>

        <!-- Modal per termini e condizioni -->
        <div class="modal fade" id="termsModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Termini e Condizioni</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <h6>1. Accettazione dei Termini</h6>
                        <p>Utilizzando SmartAgenda, accetti questi termini e condizioni.</p>

                        <h6>2. Utilizzo del Servizio</h6>
                        <p>Il servizio è fornito per la gestione personale degli appuntamenti.</p>

                        <h6>3. Privacy</h6>
                        <p>I tuoi dati personali sono protetti secondo le normative vigenti.</p>

                        <h6>4. Responsabilità</h6>
                        <p>L'utente è responsabile delle informazioni inserite nel sistema.</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary" data-bs-dismiss="modal">Ho capito</button>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            // Validazione in tempo reale
            document.addEventListener('DOMContentLoaded', function () {
                const form = document.getElementById('registrationForm');
                const username = document.getElementById('username');
                const email = document.getElementById('email');
                const password = document.getElementById('password');
                const confirmPassword = document.getElementById('confirmPassword');
                const terms = document.getElementById('terms');

                // Validazione username
                username.addEventListener('input', function () {
                    const value = this.value.trim();
                    if (value.length < 3) {
                        setInvalid(this, 'Username deve essere di almeno 3 caratteri');
                    } else if (!/^[a-zA-Z0-9_]+$/.test(value)) {
                        setInvalid(this, 'Username può contenere solo lettere, numeri e underscore');
                    } else {
                        setValid(this);
                    }
                });

                // Validazione email
                email.addEventListener('input', function () {
                    const value = this.value.trim();
                    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                    if (!emailRegex.test(value)) {
                        setInvalid(this, 'Inserisci un indirizzo email valido');
                    } else {
                        setValid(this);
                    }
                });

                // Validazione password con indicatore forza
                password.addEventListener('input', function () {
                    const value = this.value;
                    validatePassword(value);
                    updatePasswordStrength(value);

                    // Rivalidata conferma password
                    if (confirmPassword.value) {
                        validateConfirmPassword();
                    }
                });

                // Validazione conferma password
                confirmPassword.addEventListener('input', validateConfirmPassword);

                function validatePassword(password) {
                    const requirements = {
                        length: password.length >= 6,
                        letter: /[a-zA-Z]/.test(password),
                        number: /\d/.test(password)
                    };

                    // Aggiorna indicatori requisiti
                    updateRequirement('req-length', requirements.length);
                    updateRequirement('req-letter', requirements.letter);
                    updateRequirement('req-number', requirements.number);

                    const isValid = Object.values(requirements).every(req => req);
                    if (isValid) {
                        setValid(document.getElementById('password'));
                    } else {
                        setInvalid(document.getElementById('password'), 'Password non soddisfa i requisiti');
                    }

                    return isValid;
                }

                function updateRequirement(id, isValid) {
                    const element = document.getElementById(id);
                    element.className = isValid ? 'valid' : 'invalid';
                }

                function updatePasswordStrength(password) {
                    const meter = document.getElementById('strengthMeter');
                    let strength = 0;

                    if (password.length >= 6) strength++;
                    if (/[a-z]/.test(password)) strength++;
                    if (/[A-Z]/.test(password)) strength++;
                    if (/\d/.test(password)) strength++;
                    if (/[^a-zA-Z\d]/.test(password)) strength++;

                    meter.className = 'strength-meter-fill';
                    if (strength <= 2) meter.classList.add('strength-weak');
                    else if (strength <= 3) meter.classList.add('strength-fair');
                    else if (strength <= 4) meter.classList.add('strength-good');
                    else meter.classList.add('strength-strong');
                }

                function validateConfirmPassword() {
                    const password = document.getElementById('password').value;
                    const confirmPassword = document.getElementById('confirmPassword').value;

                    if (confirmPassword !== password) {
                        setInvalid(document.getElementById('confirmPassword'), 'Le password non corrispondono');
                    } else if (confirmPassword.length > 0) {
                        setValid(document.getElementById('confirmPassword'));
                    }
                }

                function setValid(element) {
                    element.classList.remove('is-invalid');
                    element.classList.add('is-valid');
                    const feedback = element.parentNode.nextElementSibling;
                    if (feedback && feedback.classList.contains('invalid-feedback')) {
                        feedback.style.display = 'none';
                    }
                }

                function setInvalid(element, message) {
                    element.classList.remove('is-valid');
                    element.classList.add('is-invalid');
                    const feedback = element.parentNode.nextElementSibling;
                    if (feedback && feedback.classList.contains('invalid-feedback')) {
                        feedback.textContent = message;
                        feedback.style.display = 'block';
                    }
                }

                // Validazione form al submit
                form.addEventListener('submit', function (e) {
                    e.preventDefault();

                    let isValid = true;

                    // Valida tutti i campi
                    if (username.value.trim().length < 3) {
                        setInvalid(username, 'Username deve essere di almeno 3 caratteri');
                        isValid = false;
                    }

                    if (!email.value.match(/^[^\s@]+@[^\s@]+\.[^\s@]+$/)) {
                        setInvalid(email, 'Inserisci un indirizzo email valido');
                        isValid = false;
                    }

                    if (!validatePassword(password.value)) {
                        isValid = false;
                    }

                    if (password.value !== confirmPassword.value) {
                        setInvalid(confirmPassword, 'Le password non corrispondono');
                        isValid = false;
                    }

                    if (!terms.checked) {
                        terms.classList.add('is-invalid');
                        isValid = false;
                    } else {
                        terms.classList.remove('is-invalid');
                    }

                    if (isValid) {
                        this.submit();
                    }
                });
            });

            function showTerms() {
                new bootstrap.Modal(document.getElementById('termsModal')).show();
            }
        </script>
    </body>

    </html>
package controller;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.*;
import javax.servlet.http.*;

import dao.UtenteDAO;
import dao.CategoriaDAO;
import dao.NotificaDAO;
import dao.DBConnection;
import model.Utente;

public class RegistrationServlet extends HttpServlet {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    @Override
    public void init() throws ServletException {
        // Inizializza la connessione al database
        DBConnection.init(getServletContext());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Prendi dati dal form
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String email = request.getParameter("email");
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");

        // Validazione input
        String errore = validateInput(username, password, confirmPassword, email);
        if (errore != null) {
            request.setAttribute("errore", errore);
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            request.setAttribute("nome", nome);
            request.setAttribute("cognome", cognome);
            RequestDispatcher rd = request.getRequestDispatcher("jsp/registration.jsp");
            rd.forward(request, response);
            return;
        }

        UtenteDAO dao = new UtenteDAO();

        // Verifica se username o email esistono già
        if (dao.usernameExists(username)) {
            request.setAttribute("errore", "Username già esistente!");
            request.setAttribute("email", email);
            request.setAttribute("nome", nome);
            request.setAttribute("cognome", cognome);
            RequestDispatcher rd = request.getRequestDispatcher("jsp/registration.jsp");
            rd.forward(request, response);
            return;
        }

        if (dao.emailExists(email)) {
            request.setAttribute("errore", "Email già registrata!");
            request.setAttribute("username", username);
            request.setAttribute("nome", nome);
            request.setAttribute("cognome", cognome);
            RequestDispatcher rd = request.getRequestDispatcher("jsp/registration.jsp");
            rd.forward(request, response);
            return;
        }

        // Crea nuovo utente
        Utente utente = new Utente();
        utente.setUsername(username.trim());
        utente.setPassword(password);
        utente.setEmail(email.trim().toLowerCase());
        utente.setRuolo("utente");

        boolean success = dao.insert(utente);

        if (success) {
            // Trova l'utente appena creato per ottenere l'ID
            Utente utenteCreato = dao.findByUsername(username);
            if (utenteCreato != null) {
                // Crea categorie di default
                CategoriaDAO catDAO = new CategoriaDAO();
                catDAO.createDefaultCategories(utenteCreato.getId());

                // Crea notifica di benvenuto
                NotificaDAO notDAO = new NotificaDAO();
                notDAO.createBenvenuto(utenteCreato.getId(), username);
            }

            // Registrazione riuscita, reindirizzo a login
            response.sendRedirect(request.getContextPath() + "/jsp/login.jsp?registrazione=ok");
        } else {
            // Registrazione fallita
            request.setAttribute("errore", "Errore nella registrazione! Riprova.");
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            request.setAttribute("nome", nome);
            request.setAttribute("cognome", cognome);
            RequestDispatcher rd = request.getRequestDispatcher("jsp/registration.jsp");
            rd.forward(request, response);
        }
    }

    private String validateInput(String username, String password, String confirmPassword, String email) {
        // Validazione username
        if (username == null || username.trim().isEmpty()) {
            return "Username è obbligatorio";
        }
        if (username.trim().length() < 3) {
            return "Username deve essere di almeno 3 caratteri";
        }
        if (username.trim().length() > 50) {
            return "Username troppo lungo (max 50 caratteri)";
        }
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            return "Username può contenere solo lettere, numeri e underscore";
        }

        // Validazione password
        if (password == null || password.isEmpty()) {
            return "Password è obbligatoria";
        }
        if (password.length() < 6) {
            return "Password deve essere di almeno 6 caratteri";
        }
        if (password.length() > 255) {
            return "Password troppo lunga";
        }

        // Validazione conferma password
        if (confirmPassword == null || !password.equals(confirmPassword)) {
            return "Le password non corrispondono";
        }

        // Validazione email
        if (email == null || email.trim().isEmpty()) {
            return "Email è obbligatoria";
        }
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            return "Formato email non valido";
        }
        if (email.trim().length() > 100) {
            return "Email troppo lunga (max 100 caratteri)";
        }

        return null; // Nessun errore
    }
}

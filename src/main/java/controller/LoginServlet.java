package controller;

import dao.UtenteDAO;
import dao.DBConnection;
import dao.NotificaDAO;
import model.Utente;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Timestamp;

public class LoginServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        // Inizializza la connessione al database
        DBConnection.init(getServletContext());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String remember = request.getParameter("remember");

        // Validazione input
        if (username == null || username.trim().isEmpty()) {
            request.setAttribute("errore", "Username è obbligatorio");
            forwardToLogin(request, response);
            return;
        }

        if (password == null || password.isEmpty()) {
            request.setAttribute("errore", "Password è obbligatoria");
            request.setAttribute("username", username);
            forwardToLogin(request, response);
            return;
        }

        UtenteDAO dao = new UtenteDAO();
        Utente utente = dao.login(username.trim(), password);

        if (utente != null) {
            // Verifica se l'utente è attivo
            if (!utente.isAttivo()) {
                request.setAttribute("errore", "Account bloccato. Contatta l'amministratore.");
                request.setAttribute("username", username);
                forwardToLogin(request, response);
                return;
            }

            if (utente.isOspite()) {
                request.setAttribute("errore", "Account non attivato. Contatta l'amministratore.");
                request.setAttribute("username", username);
                forwardToLogin(request, response);
                return;
            }

            // Login riuscito: configura sessione
            HttpSession session = request.getSession();
            session.setAttribute("utente", utente);
            session.setAttribute("username", utente.getUsername());
            session.setAttribute("ruolo", utente.getRuolo());

            // Aggiorna ultimo accesso
            updateLastAccess(utente.getId());

            // Gestione "Ricordami"
            if (remember != null && remember.equals("on")) {
                Cookie usernameCookie = new Cookie("remembered_username", username);
                usernameCookie.setMaxAge(30 * 24 * 60 * 60); // 30 giorni
                usernameCookie.setPath("/");
                response.addCookie(usernameCookie);
            }

            // Reindirizza alla home page per tutti gli utenti
            response.sendRedirect(request.getContextPath() + "/jsp/home.jsp");

        } else {
            // Login fallito
            request.setAttribute("errore", "Username o password non validi");
            request.setAttribute("username", username);
            forwardToLogin(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Controlla se l'utente è già loggato
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("utente") != null) {
            // Reindirizza alla home page per tutti gli utenti già loggati
            response.sendRedirect(request.getContextPath() + "/jsp/home.jsp");
            return;
        }

        // Controlla cookie "ricordami"
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("remembered_username".equals(cookie.getName())) {
                    request.setAttribute("rememberedUsername", cookie.getValue());
                    break;
                }
            }
        }

        forwardToLogin(request, response);
    }

    private void forwardToLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("jsp/login.jsp");
        rd.forward(request, response);
    }

    private void updateLastAccess(int userId) {
        try {
            UtenteDAO dao = new UtenteDAO();
            dao.updateUltimoAccesso(userId);
        } catch (Exception e) {
            // Log dell'errore ma non bloccare il login
            e.printStackTrace();
        }
    }
}

package controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.UtenteDAO;
import dao.AppuntamentoDAO;
import dao.NotificaDAO;
import dao.DBConnection;
import model.Utente;
import model.Appuntamento;
import model.Notifica;

public class AdminServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        DBConnection.init(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Utente utente = (Utente) request.getSession().getAttribute("utente");
        if (utente == null || !utente.getRuolo().equals("admin")) {
            response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null)
            action = "dashboard";

        switch (action) {
            case "dashboard":
                dashboard(request, response, utente);
                break;
            case "users":
                gestisciUtenti(request, response, utente);
                break;
            case "appointments":
                gestisciAppuntamenti(request, response, utente);
                break;
            case "editUser":
                editUtente(request, response, utente);
                break;
            case "deleteUser":
                deleteUtente(request, response, utente);
                break;
            case "searchUsers":
                searchUtenti(request, response, utente);
                break;
            case "blockUser":
                bloccaUtente(request, response, utente);
                break;
            case "unblockUser":
                sbloccaUtente(request, response, utente);
                break;
            case "activateUser":
                attivaUtente(request, response, utente);
                break;
            case "notifications":
                gestisciNotifiche(request, response, utente);
                break;
            case "stats":
                statistiche(request, response, utente);
                break;
            default:
                dashboard(request, response, utente);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Utente utente = (Utente) request.getSession().getAttribute("utente");
        if (utente == null || !utente.getRuolo().equals("admin")) {
            response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null)
            action = "dashboard";

        switch (action) {
            case "updateUser":
                updateUtente(request, response, utente);
                break;
            case "createUser":
                createUtente(request, response, utente);
                break;
            case "sendNotification":
                sendNotificationToAll(request, response, utente);
                break;
            case "resetPassword":
                resetPassword(request, response, utente);
                break;
            case "sendSystemNotification":
                sendSystemNotification(request, response, utente);
                break;
            default:
                dashboard(request, response, utente);
        }
    }

    private void dashboard(HttpServletRequest request, HttpServletResponse response, Utente utente)
            throws ServletException, IOException {

        UtenteDAO userDAO = new UtenteDAO();
        AppuntamentoDAO appDAO = new AppuntamentoDAO();

        // Statistiche generali
        List<Utente> utenti = userDAO.findAll();
        List<Appuntamento> appuntamentiCondivisi = appDAO.findCondivisi();
        Map<String, Integer> statsUtenti = userDAO.getStatisticheUtenti();

        request.setAttribute("totalUsers", utenti.size());
        request.setAttribute("totalAppointments", appuntamentiCondivisi.size());
        request.setAttribute("recentUsers", utenti.subList(0, Math.min(5, utenti.size())));
        request.setAttribute("recentAppointments",
                appuntamentiCondivisi.subList(0, Math.min(5, appuntamentiCondivisi.size())));
        request.setAttribute("statsUtenti", statsUtenti);

        RequestDispatcher rd = request.getRequestDispatcher("jsp/admin.jsp");
        rd.forward(request, response);
    }

    private void gestisciUtenti(HttpServletRequest request, HttpServletResponse response, Utente utente)
            throws ServletException, IOException {

        UtenteDAO dao = new UtenteDAO();
        List<Utente> utenti = dao.findAll();

        request.setAttribute("utenti", utenti);
        request.setAttribute("currentSection", "users");

        RequestDispatcher rd = request.getRequestDispatcher("jsp/admin.jsp");
        rd.forward(request, response);
    }

    private void gestisciAppuntamenti(HttpServletRequest request, HttpServletResponse response, Utente utente)
            throws ServletException, IOException {

        AppuntamentoDAO dao = new AppuntamentoDAO();
        List<Appuntamento> appuntamenti = dao.findCondivisi();

        request.setAttribute("appuntamenti", appuntamenti);
        request.setAttribute("currentSection", "appointments");

        RequestDispatcher rd = request.getRequestDispatcher("jsp/admin.jsp");
        rd.forward(request, response);
    }

    private void gestisciNotifiche(HttpServletRequest request, HttpServletResponse response, Utente utente)
            throws ServletException, IOException {

        NotificaDAO dao = new NotificaDAO();
        List<Notifica> notifiche = dao.findAll();

        request.setAttribute("notifiche", notifiche);
        request.setAttribute("currentSection", "notifications");

        RequestDispatcher rd = request.getRequestDispatcher("jsp/admin.jsp");
        rd.forward(request, response);
    }

    private void statistiche(HttpServletRequest request, HttpServletResponse response, Utente utente)
            throws ServletException, IOException {

        UtenteDAO userDAO = new UtenteDAO();
        AppuntamentoDAO appDAO = new AppuntamentoDAO();

        Map<String, Integer> statsUtenti = userDAO.getStatisticheUtenti();
        List<Appuntamento> appuntamentiCondivisi = appDAO.findCondivisi();

        request.setAttribute("statsUtenti", statsUtenti);
        request.setAttribute("appuntamentiCondivisi", appuntamentiCondivisi);
        request.setAttribute("currentSection", "stats");

        RequestDispatcher rd = request.getRequestDispatcher("jsp/admin.jsp");
        rd.forward(request, response);
    }

    private void editUtente(HttpServletRequest request, HttpServletResponse response, Utente utente)
            throws ServletException, IOException {

        try {
            String userIdStr = request.getParameter("id");
            if (userIdStr == null || userIdStr.trim().isEmpty()) {
                request.setAttribute("errore", "ID utente non valido");
                gestisciUtenti(request, response, utente);
                return;
            }

            int userId = Integer.parseInt(userIdStr);
            UtenteDAO dao = new UtenteDAO();
            Utente utenteToEdit = dao.findById(userId);

            if (utenteToEdit == null) {
                request.setAttribute("errore", "Utente non trovato");
                gestisciUtenti(request, response, utente);
                return;
            }

            request.setAttribute("utenteToEdit", utenteToEdit);
            request.setAttribute("currentSection", "users");
            request.setAttribute("editMode", true);

        } catch (NumberFormatException e) {
            request.setAttribute("errore", "ID utente non valido");
        } catch (Exception e) {
            request.setAttribute("errore", "Errore interno: " + e.getMessage());
            e.printStackTrace();
        }

        gestisciUtenti(request, response, utente);
    }

    private void updateUtente(HttpServletRequest request, HttpServletResponse response, Utente utente)
            throws ServletException, IOException {

        try {
            String userIdStr = request.getParameter("id");
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String ruolo = request.getParameter("ruolo");

            if (userIdStr == null || username == null || email == null || ruolo == null) {
                request.setAttribute("errore", "Dati incompleti");
                gestisciUtenti(request, response, utente);
                return;
            }

            int userId = Integer.parseInt(userIdStr);
            UtenteDAO dao = new UtenteDAO();
            Utente utenteToUpdate = dao.findById(userId);

            if (utenteToUpdate == null) {
                request.setAttribute("errore", "Utente non trovato");
                gestisciUtenti(request, response, utente);
                return;
            }

            // Verifica che non si stia modificando se stesso per togliere admin
            if (userId == utente.getId() && !ruolo.equals("admin")) {
                request.setAttribute("errore", "Non puoi rimuovere il ruolo admin da te stesso");
                gestisciUtenti(request, response, utente);
                return;
            }

            utenteToUpdate.setUsername(username.trim());
            utenteToUpdate.setEmail(email.trim().toLowerCase());
            utenteToUpdate.setRuolo(ruolo);

            boolean success = dao.update(utenteToUpdate);

            if (success) {
                request.setAttribute("successo", "Utente aggiornato con successo!");
            } else {
                request.setAttribute("errore", "Errore nell'aggiornamento dell'utente");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("errore", "ID utente non valido");
        } catch (Exception e) {
            request.setAttribute("errore", "Errore interno: " + e.getMessage());
            e.printStackTrace();
        }

        gestisciUtenti(request, response, utente);
    }

    private void deleteUtente(HttpServletRequest request, HttpServletResponse response, Utente utente)
            throws ServletException, IOException {

        try {
            String userIdStr = request.getParameter("id");
            if (userIdStr == null || userIdStr.trim().isEmpty()) {
                request.setAttribute("errore", "ID utente non valido");
                gestisciUtenti(request, response, utente);
                return;
            }

            int userId = Integer.parseInt(userIdStr);

            // Verifica che non si stia eliminando se stesso
            if (userId == utente.getId()) {
                request.setAttribute("errore", "Non puoi eliminare te stesso");
                gestisciUtenti(request, response, utente);
                return;
            }

            UtenteDAO dao = new UtenteDAO();
            boolean success = dao.delete(userId);

            if (success) {
                request.setAttribute("successo", "Utente eliminato con successo!");
            } else {
                request.setAttribute("errore", "Errore nell'eliminazione dell'utente");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("errore", "ID utente non valido");
        } catch (Exception e) {
            request.setAttribute("errore", "Errore interno: " + e.getMessage());
            e.printStackTrace();
        }

        gestisciUtenti(request, response, utente);
    }

    private void bloccaUtente(HttpServletRequest request, HttpServletResponse response, Utente utente)
            throws ServletException, IOException {

        try {
            String userIdStr = request.getParameter("id");
            if (userIdStr == null || userIdStr.trim().isEmpty()) {
                request.setAttribute("errore", "ID utente non valido");
                gestisciUtenti(request, response, utente);
                return;
            }

            int userId = Integer.parseInt(userIdStr);

            // Verifica che non si stia bloccando se stesso
            if (userId == utente.getId()) {
                request.setAttribute("errore", "Non puoi bloccare te stesso");
                gestisciUtenti(request, response, utente);
                return;
            }

            UtenteDAO dao = new UtenteDAO();
            boolean success = dao.bloccaUtente(userId);

            if (success) {
                request.setAttribute("successo", "Utente bloccato con successo!");
            } else {
                request.setAttribute("errore", "Errore nel blocco dell'utente");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("errore", "ID utente non valido");
        } catch (Exception e) {
            request.setAttribute("errore", "Errore interno: " + e.getMessage());
            e.printStackTrace();
        }

        gestisciUtenti(request, response, utente);
    }

    private void sbloccaUtente(HttpServletRequest request, HttpServletResponse response, Utente utente)
            throws ServletException, IOException {

        try {
            String userIdStr = request.getParameter("id");
            if (userIdStr == null || userIdStr.trim().isEmpty()) {
                request.setAttribute("errore", "ID utente non valido");
                gestisciUtenti(request, response, utente);
                return;
            }

            int userId = Integer.parseInt(userIdStr);
            UtenteDAO dao = new UtenteDAO();
            boolean success = dao.sbloccaUtente(userId);

            if (success) {
                request.setAttribute("successo", "Utente sbloccato con successo!");
            } else {
                request.setAttribute("errore", "Errore nello sblocco dell'utente");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("errore", "ID utente non valido");
        } catch (Exception e) {
            request.setAttribute("errore", "Errore interno: " + e.getMessage());
            e.printStackTrace();
        }

        gestisciUtenti(request, response, utente);
    }

    private void attivaUtente(HttpServletRequest request, HttpServletResponse response, Utente utente)
            throws ServletException, IOException {

        try {
            String userIdStr = request.getParameter("id");
            if (userIdStr == null || userIdStr.trim().isEmpty()) {
                request.setAttribute("errore", "ID utente non valido");
                gestisciUtenti(request, response, utente);
                return;
            }

            int userId = Integer.parseInt(userIdStr);
            UtenteDAO dao = new UtenteDAO();

            // Attiva l'utente e cambia il ruolo da ospite a utente
            boolean success = dao.sbloccaUtente(userId);
            if (success) {
                Utente utenteToActivate = dao.findById(userId);
                if (utenteToActivate != null && utenteToActivate.isOspite()) {
                    utenteToActivate.setRuolo("utente");
                    success = dao.update(utenteToActivate);
                }
            }

            if (success) {
                request.setAttribute("successo", "Utente attivato con successo!");
            } else {
                request.setAttribute("errore", "Errore nell'attivazione dell'utente");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("errore", "ID utente non valido");
        } catch (Exception e) {
            request.setAttribute("errore", "Errore interno: " + e.getMessage());
            e.printStackTrace();
        }

        gestisciUtenti(request, response, utente);
    }

    private void searchUtenti(HttpServletRequest request, HttpServletResponse response, Utente utente)
            throws ServletException, IOException {

        String termine = request.getParameter("termine");
        if (termine == null || termine.trim().isEmpty()) {
            gestisciUtenti(request, response, utente);
            return;
        }

        UtenteDAO dao = new UtenteDAO();
        List<Utente> utenti = dao.search(termine.trim());

        request.setAttribute("utenti", utenti);
        request.setAttribute("currentSection", "users");
        request.setAttribute("searchTerm", termine.trim());

        RequestDispatcher rd = request.getRequestDispatcher("jsp/admin.jsp");
        rd.forward(request, response);
    }

    private void createUtente(HttpServletRequest request, HttpServletResponse response, Utente utente)
            throws ServletException, IOException {

        try {
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String ruolo = request.getParameter("ruolo");

            if (username == null || email == null || password == null || ruolo == null ||
                    username.trim().isEmpty() || email.trim().isEmpty() || password.isEmpty()) {
                request.setAttribute("errore", "Tutti i campi sono obbligatori");
                gestisciUtenti(request, response, utente);
                return;
            }

            UtenteDAO dao = new UtenteDAO();

            // Verifica duplicati
            if (dao.usernameExists(username.trim())) {
                request.setAttribute("errore", "Username già esistente");
                gestisciUtenti(request, response, utente);
                return;
            }

            if (dao.emailExists(email.trim())) {
                request.setAttribute("errore", "Email già registrata");
                gestisciUtenti(request, response, utente);
                return;
            }

            Utente nuovoUtente = new Utente();
            nuovoUtente.setUsername(username.trim());
            nuovoUtente.setEmail(email.trim().toLowerCase());
            nuovoUtente.setPassword(password);
            nuovoUtente.setRuolo(ruolo);

            boolean success = dao.insert(nuovoUtente);

            if (success) {
                request.setAttribute("successo", "Utente creato con successo!");
            } else {
                request.setAttribute("errore", "Errore nella creazione dell'utente");
            }

        } catch (Exception e) {
            request.setAttribute("errore", "Errore interno: " + e.getMessage());
            e.printStackTrace();
        }

        gestisciUtenti(request, response, utente);
    }

    private void sendNotificationToAll(HttpServletRequest request, HttpServletResponse response, Utente utente)
            throws ServletException, IOException {

        try {
            String titolo = request.getParameter("titolo");
            String messaggio = request.getParameter("messaggio");
            String tipo = request.getParameter("tipo");

            if (titolo == null || messaggio == null || tipo == null ||
                    titolo.trim().isEmpty() || messaggio.trim().isEmpty()) {
                request.setAttribute("errore", "Titolo e messaggio sono obbligatori");
                dashboard(request, response, utente);
                return;
            }

            NotificaDAO dao = new NotificaDAO();
            boolean success = dao.sendToAll(titolo.trim(), messaggio.trim(), tipo);

            if (success) {
                request.setAttribute("successo", "Notifica inviata a tutti gli utenti!");
            } else {
                request.setAttribute("errore", "Errore nell'invio della notifica");
            }

        } catch (Exception e) {
            request.setAttribute("errore", "Errore interno: " + e.getMessage());
            e.printStackTrace();
        }

        dashboard(request, response, utente);
    }

    private void sendSystemNotification(HttpServletRequest request, HttpServletResponse response, Utente utente)
            throws ServletException, IOException {

        try {
            String titolo = request.getParameter("titolo");
            String messaggio = request.getParameter("messaggio");
            String tipo = request.getParameter("tipo");
            String targetUsers = request.getParameter("targetUsers"); // "all", "active", "inactive"

            if (titolo == null || messaggio == null || tipo == null ||
                    titolo.trim().isEmpty() || messaggio.trim().isEmpty()) {
                request.setAttribute("errore", "Titolo e messaggio sono obbligatori");
                gestisciNotifiche(request, response, utente);
                return;
            }

            NotificaDAO dao = new NotificaDAO();
            boolean success = false;

            if ("all".equals(targetUsers)) {
                success = dao.sendToAll(titolo.trim(), messaggio.trim(), tipo);
            } else if ("active".equals(targetUsers)) {
                success = dao.sendToActiveUsers(titolo.trim(), messaggio.trim(), tipo);
            } else if ("inactive".equals(targetUsers)) {
                success = dao.sendToInactiveUsers(titolo.trim(), messaggio.trim(), tipo);
            }

            if (success) {
                request.setAttribute("successo", "Notifica di sistema inviata con successo!");
            } else {
                request.setAttribute("errore", "Errore nell'invio della notifica di sistema");
            }

        } catch (Exception e) {
            request.setAttribute("errore", "Errore interno: " + e.getMessage());
            e.printStackTrace();
        }

        gestisciNotifiche(request, response, utente);
    }

    private void resetPassword(HttpServletRequest request, HttpServletResponse response, Utente utente)
            throws ServletException, IOException {

        try {
            String userIdStr = request.getParameter("id");
            String newPassword = request.getParameter("newPassword");

            if (userIdStr == null || newPassword == null ||
                    userIdStr.trim().isEmpty() || newPassword.isEmpty()) {
                request.setAttribute("errore", "Dati incompleti");
                gestisciUtenti(request, response, utente);
                return;
            }

            int userId = Integer.parseInt(userIdStr);
            UtenteDAO dao = new UtenteDAO();

            boolean success = dao.updatePassword(userId, newPassword);

            if (success) {
                request.setAttribute("successo", "Password reimpostata con successo!");
            } else {
                request.setAttribute("errore", "Errore nel reset della password");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("errore", "ID utente non valido");
        } catch (Exception e) {
            request.setAttribute("errore", "Errore interno: " + e.getMessage());
            e.printStackTrace();
        }

        gestisciUtenti(request, response, utente);
    }
}
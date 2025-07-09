package controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.InvitoDAO;
import dao.AppuntamentoDAO;
import dao.UtenteDAO;
import dao.NotificaDAO;
import dao.DBConnection;
import model.Invito;
import model.Appuntamento;
import model.Utente;

public class InvitoServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        DBConnection.init(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Utente utente = (Utente) request.getSession().getAttribute("utente");
        if (utente == null) {
            response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null)
            action = "list";

        switch (action) {
            case "list":
                listaInviti(request, response, utente);
                break;
            case "inviati":
                invititInviati(request, response, utente);
                break;
            case "accept":
                accettaInvito(request, response, utente);
                break;
            case "decline":
            case "reject":
                rifiutaInvito(request, response, utente);
                break;
            case "cancel":
                eliminaInvito(request, response, utente);
                break;
            default:
                listaInviti(request, response, utente);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Utente utente = (Utente) request.getSession().getAttribute("utente");
        if (utente == null) {
            response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null)
            action = "create";

        switch (action) {
            case "create":
                creaInvito(request, response, utente);
                break;
            case "delete":
                eliminaInvito(request, response, utente);
                break;
            default:
                creaInvito(request, response, utente);
        }
    }

    private void listaInviti(HttpServletRequest request, HttpServletResponse response, Utente utente)
            throws ServletException, IOException {

        InvitoDAO dao = new InvitoDAO();
        List<Invito> inviti = dao.findInvitiRicevuti(utente.getId());

        request.setAttribute("inviti", inviti);
        request.setAttribute("tipoLista", "ricevuti");

        RequestDispatcher rd = request.getRequestDispatcher("jsp/inviti.jsp");
        rd.forward(request, response);
    }

    private void invititInviati(HttpServletRequest request, HttpServletResponse response, Utente utente)
            throws ServletException, IOException {

        InvitoDAO dao = new InvitoDAO();
        List<Invito> inviti = dao.findInvitiInviati(utente.getId());

        request.setAttribute("inviti", inviti);
        request.setAttribute("tipoLista", "inviati");

        RequestDispatcher rd = request.getRequestDispatcher("jsp/inviti.jsp");
        rd.forward(request, response);
    }

    private void creaInvito(HttpServletRequest request, HttpServletResponse response, Utente utente)
            throws ServletException, IOException {

        try {
            // Get form parameters
            String usernameInvitato = request.getParameter("username");
            String titolo = request.getParameter("titolo");
            String dataStr = request.getParameter("data");
            String oraStr = request.getParameter("ora");
            String descrizione = request.getParameter("descrizione");
            String messaggio = request.getParameter("messaggio");

            if (usernameInvitato == null || titolo == null || dataStr == null ||
                    usernameInvitato.trim().isEmpty() || titolo.trim().isEmpty() || dataStr.trim().isEmpty()) {
                request.getSession().setAttribute("errore", "Dati incompleti per l'invito");
                response.sendRedirect(request.getContextPath() + "/inviti");
                return;
            }

            // Verify invited user exists
            UtenteDAO userDAO = new UtenteDAO();
            Utente utenteInvitato = userDAO.findByUsername(usernameInvitato.trim());

            if (utenteInvitato == null) {
                request.getSession().setAttribute("errore", "Utente da invitare non trovato");
                response.sendRedirect(request.getContextPath() + "/inviti");
                return;
            }

            if (utenteInvitato.getId() == utente.getId()) {
                request.getSession().setAttribute("errore", "Non puoi invitare te stesso");
                response.sendRedirect(request.getContextPath() + "/inviti");
                return;
            }

            // Create appointment
            Appuntamento appuntamento = new Appuntamento();
            appuntamento.setTitolo(titolo.trim());
            appuntamento.setDescrizione(descrizione != null ? descrizione.trim() : "");

            // Parse date and time
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String dataOraStr = dataStr + " " + (oraStr != null && !oraStr.trim().isEmpty() ? oraStr : "00:00");
                Date dataOra = sdf.parse(dataOraStr);
                appuntamento.setDataOra(dataOra);
            } catch (ParseException e) {
                request.getSession().setAttribute("errore", "Formato data/ora non valido");
                response.sendRedirect(request.getContextPath() + "/inviti");
                return;
            }

            appuntamento.setIdUtente(utente.getId());
            appuntamento.setCondiviso(true); // Always true for invites

            // Save appointment
            AppuntamentoDAO appDAO = new AppuntamentoDAO();
            boolean appointmentSuccess = appDAO.insert(appuntamento);

            if (!appointmentSuccess) {
                request.getSession().setAttribute("errore", "Errore nella creazione dell'appuntamento");
                response.sendRedirect(request.getContextPath() + "/inviti");
                return;
            }

            // Create invite using the ID set in the appointment object
            Invito invito = new Invito(appuntamento.getId(), utente.getId(), utenteInvitato.getId(),
                    messaggio != null ? messaggio.trim() : "");

            InvitoDAO invDAO = new InvitoDAO();
            boolean inviteSuccess = invDAO.insert(invito);

            if (inviteSuccess) {
                // Create notification for invited user
                NotificaDAO notDAO = new NotificaDAO();
                notDAO.createInvito(utenteInvitato.getId(), utente.getUsername(), titolo);
                request.getSession().setAttribute("successo", "Invito inviato con successo!");
            } else {
                // If invite fails, delete the appointment
                appDAO.delete(appuntamento.getId());
                request.getSession().setAttribute("errore", "Errore nell'invio dell'invito");
            }

        } catch (Exception e) {
            request.getSession().setAttribute("errore", "Errore interno: " + e.getMessage());
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/inviti");
    }

    private void accettaInvito(HttpServletRequest request, HttpServletResponse response, Utente utente)
            throws ServletException, IOException {

        try {
            String invitoIdStr = request.getParameter("id");
            if (invitoIdStr == null || invitoIdStr.trim().isEmpty()) {
                request.setAttribute("errore", "ID invito non valido");
                listaInviti(request, response, utente);
                return;
            }

            int invitoId = Integer.parseInt(invitoIdStr);
            InvitoDAO dao = new InvitoDAO();
            Invito invito = dao.findById(invitoId);

            if (invito == null || invito.getIdUtenteInvitato() != utente.getId()) {
                request.setAttribute("errore", "Invito non trovato o non autorizzato");
                listaInviti(request, response, utente);
                return;
            }

            if (!invito.isPending()) {
                request.setAttribute("errore", "Invito già processato");
                listaInviti(request, response, utente);
                return;
            }

            boolean success = dao.updateStato(invitoId, "accettato");

            if (success) {
                request.setAttribute("successo", "Invito accettato!");
            } else {
                request.setAttribute("errore", "Errore nell'accettazione dell'invito");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("errore", "ID invito non valido");
        } catch (Exception e) {
            request.setAttribute("errore", "Errore interno: " + e.getMessage());
            e.printStackTrace();
        }

        listaInviti(request, response, utente);
    }

    private void rifiutaInvito(HttpServletRequest request, HttpServletResponse response, Utente utente)
            throws ServletException, IOException {

        try {
            String invitoIdStr = request.getParameter("id");
            if (invitoIdStr == null || invitoIdStr.trim().isEmpty()) {
                request.setAttribute("errore", "ID invito non valido");
                listaInviti(request, response, utente);
                return;
            }

            int invitoId = Integer.parseInt(invitoIdStr);
            InvitoDAO dao = new InvitoDAO();
            Invito invito = dao.findById(invitoId);

            if (invito == null || invito.getIdUtenteInvitato() != utente.getId()) {
                request.setAttribute("errore", "Invito non trovato o non autorizzato");
                listaInviti(request, response, utente);
                return;
            }

            if (!invito.isPending()) {
                request.setAttribute("errore", "Invito già processato");
                listaInviti(request, response, utente);
                return;
            }

            boolean success = dao.updateStato(invitoId, "rifiutato");

            if (success) {
                request.setAttribute("successo", "Invito rifiutato");
            } else {
                request.setAttribute("errore", "Errore nel rifiuto dell'invito");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("errore", "ID invito non valido");
        } catch (Exception e) {
            request.setAttribute("errore", "Errore interno: " + e.getMessage());
            e.printStackTrace();
        }

        listaInviti(request, response, utente);
    }

    private void eliminaInvito(HttpServletRequest request, HttpServletResponse response, Utente utente)
            throws ServletException, IOException {

        try {
            String invitoIdStr = request.getParameter("id");
            if (invitoIdStr == null || invitoIdStr.trim().isEmpty()) {
                request.setAttribute("errore", "ID invito non valido");
                listaInviti(request, response, utente);
                return;
            }

            int invitoId = Integer.parseInt(invitoIdStr);
            InvitoDAO dao = new InvitoDAO();
            Invito invito = dao.findById(invitoId);

            if (invito == null || invito.getIdUtenteInvitante() != utente.getId()) {
                request.setAttribute("errore", "Invito non trovato o non autorizzato");
                listaInviti(request, response, utente);
                return;
            }

            boolean success = dao.delete(invitoId);

            if (success) {
                request.setAttribute("successo", "Invito eliminato");
            } else {
                request.setAttribute("errore", "Errore nell'eliminazione dell'invito");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("errore", "ID invito non valido");
        } catch (Exception e) {
            request.setAttribute("errore", "Errore interno: " + e.getMessage());
            e.printStackTrace();
        }

        invititInviati(request, response, utente);
    }
}
package controller;

import java.io.IOException;
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
            String appuntamentoIdStr = request.getParameter("appuntamentoId");
            String usernameInvitato = request.getParameter("usernameInvitato");
            String messaggio = request.getParameter("messaggio");

            if (appuntamentoIdStr == null || usernameInvitato == null ||
                    appuntamentoIdStr.trim().isEmpty() || usernameInvitato.trim().isEmpty()) {
                request.setAttribute("errore", "Dati incompleti per l'invito");
                response.sendRedirect("agenda?action=list");
                return;
            }

            int appuntamentoId = Integer.parseInt(appuntamentoIdStr);

            // Verifica che l'appuntamento appartenga all'utente
            AppuntamentoDAO appDAO = new AppuntamentoDAO();
            Appuntamento appuntamento = appDAO.findById(appuntamentoId);

            if (appuntamento == null || appuntamento.getIdUtente() != utente.getId()) {
                request.setAttribute("errore", "Appuntamento non trovato o non autorizzato");
                response.sendRedirect("agenda?action=list");
                return;
            }

            // Verifica che l'utente invitato esista
            UtenteDAO userDAO = new UtenteDAO();
            Utente utenteInvitato = userDAO.findByUsername(usernameInvitato.trim());

            if (utenteInvitato == null) {
                request.setAttribute("errore", "Utente da invitare non trovato");
                response.sendRedirect("agenda?action=list");
                return;
            }

            if (utenteInvitato.getId() == utente.getId()) {
                request.setAttribute("errore", "Non puoi invitare te stesso");
                response.sendRedirect("agenda?action=list");
                return;
            }

            // Verifica che non esista già un invito
            InvitoDAO invDAO = new InvitoDAO();
            if (invDAO.existsInvito(appuntamentoId, utenteInvitato.getId())) {
                request.setAttribute("errore", "Invito già inviato a questo utente");
                response.sendRedirect("agenda?action=list");
                return;
            }

            // Crea l'invito
            Invito invito = new Invito(appuntamentoId, utente.getId(), utenteInvitato.getId(),
                    messaggio != null ? messaggio.trim() : "");

            boolean success = invDAO.insert(invito);

            if (success) {
                // Crea notifica per l'utente invitato
                NotificaDAO notDAO = new NotificaDAO();
                notDAO.createInvito(utenteInvitato.getId(), utente.getUsername(), appuntamento.getTitolo());

                request.setAttribute("successo", "Invito inviato con successo!");
            } else {
                request.setAttribute("errore", "Errore nell'invio dell'invito");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("errore", "ID appuntamento non valido");
        } catch (Exception e) {
            request.setAttribute("errore", "Errore interno: " + e.getMessage());
            e.printStackTrace();
        }

        response.sendRedirect("agenda?action=list");
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
package controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.AppuntamentoDAO;
import dao.CategoriaDAO;
import dao.DBConnection;
import dao.NotificaDAO;
import dao.UtenteDAO;
import model.Appuntamento;
import model.Categoria;
import model.Utente;

public class AppuntamentoServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        // Inizializza la connessione al database
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
                listaAppuntamenti(request, response, utente);
                break;
            case "edit":
                editAppuntamento(request, response, utente);
                break;
            case "delete":
                deleteAppuntamento(request, response, utente);
                break;
            case "search":
                searchAppuntamenti(request, response, utente);
                break;
            case "condivisi":
                appuntamentiCondivisi(request, response, utente);
                break;
            case "stats":
                if (utente.getRuolo().equals("admin")) {
                    mostraStatistiche(request, response, utente);
                } else {
                    listaAppuntamenti(request, response, utente);
                }
                break;
            default:
                listaAppuntamenti(request, response, utente);
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

        switch (action.trim()) {
            case "create":
                createAppuntamento(request, response, utente);
                break;
            case "update":
                updateAppuntamento(request, response, utente);
                break;
            default:
                createAppuntamento(request, response, utente);
        }
    }

    private void listaAppuntamenti(HttpServletRequest request, HttpServletResponse response, Utente utente)
            throws ServletException, IOException {

        AppuntamentoDAO appDAO = new AppuntamentoDAO();
        CategoriaDAO catDAO = new CategoriaDAO();
        UtenteDAO userDAO = new UtenteDAO();

        List<Appuntamento> appuntamenti;
        List<Categoria> categorie;
        List<Utente> utenti = null;

        // Se l'utente è admin, mostra tutti gli appuntamenti
        if (utente.getRuolo().equals("admin")) {
            appuntamenti = appDAO.findAllForAdmin();
            categorie = catDAO.findAll(); // Tutte le categorie per admin
            utenti = userDAO.findAll(); // Tutti gli utenti per permettere la selezione
        } else {
            appuntamenti = appDAO.findByUtente(utente.getId());
            categorie = catDAO.findByUtente(utente.getId());
        }

        request.setAttribute("appuntamenti", appuntamenti);
        request.setAttribute("categorie", categorie);
        request.setAttribute("utenti", utenti); // Per gli admin

        RequestDispatcher rd = request.getRequestDispatcher("jsp/agenda.jsp");
        rd.forward(request, response);
    }

    private void createAppuntamento(HttpServletRequest request, HttpServletResponse response, Utente utente)
            throws ServletException, IOException {

        try {
            String titolo = request.getParameter("titolo");
            String descrizione = request.getParameter("descrizione");
            String dataStr = request.getParameter("data");
            String oraStr = request.getParameter("ora");
            String condivisoStr = request.getParameter("condiviso");
            String categoriaStr = request.getParameter("categoria");
            String targetUserIdStr = request.getParameter("targetUserId"); // Nuovo parametro per admin

            // Validazione input
            if (titolo == null || titolo.trim().isEmpty()) {
                request.setAttribute("errore", "Il titolo è obbligatorio");
                listaAppuntamenti(request, response, utente);
                return;
            }

            if (dataStr == null || dataStr.trim().isEmpty()) {
                request.setAttribute("errore", "La data è obbligatoria");
                listaAppuntamenti(request, response, utente);
                return;
            }

            // Parsing data e ora
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String dataOraStr = dataStr + " " + (oraStr != null ? oraStr : "00:00");
            Date dataOra = sdf.parse(dataOraStr);

            // Verifica che la data non sia nel passato
            if (dataOra.before(new Date())) {
                request.setAttribute("errore", "Non puoi creare appuntamenti nel passato");
                listaAppuntamenti(request, response, utente);
                return;
            }

            // Determina l'ID utente target
            int targetUserId = utente.getId(); // Default: utente corrente

            // Se è admin e ha specificato un target user, usa quello
            if (utente.getRuolo().equals("admin") && targetUserIdStr != null && !targetUserIdStr.trim().isEmpty()) {
                try {
                    targetUserId = Integer.parseInt(targetUserIdStr);

                    // Verifica che l'utente target esista
                    UtenteDAO userDAO = new UtenteDAO();
                    Utente targetUser = userDAO.findById(targetUserId);
                    if (targetUser == null) {
                        request.setAttribute("errore", "Utente specificato non trovato");
                        listaAppuntamenti(request, response, utente);
                        return;
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("errore", "ID utente non valido");
                    listaAppuntamenti(request, response, utente);
                    return;
                }
            }

            Appuntamento appuntamento = new Appuntamento();
            appuntamento.setTitolo(titolo.trim());
            appuntamento.setDescrizione(descrizione != null ? descrizione.trim() : "");
            appuntamento.setDataOra(dataOra);
            appuntamento.setCondiviso(condivisoStr != null && condivisoStr.equals("on"));

            // Gestione categoria
            if (categoriaStr != null && !categoriaStr.trim().isEmpty()) {
                try {
                    int idCategoria = Integer.parseInt(categoriaStr);

                    // Verifica che la categoria appartenga all'utente target o sia dell'admin
                    CategoriaDAO catDAO = new CategoriaDAO();
                    if (utente.getRuolo().equals("admin") || catDAO.belongsToUser(idCategoria, targetUserId)) {
                        appuntamento.setIdCategoria(idCategoria);
                    }
                } catch (NumberFormatException e) {
                    // Ignora categoria non valida
                }
            }

            AppuntamentoDAO dao = new AppuntamentoDAO();
            boolean success;

            // Usa il metodo appropriato basato sul ruolo
            if (utente.getRuolo().equals("admin") && targetUserId != utente.getId()) {
                success = dao.insertForUser(appuntamento, targetUserId);
            } else {
                appuntamento.setIdUtente(utente.getId());
                success = dao.insert(appuntamento);
            }

            if (success) {
                String successMessage = "Appuntamento creato con successo!";
                if (utente.getRuolo().equals("admin") && targetUserId != utente.getId()) {
                    UtenteDAO userDAO = new UtenteDAO();
                    Utente targetUser = userDAO.findById(targetUserId);
                    successMessage += " (per utente: " + targetUser.getUsername() + ")";
                }
                request.setAttribute("successo", successMessage);

                // Crea notifica di promemoria se l'appuntamento è tra 24 ore
                long timeDiff = dataOra.getTime() - System.currentTimeMillis();
                if (timeDiff > 0 && timeDiff < 24 * 60 * 60 * 1000) { // Meno di 24 ore
                    NotificaDAO notDAO = new NotificaDAO();
                    notDAO.createPromemoria(targetUserId, titolo, dataOra);
                }
            } else {
                request.setAttribute("errore", "Errore nella creazione dell'appuntamento");
            }

        } catch (ParseException e) {
            request.setAttribute("errore", "Formato data/ora non valido");
        } catch (Exception e) {
            request.setAttribute("errore", "Errore interno: " + e.getMessage());
            e.printStackTrace();
        }

        listaAppuntamenti(request, response, utente);
    }

    private void updateAppuntamento(HttpServletRequest request, HttpServletResponse response, Utente utente)
            throws ServletException, IOException {

        try {
            String idStr = request.getParameter("id");
            String titolo = request.getParameter("titolo");
            String descrizione = request.getParameter("descrizione");
            String dataStr = request.getParameter("data");
            String oraStr = request.getParameter("ora");
            String condivisoStr = request.getParameter("condiviso");
            String categoriaStr = request.getParameter("categoria");

            // Debug logging
            System.out.println("DEBUG UPDATE - ID: " + idStr);
            System.out.println("DEBUG UPDATE - Titolo: " + titolo);
            System.out.println("DEBUG UPDATE - Action: " + request.getParameter("action"));

            if (idStr == null || idStr.trim().isEmpty()) {
                request.setAttribute("errore", "ID appuntamento non valido");
                listaAppuntamenti(request, response, utente);
                return;
            }

            int id = Integer.parseInt(idStr);
            AppuntamentoDAO dao = new AppuntamentoDAO();

            // Verifica che l'appuntamento esista prima di procedere
            Appuntamento appuntamento = dao.findById(id);

            System.out.println("DEBUG UPDATE - Appuntamento trovato: " + (appuntamento != null));
            if (appuntamento != null) {
                System.out.println("DEBUG UPDATE - ID originale: " + appuntamento.getId());
                System.out.println("DEBUG UPDATE - Utente originale: " + appuntamento.getIdUtente());
                System.out.println("DEBUG UPDATE - Utente corrente: " + utente.getId());
            }

            if (appuntamento == null) {
                request.setAttribute("errore", "Appuntamento non trovato");
                listaAppuntamenti(request, response, utente);
                return;
            }

            // Verifica autorizzazioni
            if (appuntamento.getIdUtente() != utente.getId() && !utente.getRuolo().equals("admin")) {
                request.setAttribute("errore", "Non sei autorizzato a modificare questo appuntamento");
                listaAppuntamenti(request, response, utente);
                return;
            }

            // Validazione input
            if (titolo == null || titolo.trim().isEmpty()) {
                request.setAttribute("errore", "Il titolo è obbligatorio");
                listaAppuntamenti(request, response, utente);
                return;
            }

            if (dataStr == null || dataStr.trim().isEmpty()) {
                request.setAttribute("errore", "La data è obbligatoria");
                listaAppuntamenti(request, response, utente);
                return;
            }

            // Aggiorna i campi dell'appuntamento esistente
            appuntamento.setTitolo(titolo.trim());
            appuntamento.setDescrizione(descrizione != null ? descrizione.trim() : "");

            // Parsing data e ora
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String dataOraStr = dataStr + " " + (oraStr != null ? oraStr : "00:00");
            Date dataOra = sdf.parse(dataOraStr);
            appuntamento.setDataOra(dataOra);

            appuntamento.setCondiviso(condivisoStr != null && condivisoStr.equals("on"));

            // Gestione categoria
            if (categoriaStr != null && !categoriaStr.trim().isEmpty()) {
                try {
                    int idCategoria = Integer.parseInt(categoriaStr);
                    appuntamento.setIdCategoria(idCategoria);
                } catch (NumberFormatException e) {
                    appuntamento.setIdCategoria(0); // Rimuovi categoria
                }
            } else {
                appuntamento.setIdCategoria(0); // Rimuovi categoria se non selezionata
            }

            // IMPORTANTE: Non modificare l'ID utente - l'appuntamento mantiene il
            // proprietario originale
            // appuntamento.setIdUtente() NON deve essere chiamato qui

            System.out.println("DEBUG UPDATE - Prima del save - ID: " + appuntamento.getId());
            System.out.println("DEBUG UPDATE - Prima del save - Utente: " + appuntamento.getIdUtente());
            System.out.println("DEBUG UPDATE - Prima del save - Titolo: " + appuntamento.getTitolo());

            // Verifica ancora una volta che l'ID sia valido
            if (appuntamento.getId() <= 0) {
                request.setAttribute("errore", "ID appuntamento non valido per l'aggiornamento");
                listaAppuntamenti(request, response, utente);
                return;
            }

            // Usa il nuovo metodo updateOrReplace che elimina e reinserisce
            boolean success = dao.updateOrReplace(appuntamento);

            System.out.println("DEBUG UPDATE - Risultato updateOrReplace: " + success);
            if (success) {
                System.out.println("DEBUG UPDATE - Nuovo ID dopo updateOrReplace: " + appuntamento.getId());
            }

            if (success) {
                request.setAttribute("successo", "Appuntamento aggiornato con successo!");
            } else {
                request.setAttribute("errore", "Errore nell'aggiornamento dell'appuntamento");
            }

        } catch (ParseException e) {
            request.setAttribute("errore", "Formato data/ora non valido");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            request.setAttribute("errore", "ID appuntamento non valido");
            e.printStackTrace();
        } catch (Exception e) {
            request.setAttribute("errore", "Errore interno: " + e.getMessage());
            e.printStackTrace();
        }

        listaAppuntamenti(request, response, utente);
    }

    private void editAppuntamento(HttpServletRequest request, HttpServletResponse response, Utente utente)
            throws ServletException, IOException {

        try {
            String idStr = request.getParameter("id");
            if (idStr == null || idStr.trim().isEmpty()) {
                request.setAttribute("errore", "ID appuntamento non valido");
                listaAppuntamenti(request, response, utente);
                return;
            }

            int id = Integer.parseInt(idStr);
            AppuntamentoDAO dao = new AppuntamentoDAO();
            Appuntamento appuntamento = dao.findById(id);

            if (appuntamento == null
                    || (appuntamento.getIdUtente() != utente.getId() && !utente.getRuolo().equals("admin"))) {
                request.setAttribute("errore", "Appuntamento non trovato o non autorizzato");
                listaAppuntamenti(request, response, utente);
                return;
            }

            CategoriaDAO catDAO = new CategoriaDAO();
            List<Categoria> categorie;
            if (utente.getRuolo().equals("admin")) {
                categorie = catDAO.findAll();
            } else {
                categorie = catDAO.findByUtente(utente.getId());
            }

            request.setAttribute("appuntamentoEdit", appuntamento);
            request.setAttribute("categorie", categorie);
            request.setAttribute("editMode", true);

        } catch (NumberFormatException e) {
            request.setAttribute("errore", "ID appuntamento non valido");
        } catch (Exception e) {
            request.setAttribute("errore", "Errore interno: " + e.getMessage());
            e.printStackTrace();
        }

        listaAppuntamenti(request, response, utente);
    }

    private void deleteAppuntamento(HttpServletRequest request, HttpServletResponse response, Utente utente)
            throws ServletException, IOException {

        try {
            String idStr = request.getParameter("id");
            if (idStr == null || idStr.trim().isEmpty()) {
                request.setAttribute("errore", "ID appuntamento non valido");
                listaAppuntamenti(request, response, utente);
                return;
            }

            int id = Integer.parseInt(idStr);
            AppuntamentoDAO dao = new AppuntamentoDAO();
            Appuntamento appuntamento = dao.findById(id);

            if (appuntamento == null
                    || (appuntamento.getIdUtente() != utente.getId() && !utente.getRuolo().equals("admin"))) {
                request.setAttribute("errore", "Appuntamento non trovato o non autorizzato");
                listaAppuntamenti(request, response, utente);
                return;
            }

            boolean success = dao.delete(id);

            if (success) {
                request.setAttribute("successo", "Appuntamento eliminato con successo!");
            } else {
                request.setAttribute("errore", "Errore nell'eliminazione dell'appuntamento");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("errore", "ID appuntamento non valido");
        } catch (Exception e) {
            request.setAttribute("errore", "Errore interno: " + e.getMessage());
            e.printStackTrace();
        }

        listaAppuntamenti(request, response, utente);
    }

    private void searchAppuntamenti(HttpServletRequest request, HttpServletResponse response, Utente utente)
            throws ServletException, IOException {

        String termine = request.getParameter("termine");
        if (termine == null || termine.trim().isEmpty()) {
            listaAppuntamenti(request, response, utente);
            return;
        }

        AppuntamentoDAO dao = new AppuntamentoDAO();
        CategoriaDAO catDAO = new CategoriaDAO();

        List<Appuntamento> appuntamenti = dao.search(utente.getId(), termine.trim());
        List<Categoria> categorie = catDAO.findByUtente(utente.getId());

        request.setAttribute("appuntamenti", appuntamenti);
        request.setAttribute("categorie", categorie);
        request.setAttribute("termine", termine.trim());
        request.setAttribute("searchMode", true);

        RequestDispatcher rd = request.getRequestDispatcher("jsp/agenda.jsp");
        rd.forward(request, response);
    }

    private void appuntamentiCondivisi(HttpServletRequest request, HttpServletResponse response, Utente utente)
            throws ServletException, IOException {

        AppuntamentoDAO dao = new AppuntamentoDAO();
        List<Appuntamento> appuntamenti = dao.findCondivisi();

        request.setAttribute("appuntamenti", appuntamenti);
        request.setAttribute("tipoVista", "condivisi");

        RequestDispatcher rd = request.getRequestDispatcher("jsp/agenda.jsp");
        rd.forward(request, response);
    }

    private void mostraStatistiche(HttpServletRequest request, HttpServletResponse response, Utente utente)
            throws ServletException, IOException {

        if (!utente.getRuolo().equals("admin")) {
            response.sendRedirect(request.getContextPath() + "/agenda");
            return;
        }

        UtenteDAO userDAO = new UtenteDAO();
        AppuntamentoDAO appDAO = new AppuntamentoDAO();

        List<Utente> utenti = userDAO.findAll();
        Map<String, Integer> statistiche = new HashMap<>();

        for (Utente u : utenti) {
            int count = appDAO.countByUser(u.getId());
            statistiche.put(u.getUsername(), count);
        }

        // Statistiche generali
        int totalAppuntamenti = appDAO.findAllForAdmin().size();
        int appuntamentiCondivisi = appDAO.findCondivisi().size();

        request.setAttribute("utenti", utenti);
        request.setAttribute("statistiche", statistiche);
        request.setAttribute("totalAppuntamenti", totalAppuntamenti);
        request.setAttribute("appuntamentiCondivisi", appuntamentiCondivisi);

        RequestDispatcher rd = request.getRequestDispatcher("jsp/stats.jsp");
        rd.forward(request, response);
    }
}

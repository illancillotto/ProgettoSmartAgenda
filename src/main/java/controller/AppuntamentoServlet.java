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

        switch (action) {
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

        List<Appuntamento> appuntamenti = appDAO.findByUtente(utente.getId());
        List<Categoria> categorie = catDAO.findByUtente(utente.getId());

        request.setAttribute("appuntamenti", appuntamenti);
        request.setAttribute("categorie", categorie);

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

            Appuntamento appuntamento = new Appuntamento();
            appuntamento.setTitolo(titolo.trim());
            appuntamento.setDescrizione(descrizione != null ? descrizione.trim() : "");
            appuntamento.setDataOra(dataOra);
            appuntamento.setIdUtente(utente.getId());
            appuntamento.setCondiviso(condivisoStr != null && condivisoStr.equals("on"));

            if (categoriaStr != null && !categoriaStr.trim().isEmpty()) {
                try {
                    int idCategoria = Integer.parseInt(categoriaStr);
                    appuntamento.setIdCategoria(idCategoria);
                } catch (NumberFormatException e) {
                    // Ignora categoria non valida
                }
            }

            AppuntamentoDAO dao = new AppuntamentoDAO();
            boolean success = dao.insert(appuntamento);

            if (success) {
                request.setAttribute("successo", "Appuntamento creato con successo!");

                // Crea notifica di promemoria se l'appuntamento è tra 24 ore
                long timeDiff = dataOra.getTime() - System.currentTimeMillis();
                if (timeDiff > 0 && timeDiff < 24 * 60 * 60 * 1000) { // Meno di 24 ore
                    NotificaDAO notDAO = new NotificaDAO();
                    notDAO.createPromemoria(utente.getId(), titolo, dataOra);
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

            if (idStr == null || idStr.trim().isEmpty()) {
                request.setAttribute("errore", "ID appuntamento non valido");
                listaAppuntamenti(request, response, utente);
                return;
            }

            int id = Integer.parseInt(idStr);
            AppuntamentoDAO dao = new AppuntamentoDAO();
            Appuntamento appuntamento = dao.findById(id);

            if (appuntamento == null || appuntamento.getIdUtente() != utente.getId()) {
                request.setAttribute("errore", "Appuntamento non trovato o non autorizzato");
                listaAppuntamenti(request, response, utente);
                return;
            }

            // Aggiorna i campi
            if (titolo != null && !titolo.trim().isEmpty()) {
                appuntamento.setTitolo(titolo.trim());
            }
            if (descrizione != null) {
                appuntamento.setDescrizione(descrizione.trim());
            }
            if (dataStr != null && !dataStr.trim().isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String dataOraStr = dataStr + " " + (oraStr != null ? oraStr : "00:00");
                Date dataOra = sdf.parse(dataOraStr);
                appuntamento.setDataOra(dataOra);
            }

            appuntamento.setCondiviso(condivisoStr != null && condivisoStr.equals("on"));

            if (categoriaStr != null && !categoriaStr.trim().isEmpty()) {
                try {
                    int idCategoria = Integer.parseInt(categoriaStr);
                    appuntamento.setIdCategoria(idCategoria);
                } catch (NumberFormatException e) {
                    appuntamento.setIdCategoria(0); // Rimuovi categoria
                }
            }

            boolean success = dao.update(appuntamento);

            if (success) {
                request.setAttribute("successo", "Appuntamento aggiornato con successo!");
            } else {
                request.setAttribute("errore", "Errore nell'aggiornamento dell'appuntamento");
            }

        } catch (ParseException e) {
            request.setAttribute("errore", "Formato data/ora non valido");
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

            if (appuntamento == null || appuntamento.getIdUtente() != utente.getId()) {
                request.setAttribute("errore", "Appuntamento non trovato o non autorizzato");
                listaAppuntamenti(request, response, utente);
                return;
            }

            CategoriaDAO catDAO = new CategoriaDAO();
            List<Categoria> categorie = catDAO.findByUtente(utente.getId());

            request.setAttribute("appuntamento", appuntamento);
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

            if (appuntamento == null || appuntamento.getIdUtente() != utente.getId()) {
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
        request.setAttribute("condivisiMode", true);

        RequestDispatcher rd = request.getRequestDispatcher("jsp/agenda.jsp");
        rd.forward(request, response);
    }
}

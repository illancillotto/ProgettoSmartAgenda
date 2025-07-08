package dao;

import model.Invito;
import java.sql.*;
import java.util.*;

public class InvitoDAO {

    // Inserisce un nuovo invito
    public boolean insert(Invito invito) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO INVITI (ID_APPUNTAMENTO, ID_UTENTE_INVITANTE, ID_UTENTE_INVITATO, MESSAGGIO) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, invito.getIdAppuntamento());
            ps.setInt(2, invito.getIdUtenteInvitante());
            ps.setInt(3, invito.getIdUtenteInvitato());
            ps.setString(4, invito.getMessaggio());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Trova inviti ricevuti da un utente (metodo compatibile con JSP)
    public List<Invito> findByDestinatario(int idUtente) {
        return findInvitiRicevuti(idUtente);
    }

    // Trova inviti inviati da un utente (metodo compatibile con JSP)
    public List<Invito> findByMittente(int idUtente) {
        return findInvitiInviati(idUtente);
    }

    // Trova inviti ricevuti da un utente
    public List<Invito> findInvitiRicevuti(int idUtente) {
        List<Invito> lista = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT i.*, u1.USERNAME as username_invitante, u2.USERNAME as username_invitato, " +
                    "a.TITOLO as titolo_appuntamento, a.DATA as data_appuntamento, a.DESCRIZIONE " +
                    "FROM INVITI i " +
                    "JOIN UTENTI u1 ON i.ID_UTENTE_INVITANTE = u1.ID " +
                    "JOIN UTENTI u2 ON i.ID_UTENTE_INVITATO = u2.ID " +
                    "JOIN APPUNTAMENTI a ON i.ID_APPUNTAMENTO = a.ID " +
                    "WHERE i.ID_UTENTE_INVITATO = ? ORDER BY i.DATA_INVITO DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUtente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Invito invito = new Invito();
                invito.setId(rs.getInt("ID"));
                invito.setIdAppuntamento(rs.getInt("ID_APPUNTAMENTO"));
                invito.setIdUtenteInvitante(rs.getInt("ID_UTENTE_INVITANTE"));
                invito.setIdUtenteInvitato(rs.getInt("ID_UTENTE_INVITATO"));
                invito.setStato(rs.getString("STATO"));
                invito.setDataInvito(rs.getTimestamp("DATA_INVITO"));
                invito.setDataRisposta(rs.getTimestamp("DATA_RISPOSTA"));
                invito.setMessaggio(rs.getString("MESSAGGIO"));
                invito.setUsernameInvitante(rs.getString("username_invitante"));
                invito.setUsernameInvitato(rs.getString("username_invitato"));
                invito.setTitoloAppuntamento(rs.getString("titolo_appuntamento"));
                invito.setDataAppuntamento(rs.getTimestamp("data_appuntamento"));
                invito.setDescrizione(rs.getString("DESCRIZIONE"));
                lista.add(invito);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Trova inviti inviati da un utente
    public List<Invito> findInvitiInviati(int idUtente) {
        List<Invito> lista = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT i.*, u1.USERNAME as username_invitante, u2.USERNAME as username_invitato, " +
                    "a.TITOLO as titolo_appuntamento, a.DATA as data_appuntamento, a.DESCRIZIONE " +
                    "FROM INVITI i " +
                    "JOIN UTENTI u1 ON i.ID_UTENTE_INVITANTE = u1.ID " +
                    "JOIN UTENTI u2 ON i.ID_UTENTE_INVITATO = u2.ID " +
                    "JOIN APPUNTAMENTI a ON i.ID_APPUNTAMENTO = a.ID " +
                    "WHERE i.ID_UTENTE_INVITANTE = ? ORDER BY i.DATA_INVITO DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUtente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Invito invito = new Invito();
                invito.setId(rs.getInt("ID"));
                invito.setIdAppuntamento(rs.getInt("ID_APPUNTAMENTO"));
                invito.setIdUtenteInvitante(rs.getInt("ID_UTENTE_INVITANTE"));
                invito.setIdUtenteInvitato(rs.getInt("ID_UTENTE_INVITATO"));
                invito.setStato(rs.getString("STATO"));
                invito.setDataInvito(rs.getTimestamp("DATA_INVITO"));
                invito.setDataRisposta(rs.getTimestamp("DATA_RISPOSTA"));
                invito.setMessaggio(rs.getString("MESSAGGIO"));
                invito.setUsernameInvitante(rs.getString("username_invitante"));
                invito.setUsernameInvitato(rs.getString("username_invitato"));
                invito.setTitoloAppuntamento(rs.getString("titolo_appuntamento"));
                invito.setDataAppuntamento(rs.getTimestamp("data_appuntamento"));
                invito.setDescrizione(rs.getString("DESCRIZIONE"));
                lista.add(invito);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Trova invito per ID
    public Invito findById(int id) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT i.*, u1.USERNAME as username_invitante, u2.USERNAME as username_invitato, " +
                    "a.TITOLO as titolo_appuntamento, a.DATA as data_appuntamento, a.DESCRIZIONE " +
                    "FROM INVITI i " +
                    "JOIN UTENTI u1 ON i.ID_UTENTE_INVITANTE = u1.ID " +
                    "JOIN UTENTI u2 ON i.ID_UTENTE_INVITATO = u2.ID " +
                    "JOIN APPUNTAMENTI a ON i.ID_APPUNTAMENTO = a.ID " +
                    "WHERE i.ID = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Invito invito = new Invito();
                invito.setId(rs.getInt("ID"));
                invito.setIdAppuntamento(rs.getInt("ID_APPUNTAMENTO"));
                invito.setIdUtenteInvitante(rs.getInt("ID_UTENTE_INVITANTE"));
                invito.setIdUtenteInvitato(rs.getInt("ID_UTENTE_INVITATO"));
                invito.setStato(rs.getString("STATO"));
                invito.setDataInvito(rs.getTimestamp("DATA_INVITO"));
                invito.setDataRisposta(rs.getTimestamp("DATA_RISPOSTA"));
                invito.setMessaggio(rs.getString("MESSAGGIO"));
                invito.setUsernameInvitante(rs.getString("username_invitante"));
                invito.setUsernameInvitato(rs.getString("username_invitato"));
                invito.setTitoloAppuntamento(rs.getString("titolo_appuntamento"));
                invito.setDataAppuntamento(rs.getTimestamp("data_appuntamento"));
                invito.setDescrizione(rs.getString("DESCRIZIONE"));
                return invito;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Aggiorna lo stato di un invito
    public boolean updateStato(int id, String stato) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "UPDATE INVITI SET STATO = ?, DATA_RISPOSTA = CURRENT_TIMESTAMP WHERE ID = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, stato);
            ps.setInt(2, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Elimina un invito
    public boolean delete(int id) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "DELETE FROM INVITI WHERE ID = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Verifica se esiste già un invito per un appuntamento e utente
    public boolean existsInvito(int idAppuntamento, int idUtenteInvitato) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT COUNT(*) FROM INVITI WHERE ID_APPUNTAMENTO = ? AND ID_UTENTE_INVITATO = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idAppuntamento);
            ps.setInt(2, idUtenteInvitato);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Trova inviti per un appuntamento specifico
    public List<Invito> findByAppuntamento(int idAppuntamento) {
        List<Invito> lista = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT i.*, u1.USERNAME as username_invitante, u2.USERNAME as username_invitato " +
                    "FROM INVITI i " +
                    "JOIN UTENTI u1 ON i.ID_UTENTE_INVITANTE = u1.ID " +
                    "JOIN UTENTI u2 ON i.ID_UTENTE_INVITATO = u2.ID " +
                    "WHERE i.ID_APPUNTAMENTO = ? ORDER BY i.DATA_INVITO DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idAppuntamento);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Invito invito = new Invito();
                invito.setId(rs.getInt("ID"));
                invito.setIdAppuntamento(rs.getInt("ID_APPUNTAMENTO"));
                invito.setIdUtenteInvitante(rs.getInt("ID_UTENTE_INVITANTE"));
                invito.setIdUtenteInvitato(rs.getInt("ID_UTENTE_INVITATO"));
                invito.setStato(rs.getString("STATO"));
                invito.setDataInvito(rs.getTimestamp("DATA_INVITO"));
                invito.setDataRisposta(rs.getTimestamp("DATA_RISPOSTA"));
                invito.setMessaggio(rs.getString("MESSAGGIO"));
                invito.setUsernameInvitante(rs.getString("username_invitante"));
                invito.setUsernameInvitato(rs.getString("username_invitato"));
                lista.add(invito);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}
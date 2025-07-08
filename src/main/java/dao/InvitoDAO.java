package dao;

import model.Invito;
import java.sql.*;
import java.util.*;

public class InvitoDAO {

    // Inserisce un nuovo invito
    public boolean insert(Invito invito) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO inviti (id_appuntamento, id_utente_invitante, id_utente_invitato, messaggio) VALUES (?, ?, ?, ?)";
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

    // Trova inviti ricevuti da un utente
    public List<Invito> findInvitiRicevuti(int idUtente) {
        List<Invito> lista = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT i.*, u1.username as username_invitante, u2.username as username_invitato, " +
                    "a.titolo as titolo_appuntamento, a.data as data_appuntamento " +
                    "FROM inviti i " +
                    "JOIN utenti u1 ON i.id_utente_invitante = u1.id " +
                    "JOIN utenti u2 ON i.id_utente_invitato = u2.id " +
                    "JOIN appuntamenti a ON i.id_appuntamento = a.id " +
                    "WHERE i.id_utente_invitato = ? ORDER BY i.data_invito DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUtente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Invito invito = new Invito();
                invito.setId(rs.getInt("id"));
                invito.setIdAppuntamento(rs.getInt("id_appuntamento"));
                invito.setIdUtenteInvitante(rs.getInt("id_utente_invitante"));
                invito.setIdUtenteInvitato(rs.getInt("id_utente_invitato"));
                invito.setStato(rs.getString("stato"));
                invito.setDataInvito(rs.getTimestamp("data_invito"));
                invito.setDataRisposta(rs.getTimestamp("data_risposta"));
                invito.setMessaggio(rs.getString("messaggio"));
                invito.setUsernameInvitante(rs.getString("username_invitante"));
                invito.setUsernameInvitato(rs.getString("username_invitato"));
                invito.setTitoloAppuntamento(rs.getString("titolo_appuntamento"));
                invito.setDataAppuntamento(rs.getTimestamp("data_appuntamento"));
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
            String sql = "SELECT i.*, u1.username as username_invitante, u2.username as username_invitato, " +
                    "a.titolo as titolo_appuntamento, a.data as data_appuntamento " +
                    "FROM inviti i " +
                    "JOIN utenti u1 ON i.id_utente_invitante = u1.id " +
                    "JOIN utenti u2 ON i.id_utente_invitato = u2.id " +
                    "JOIN appuntamenti a ON i.id_appuntamento = a.id " +
                    "WHERE i.id_utente_invitante = ? ORDER BY i.data_invito DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUtente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Invito invito = new Invito();
                invito.setId(rs.getInt("id"));
                invito.setIdAppuntamento(rs.getInt("id_appuntamento"));
                invito.setIdUtenteInvitante(rs.getInt("id_utente_invitante"));
                invito.setIdUtenteInvitato(rs.getInt("id_utente_invitato"));
                invito.setStato(rs.getString("stato"));
                invito.setDataInvito(rs.getTimestamp("data_invito"));
                invito.setDataRisposta(rs.getTimestamp("data_risposta"));
                invito.setMessaggio(rs.getString("messaggio"));
                invito.setUsernameInvitante(rs.getString("username_invitante"));
                invito.setUsernameInvitato(rs.getString("username_invitato"));
                invito.setTitoloAppuntamento(rs.getString("titolo_appuntamento"));
                invito.setDataAppuntamento(rs.getTimestamp("data_appuntamento"));
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
            String sql = "SELECT i.*, u1.username as username_invitante, u2.username as username_invitato, " +
                    "a.titolo as titolo_appuntamento, a.data as data_appuntamento " +
                    "FROM inviti i " +
                    "JOIN utenti u1 ON i.id_utente_invitante = u1.id " +
                    "JOIN utenti u2 ON i.id_utente_invitato = u2.id " +
                    "JOIN appuntamenti a ON i.id_appuntamento = a.id " +
                    "WHERE i.id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Invito invito = new Invito();
                invito.setId(rs.getInt("id"));
                invito.setIdAppuntamento(rs.getInt("id_appuntamento"));
                invito.setIdUtenteInvitante(rs.getInt("id_utente_invitante"));
                invito.setIdUtenteInvitato(rs.getInt("id_utente_invitato"));
                invito.setStato(rs.getString("stato"));
                invito.setDataInvito(rs.getTimestamp("data_invito"));
                invito.setDataRisposta(rs.getTimestamp("data_risposta"));
                invito.setMessaggio(rs.getString("messaggio"));
                invito.setUsernameInvitante(rs.getString("username_invitante"));
                invito.setUsernameInvitato(rs.getString("username_invitato"));
                invito.setTitoloAppuntamento(rs.getString("titolo_appuntamento"));
                invito.setDataAppuntamento(rs.getTimestamp("data_appuntamento"));
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
            String sql = "UPDATE inviti SET stato = ?, data_risposta = CURRENT_TIMESTAMP WHERE id = ?";
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
            String sql = "DELETE FROM inviti WHERE id = ?";
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
            String sql = "SELECT COUNT(*) FROM inviti WHERE id_appuntamento = ? AND id_utente_invitato = ?";
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
            String sql = "SELECT i.*, u1.username as username_invitante, u2.username as username_invitato " +
                    "FROM inviti i " +
                    "JOIN utenti u1 ON i.id_utente_invitante = u1.id " +
                    "JOIN utenti u2 ON i.id_utente_invitato = u2.id " +
                    "WHERE i.id_appuntamento = ? ORDER BY i.data_invito DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idAppuntamento);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Invito invito = new Invito();
                invito.setId(rs.getInt("id"));
                invito.setIdAppuntamento(rs.getInt("id_appuntamento"));
                invito.setIdUtenteInvitante(rs.getInt("id_utente_invitante"));
                invito.setIdUtenteInvitato(rs.getInt("id_utente_invitato"));
                invito.setStato(rs.getString("stato"));
                invito.setDataInvito(rs.getTimestamp("data_invito"));
                invito.setDataRisposta(rs.getTimestamp("data_risposta"));
                invito.setMessaggio(rs.getString("messaggio"));
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
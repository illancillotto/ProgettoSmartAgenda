package dao;

import model.Notifica;
import java.sql.*;
import java.util.*;

public class NotificaDAO {

    // Inserisce una nuova notifica
    public boolean insert(Notifica notifica) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO notifiche (id_utente, titolo, messaggio, tipo, data_scadenza) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, notifica.getIdUtente());
            ps.setString(2, notifica.getTitolo());
            ps.setString(3, notifica.getMessaggio());
            ps.setString(4, notifica.getTipo());
            if (notifica.getDataScadenza() != null) {
                ps.setTimestamp(5, new Timestamp(notifica.getDataScadenza().getTime()));
            } else {
                ps.setNull(5, Types.TIMESTAMP);
            }
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Trova notifiche per un utente
    public List<Notifica> findByUtente(int idUtente) {
        List<Notifica> lista = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM notifiche WHERE id_utente = ? AND (data_scadenza IS NULL OR data_scadenza > NOW()) ORDER BY data_creazione DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUtente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Notifica notifica = new Notifica();
                notifica.setId(rs.getInt("id"));
                notifica.setIdUtente(rs.getInt("id_utente"));
                notifica.setTitolo(rs.getString("titolo"));
                notifica.setMessaggio(rs.getString("messaggio"));
                notifica.setTipo(rs.getString("tipo"));
                notifica.setLetta(rs.getBoolean("letta"));
                notifica.setDataCreazione(rs.getTimestamp("data_creazione"));
                notifica.setDataScadenza(rs.getTimestamp("data_scadenza"));
                lista.add(notifica);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Trova notifiche non lette per un utente
    public List<Notifica> findNonLette(int idUtente) {
        List<Notifica> lista = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM notifiche WHERE id_utente = ? AND letta = false AND (data_scadenza IS NULL OR data_scadenza > NOW()) ORDER BY data_creazione DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUtente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Notifica notifica = new Notifica();
                notifica.setId(rs.getInt("id"));
                notifica.setIdUtente(rs.getInt("id_utente"));
                notifica.setTitolo(rs.getString("titolo"));
                notifica.setMessaggio(rs.getString("messaggio"));
                notifica.setTipo(rs.getString("tipo"));
                notifica.setLetta(rs.getBoolean("letta"));
                notifica.setDataCreazione(rs.getTimestamp("data_creazione"));
                notifica.setDataScadenza(rs.getTimestamp("data_scadenza"));
                lista.add(notifica);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Conta notifiche non lette
    public int countNonLette(int idUtente) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT COUNT(*) FROM notifiche WHERE id_utente = ? AND letta = false AND (data_scadenza IS NULL OR data_scadenza > NOW())";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUtente);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Marca una notifica come letta
    public boolean markAsRead(int id) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "UPDATE notifiche SET letta = true WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Marca tutte le notifiche di un utente come lette
    public boolean markAllAsRead(int idUtente) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "UPDATE notifiche SET letta = true WHERE id_utente = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUtente);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Elimina una notifica
    public boolean delete(int id) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "DELETE FROM notifiche WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Elimina notifiche scadute
    public boolean deleteScadute() {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "DELETE FROM notifiche WHERE data_scadenza IS NOT NULL AND data_scadenza < NOW()";
            PreparedStatement ps = con.prepareStatement(sql);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Crea notifica di benvenuto per nuovo utente
    public boolean createBenvenuto(int idUtente, String username) {
        Notifica notifica = new Notifica(idUtente,
                "Benvenuto in SmartAgenda!",
                "Ciao " + username + "! Benvenuto in SmartAgenda. Puoi iniziare creando il tuo primo appuntamento.",
                "success");
        return insert(notifica);
    }

    // Crea notifica per nuovo invito
    public boolean createInvito(int idUtente, String usernameInvitante, String titoloAppuntamento) {
        Notifica notifica = new Notifica(idUtente,
                "Nuovo invito ricevuto",
                usernameInvitante + " ti ha invitato all'appuntamento: " + titoloAppuntamento,
                "info");
        return insert(notifica);
    }

    // Crea notifica per promemoria appuntamento
    public boolean createPromemoria(int idUtente, String titoloAppuntamento, java.util.Date dataAppuntamento) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
        Notifica notifica = new Notifica(idUtente,
                "Promemoria appuntamento",
                "Il tuo appuntamento '" + titoloAppuntamento + "' è programmato per " + sdf.format(dataAppuntamento),
                "warning");
        return insert(notifica);
    }

    // Invia notifica a tutti gli utenti (solo admin)
    public boolean sendToAll(String titolo, String messaggio, String tipo) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO notifiche (id_utente, titolo, messaggio, tipo) " +
                    "SELECT id, ?, ?, ? FROM utenti WHERE attivo = true";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, titolo);
            ps.setString(2, messaggio);
            ps.setString(3, tipo);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
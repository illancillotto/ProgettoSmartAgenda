package dao;

import model.Notifica;
import java.sql.*;
import java.util.*;

public class NotificaDAO {

    // Inserisci una nuova notifica
    public boolean insert(Notifica notifica) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO NOTIFICHE (ID_UTENTE, TITOLO, MESSAGGIO, TIPO, DATA_SCADENZA) VALUES (?, ?, ?, ?, ?)";
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

    // Trova tutte le notifiche attive di un utente
    public List<Notifica> findByUtente(int idUtente) {
        List<Notifica> lista = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM NOTIFICHE WHERE ID_UTENTE = ? AND (DATA_SCADENZA IS NULL OR DATA_SCADENZA > NOW()) ORDER BY DATA_CREAZIONE DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUtente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Notifica notifica = new Notifica();
                notifica.setId(rs.getInt("ID"));
                notifica.setIdUtente(rs.getInt("ID_UTENTE"));
                notifica.setTitolo(rs.getString("TITOLO"));
                notifica.setMessaggio(rs.getString("MESSAGGIO"));
                notifica.setTipo(rs.getString("TIPO"));
                notifica.setLetta(rs.getBoolean("LETTA"));
                notifica.setDataCreazione(rs.getTimestamp("DATA_CREAZIONE"));
                notifica.setDataScadenza(rs.getTimestamp("DATA_SCADENZA"));
                lista.add(notifica);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Trova notifiche non lette di un utente
    public List<Notifica> findNonLette(int idUtente) {
        List<Notifica> lista = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM NOTIFICHE WHERE ID_UTENTE = ? AND LETTA = false AND (DATA_SCADENZA IS NULL OR DATA_SCADENZA > NOW()) ORDER BY DATA_CREAZIONE DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUtente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Notifica notifica = new Notifica();
                notifica.setId(rs.getInt("ID"));
                notifica.setIdUtente(rs.getInt("ID_UTENTE"));
                notifica.setTitolo(rs.getString("TITOLO"));
                notifica.setMessaggio(rs.getString("MESSAGGIO"));
                notifica.setTipo(rs.getString("TIPO"));
                notifica.setLetta(rs.getBoolean("LETTA"));
                notifica.setDataCreazione(rs.getTimestamp("DATA_CREAZIONE"));
                notifica.setDataScadenza(rs.getTimestamp("DATA_SCADENZA"));
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
            String sql = "SELECT COUNT(*) FROM NOTIFICHE WHERE ID_UTENTE = ? AND LETTA = false AND (DATA_SCADENZA IS NULL OR DATA_SCADENZA > NOW())";
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

    // Segna una notifica come letta
    public boolean markAsRead(int id) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "UPDATE NOTIFICHE SET LETTA = true WHERE ID = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Segna tutte le notifiche come lette per un utente
    public boolean markAllAsRead(int idUtente) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "UPDATE NOTIFICHE SET LETTA = true WHERE ID_UTENTE = ?";
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
            String sql = "DELETE FROM NOTIFICHE WHERE ID = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Pulisce notifiche scadute
    public void cleanExpiredNotifications() {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "DELETE FROM NOTIFICHE WHERE DATA_SCADENZA IS NOT NULL AND DATA_SCADENZA < NOW()";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Crea notifica di benvenuto per nuovo utente
    public void createBenvenuto(int idUtente, String username) {
        try (Connection con = DBConnection.getConnection()) {
            Notifica notifica = new Notifica();
            notifica.setIdUtente(idUtente);
            notifica.setTitolo("Benvenuto in SmartAgenda!");
            notifica.setMessaggio("Ciao " + username + ", benvenuto nella tua agenda intelligente. " +
                    "Inizia creando il tuo primo appuntamento!");
            notifica.setTipo("info");
            notifica.setLetta(false);

            insert(notifica);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Crea notifica per tutti gli utenti attivi (admin)
    public void createGlobalNotification(String titolo, String messaggio, String tipo) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO NOTIFICHE (ID_UTENTE, TITOLO, MESSAGGIO, TIPO) " +
                    "SELECT ID, ?, ?, ? FROM UTENTI WHERE ATTIVO = true";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, titolo);
            ps.setString(2, messaggio);
            ps.setString(3, tipo);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Crea notifica di promemoria per appuntamento
    public void createPromemoria(int idUtente, String titoloAppuntamento, java.util.Date dataAppuntamento) {
        try {
            Notifica notifica = new Notifica();
            notifica.setIdUtente(idUtente);
            notifica.setTitolo("Promemoria Appuntamento");
            notifica.setMessaggio("Hai un appuntamento '" + titoloAppuntamento + "' programmato per oggi!");
            notifica.setTipo("warning");
            notifica.setLetta(false);
            notifica.setDataScadenza(dataAppuntamento); // La notifica scade dopo l'appuntamento

            insert(notifica);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Crea notifica per invito ricevuto
    public void createInvito(int idUtente, String usernameInvitante, String titoloAppuntamento) {
        try {
            Notifica notifica = new Notifica();
            notifica.setIdUtente(idUtente);
            notifica.setTitolo("Nuovo Invito");
            notifica.setMessaggio("Hai ricevuto un invito da " + usernameInvitante +
                    " per l'appuntamento '" + titoloAppuntamento + "'");
            notifica.setTipo("info");
            notifica.setLetta(false);

            insert(notifica);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Invia notifica a tutti gli utenti (per AdminServlet)
    public boolean sendToAll(String titolo, String messaggio, String tipo) {
        try {
            createGlobalNotification(titolo, messaggio, tipo);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Invia notifica solo agli utenti attivi
    public boolean sendToActiveUsers(String titolo, String messaggio, String tipo) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO NOTIFICHE (ID_UTENTE, TITOLO, MESSAGGIO, TIPO) " +
                    "SELECT ID, ?, ?, ? FROM UTENTI WHERE ATTIVO = true AND RUOLO != 'ospite'";
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

    // Invia notifica solo agli utenti inattivi
    public boolean sendToInactiveUsers(String titolo, String messaggio, String tipo) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO NOTIFICHE (ID_UTENTE, TITOLO, MESSAGGIO, TIPO) " +
                    "SELECT ID, ?, ?, ? FROM UTENTI WHERE ATTIVO = false";
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

    // Trova tutte le notifiche (per admin)
    public List<Notifica> findAll() {
        List<Notifica> lista = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT n.*, u.USERNAME FROM NOTIFICHE n " +
                    "LEFT JOIN UTENTI u ON n.ID_UTENTE = u.ID " +
                    "ORDER BY n.DATA_CREAZIONE DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Notifica notifica = new Notifica();
                notifica.setId(rs.getInt("ID"));
                notifica.setIdUtente(rs.getInt("ID_UTENTE"));
                notifica.setTitolo(rs.getString("TITOLO"));
                notifica.setMessaggio(rs.getString("MESSAGGIO"));
                notifica.setTipo(rs.getString("TIPO"));
                notifica.setLetta(rs.getBoolean("LETTA"));
                notifica.setDataCreazione(rs.getTimestamp("DATA_CREAZIONE"));
                notifica.setDataScadenza(rs.getTimestamp("DATA_SCADENZA"));
                lista.add(notifica);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Trova notifiche per tipo
    public List<Notifica> findByTipo(String tipo) {
        List<Notifica> lista = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM NOTIFICHE WHERE TIPO = ? ORDER BY DATA_CREAZIONE DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, tipo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Notifica notifica = new Notifica();
                notifica.setId(rs.getInt("ID"));
                notifica.setIdUtente(rs.getInt("ID_UTENTE"));
                notifica.setTitolo(rs.getString("TITOLO"));
                notifica.setMessaggio(rs.getString("MESSAGGIO"));
                notifica.setTipo(rs.getString("TIPO"));
                notifica.setLetta(rs.getBoolean("LETTA"));
                notifica.setDataCreazione(rs.getTimestamp("DATA_CREAZIONE"));
                notifica.setDataScadenza(rs.getTimestamp("DATA_SCADENZA"));
                lista.add(notifica);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Conta notifiche per tipo
    public int countByTipo(String tipo) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT COUNT(*) FROM NOTIFICHE WHERE TIPO = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, tipo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Ottieni statistiche notifiche
    public Map<String, Integer> getStatisticheNotifiche() {
        Map<String, Integer> stats = new HashMap<>();
        try (Connection con = DBConnection.getConnection()) {
            // Conta notifiche totali
            String sql1 = "SELECT COUNT(*) FROM NOTIFICHE";
            PreparedStatement ps1 = con.prepareStatement(sql1);
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) {
                stats.put("totali", rs1.getInt(1));
            }

            // Conta notifiche non lette
            String sql2 = "SELECT COUNT(*) FROM NOTIFICHE WHERE LETTA = false";
            PreparedStatement ps2 = con.prepareStatement(sql2);
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next()) {
                stats.put("nonLette", rs2.getInt(1));
            }

            // Conta notifiche per tipo
            String sql3 = "SELECT TIPO, COUNT(*) FROM NOTIFICHE GROUP BY TIPO";
            PreparedStatement ps3 = con.prepareStatement(sql3);
            ResultSet rs3 = ps3.executeQuery();
            while (rs3.next()) {
                stats.put(rs3.getString("TIPO"), rs3.getInt(2));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return stats;
    }
}
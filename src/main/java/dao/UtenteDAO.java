package dao;

import model.Utente;
import java.sql.*;
import java.util.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UtenteDAO {

    // Metodo per hash della password
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Errore nell'hashing della password", e);
        }
    }

    // Metodo di login con password hashata
    public Utente login(String username, String password) {
        Utente utente = null;
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM UTENTI WHERE USERNAME=? AND PASSWORD=? AND ATTIVO=TRUE";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, hashPassword(password));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                utente = new Utente();
                utente.setId(rs.getInt("ID"));
                utente.setUsername(rs.getString("USERNAME"));
                utente.setPassword(rs.getString("PASSWORD"));
                utente.setEmail(rs.getString("EMAIL"));
                utente.setRuolo(rs.getString("RUOLO"));
                utente.setDataRegistrazione(rs.getTimestamp("DATA_REGISTRAZIONE"));
                utente.setUltimoAccesso(rs.getTimestamp("ULTIMO_ACCESSO"));
                utente.setStato(rs.getBoolean("ATTIVO") ? "attivo" : "inattivo");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return utente;
    }

    // Metodo per inserire un nuovo utente con password hashata
    public boolean insert(Utente utente) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO UTENTI (USERNAME, PASSWORD, EMAIL, RUOLO, ATTIVO) VALUES (?, ?, ?, ?, TRUE)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, utente.getUsername());
            ps.setString(2, hashPassword(utente.getPassword()));
            ps.setString(3, utente.getEmail());
            ps.setString(4, utente.getRuolo());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Trova utente per ID
    public Utente findById(int id) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM UTENTI WHERE ID=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Utente utente = new Utente();
                utente.setId(rs.getInt("ID"));
                utente.setUsername(rs.getString("USERNAME"));
                utente.setPassword(rs.getString("PASSWORD"));
                utente.setEmail(rs.getString("EMAIL"));
                utente.setRuolo(rs.getString("RUOLO"));
                utente.setDataRegistrazione(rs.getTimestamp("DATA_REGISTRAZIONE"));
                utente.setUltimoAccesso(rs.getTimestamp("ULTIMO_ACCESSO"));
                utente.setStato(rs.getBoolean("ATTIVO") ? "attivo" : "inattivo");
                return utente;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Trova utente per username
    public Utente findByUsername(String username) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM UTENTI WHERE USERNAME=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Utente utente = new Utente();
                utente.setId(rs.getInt("ID"));
                utente.setUsername(rs.getString("USERNAME"));
                utente.setPassword(rs.getString("PASSWORD"));
                utente.setEmail(rs.getString("EMAIL"));
                utente.setRuolo(rs.getString("RUOLO"));
                utente.setDataRegistrazione(rs.getTimestamp("DATA_REGISTRAZIONE"));
                utente.setUltimoAccesso(rs.getTimestamp("ULTIMO_ACCESSO"));
                utente.setStato(rs.getBoolean("ATTIVO") ? "attivo" : "inattivo");
                return utente;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Trova tutti gli utenti (per amministratore)
    public List<Utente> findAll() {
        List<Utente> lista = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM UTENTI ORDER BY USERNAME";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Utente utente = new Utente();
                utente.setId(rs.getInt("ID"));
                utente.setUsername(rs.getString("USERNAME"));
                utente.setPassword(rs.getString("PASSWORD"));
                utente.setEmail(rs.getString("EMAIL"));
                utente.setRuolo(rs.getString("RUOLO"));
                utente.setDataRegistrazione(rs.getTimestamp("DATA_REGISTRAZIONE"));
                utente.setUltimoAccesso(rs.getTimestamp("ULTIMO_ACCESSO"));
                utente.setStato(rs.getBoolean("ATTIVO") ? "attivo" : "inattivo");
                lista.add(utente);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Trova solo utenti attivi
    public List<Utente> findAttivi() {
        List<Utente> lista = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM UTENTI WHERE ATTIVO=TRUE ORDER BY USERNAME";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Utente utente = new Utente();
                utente.setId(rs.getInt("ID"));
                utente.setUsername(rs.getString("USERNAME"));
                utente.setPassword(rs.getString("PASSWORD"));
                utente.setEmail(rs.getString("EMAIL"));
                utente.setRuolo(rs.getString("RUOLO"));
                utente.setDataRegistrazione(rs.getTimestamp("DATA_REGISTRAZIONE"));
                utente.setUltimoAccesso(rs.getTimestamp("ULTIMO_ACCESSO"));
                utente.setStato("attivo");
                lista.add(utente);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Aggiorna un utente
    public boolean update(Utente utente) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "UPDATE UTENTI SET USERNAME=?, EMAIL=?, RUOLO=? WHERE ID=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, utente.getUsername());
            ps.setString(2, utente.getEmail());
            ps.setString(3, utente.getRuolo());
            ps.setInt(4, utente.getId());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Aggiorna password utente
    public boolean updatePassword(int userId, String newPassword) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "UPDATE UTENTI SET PASSWORD=? WHERE ID=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, hashPassword(newPassword));
            ps.setInt(2, userId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Blocca un utente
    public boolean bloccaUtente(int userId) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "UPDATE UTENTI SET ATTIVO=FALSE WHERE ID=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Sblocca un utente
    public boolean sbloccaUtente(int userId) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "UPDATE UTENTI SET ATTIVO=TRUE WHERE ID=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Aggiorna ultimo accesso
    public boolean updateUltimoAccesso(int userId) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "UPDATE UTENTI SET ULTIMO_ACCESSO=CURRENT_TIMESTAMP WHERE ID=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Elimina un utente
    public boolean delete(int id) {
        try (Connection con = DBConnection.getConnection()) {
            // Prima elimina gli appuntamenti dell'utente
            String sql1 = "DELETE FROM APPUNTAMENTI WHERE ID_UTENTE=?";
            PreparedStatement ps1 = con.prepareStatement(sql1);
            ps1.setInt(1, id);
            ps1.executeUpdate();

            // Poi elimina l'utente
            String sql2 = "DELETE FROM UTENTI WHERE ID=?";
            PreparedStatement ps2 = con.prepareStatement(sql2);
            ps2.setInt(1, id);
            int rows = ps2.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Verifica se username esiste già
    public boolean usernameExists(String username) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT COUNT(*) FROM UTENTI WHERE USERNAME=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Verifica se email esiste già
    public boolean emailExists(String email) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT COUNT(*) FROM UTENTI WHERE EMAIL=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cerca utenti per username o email
    public List<Utente> search(String termine) {
        List<Utente> lista = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM UTENTI WHERE USERNAME LIKE ? OR EMAIL LIKE ? ORDER BY USERNAME";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + termine + "%");
            ps.setString(2, "%" + termine + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Utente utente = new Utente();
                utente.setId(rs.getInt("ID"));
                utente.setUsername(rs.getString("USERNAME"));
                utente.setPassword(rs.getString("PASSWORD"));
                utente.setEmail(rs.getString("EMAIL"));
                utente.setRuolo(rs.getString("RUOLO"));
                utente.setDataRegistrazione(rs.getTimestamp("DATA_REGISTRAZIONE"));
                utente.setUltimoAccesso(rs.getTimestamp("ULTIMO_ACCESSO"));
                utente.setStato(rs.getBoolean("ATTIVO") ? "attivo" : "inattivo");
                lista.add(utente);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Conta utenti per stato
    public int countByStato(String stato) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT COUNT(*) FROM UTENTI WHERE ATTIVO=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setBoolean(1, "attivo".equals(stato));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Ottieni statistiche utenti
    public Map<String, Integer> getStatisticheUtenti() {
        Map<String, Integer> stats = new HashMap<>();
        try (Connection con = DBConnection.getConnection()) {
            // Conta utenti attivi
            String sql1 = "SELECT COUNT(*) FROM UTENTI WHERE ATTIVO=TRUE";
            PreparedStatement ps1 = con.prepareStatement(sql1);
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) {
                stats.put("attivi", rs1.getInt(1));
            }

            // Conta utenti inattivi
            String sql2 = "SELECT COUNT(*) FROM UTENTI WHERE ATTIVO=FALSE";
            PreparedStatement ps2 = con.prepareStatement(sql2);
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next()) {
                stats.put("inattivi", rs2.getInt(1));
            }

            // Conta admin
            String sql3 = "SELECT COUNT(*) FROM UTENTI WHERE RUOLO='admin'";
            PreparedStatement ps3 = con.prepareStatement(sql3);
            ResultSet rs3 = ps3.executeQuery();
            if (rs3.next()) {
                stats.put("admin", rs3.getInt(1));
            }

            // Conta utenti normali
            String sql4 = "SELECT COUNT(*) FROM UTENTI WHERE RUOLO='utente'";
            PreparedStatement ps4 = con.prepareStatement(sql4);
            ResultSet rs4 = ps4.executeQuery();
            if (rs4.next()) {
                stats.put("utenti", rs4.getInt(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return stats;
    }
}

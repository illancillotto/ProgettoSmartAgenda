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
            String sql = "SELECT * FROM utenti WHERE username=? AND password=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, hashPassword(password));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                utente = new Utente();
                utente.setId(rs.getInt("id"));
                utente.setUsername(rs.getString("username"));
                utente.setPassword(rs.getString("password"));
                utente.setEmail(rs.getString("email"));
                utente.setRuolo(rs.getString("ruolo"));
                utente.setDataRegistrazione(rs.getTimestamp("data_registrazione"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return utente;
    }

    // Metodo per inserire un nuovo utente con password hashata
    public boolean insert(Utente utente) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO utenti (username, password, email, ruolo) VALUES (?, ?, ?, ?)";
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
            String sql = "SELECT * FROM utenti WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Utente utente = new Utente();
                utente.setId(rs.getInt("id"));
                utente.setUsername(rs.getString("username"));
                utente.setPassword(rs.getString("password"));
                utente.setEmail(rs.getString("email"));
                utente.setRuolo(rs.getString("ruolo"));
                utente.setDataRegistrazione(rs.getTimestamp("data_registrazione"));
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
            String sql = "SELECT * FROM utenti WHERE username=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Utente utente = new Utente();
                utente.setId(rs.getInt("id"));
                utente.setUsername(rs.getString("username"));
                utente.setPassword(rs.getString("password"));
                utente.setEmail(rs.getString("email"));
                utente.setRuolo(rs.getString("ruolo"));
                utente.setDataRegistrazione(rs.getTimestamp("data_registrazione"));
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
            String sql = "SELECT * FROM utenti ORDER BY username";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Utente utente = new Utente();
                utente.setId(rs.getInt("id"));
                utente.setUsername(rs.getString("username"));
                utente.setPassword(rs.getString("password"));
                utente.setEmail(rs.getString("email"));
                utente.setRuolo(rs.getString("ruolo"));
                utente.setDataRegistrazione(rs.getTimestamp("data_registrazione"));
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
            String sql = "UPDATE utenti SET username=?, email=?, ruolo=? WHERE id=?";
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
            String sql = "UPDATE utenti SET password=? WHERE id=?";
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

    // Elimina un utente
    public boolean delete(int id) {
        try (Connection con = DBConnection.getConnection()) {
            // Prima elimina gli appuntamenti dell'utente
            String sql1 = "DELETE FROM appuntamenti WHERE id_utente=?";
            PreparedStatement ps1 = con.prepareStatement(sql1);
            ps1.setInt(1, id);
            ps1.executeUpdate();

            // Poi elimina l'utente
            String sql2 = "DELETE FROM utenti WHERE id=?";
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
            String sql = "SELECT COUNT(*) FROM utenti WHERE username=?";
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
            String sql = "SELECT COUNT(*) FROM utenti WHERE email=?";
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
            String sql = "SELECT * FROM utenti WHERE username LIKE ? OR email LIKE ? ORDER BY username";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + termine + "%");
            ps.setString(2, "%" + termine + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Utente utente = new Utente();
                utente.setId(rs.getInt("id"));
                utente.setUsername(rs.getString("username"));
                utente.setPassword(rs.getString("password"));
                utente.setEmail(rs.getString("email"));
                utente.setRuolo(rs.getString("ruolo"));
                utente.setDataRegistrazione(rs.getTimestamp("data_registrazione"));
                lista.add(utente);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}

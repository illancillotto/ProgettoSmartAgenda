package dao;

import model.Appuntamento;
import java.sql.*;
import java.util.*;

public class AppuntamentoDAO {

    // Restituisce la lista degli appuntamenti di un dato utente
    public List<Appuntamento> findByUtente(int idUtente) {
        List<Appuntamento> lista = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM appuntamenti WHERE id_utente=? ORDER BY data ASC";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUtente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appuntamento app = new Appuntamento();
                app.setId(rs.getInt("id"));
                app.setTitolo(rs.getString("titolo"));
                app.setDescrizione(rs.getString("descrizione"));
                app.setDataOra(rs.getTimestamp("data"));
                app.setIdUtente(rs.getInt("id_utente"));
                app.setCondiviso(rs.getBoolean("condiviso"));
                lista.add(app);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Inserisci un nuovo appuntamento
    public boolean insert(Appuntamento app) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO appuntamenti (titolo, descrizione, data, id_utente, condiviso) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, app.getTitolo());
            ps.setString(2, app.getDescrizione());
            ps.setTimestamp(3, new Timestamp(app.getDataOra().getTime()));
            ps.setInt(4, app.getIdUtente());
            ps.setBoolean(5, app.isCondiviso());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Trova appuntamento per ID
    public Appuntamento findById(int id) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM appuntamenti WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Appuntamento app = new Appuntamento();
                app.setId(rs.getInt("id"));
                app.setTitolo(rs.getString("titolo"));
                app.setDescrizione(rs.getString("descrizione"));
                app.setDataOra(rs.getTimestamp("data"));
                app.setIdUtente(rs.getInt("id_utente"));
                app.setCondiviso(rs.getBoolean("condiviso"));
                return app;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Aggiorna un appuntamento
    public boolean update(Appuntamento app) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "UPDATE appuntamenti SET titolo=?, descrizione=?, data=?, condiviso=? WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, app.getTitolo());
            ps.setString(2, app.getDescrizione());
            ps.setTimestamp(3, new Timestamp(app.getDataOra().getTime()));
            ps.setBoolean(4, app.isCondiviso());
            ps.setInt(5, app.getId());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Elimina un appuntamento
    public boolean delete(int id) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "DELETE FROM appuntamenti WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Trova tutti gli appuntamenti condivisi
    public List<Appuntamento> findCondivisi() {
        List<Appuntamento> lista = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT a.*, u.username FROM appuntamenti a " +
                    "JOIN utenti u ON a.id_utente = u.id " +
                    "WHERE a.condiviso = true ORDER BY a.data ASC";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appuntamento app = new Appuntamento();
                app.setId(rs.getInt("id"));
                app.setTitolo(rs.getString("titolo"));
                app.setDescrizione(rs.getString("descrizione"));
                app.setDataOra(rs.getTimestamp("data"));
                app.setIdUtente(rs.getInt("id_utente"));
                app.setUsername(rs.getString("username"));
                app.setCondiviso(rs.getBoolean("condiviso"));
                lista.add(app);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Trova appuntamenti per intervallo di date
    public List<Appuntamento> findByDateRange(int idUtente, java.util.Date dataInizio, java.util.Date dataFine) {
        List<Appuntamento> lista = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM appuntamenti WHERE id_utente=? AND data BETWEEN ? AND ? ORDER BY data ASC";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUtente);
            ps.setTimestamp(2, new Timestamp(dataInizio.getTime()));
            ps.setTimestamp(3, new Timestamp(dataFine.getTime()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appuntamento app = new Appuntamento();
                app.setId(rs.getInt("id"));
                app.setTitolo(rs.getString("titolo"));
                app.setDescrizione(rs.getString("descrizione"));
                app.setDataOra(rs.getTimestamp("data"));
                app.setIdUtente(rs.getInt("id_utente"));
                app.setCondiviso(rs.getBoolean("condiviso"));
                lista.add(app);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Cerca appuntamenti per titolo o descrizione
    public List<Appuntamento> search(int idUtente, String termine) {
        List<Appuntamento> lista = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM appuntamenti WHERE id_utente=? AND " +
                    "(titolo LIKE ? OR descrizione LIKE ?) ORDER BY data ASC";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUtente);
            ps.setString(2, "%" + termine + "%");
            ps.setString(3, "%" + termine + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appuntamento app = new Appuntamento();
                app.setId(rs.getInt("id"));
                app.setTitolo(rs.getString("titolo"));
                app.setDescrizione(rs.getString("descrizione"));
                app.setDataOra(rs.getTimestamp("data"));
                app.setIdUtente(rs.getInt("id_utente"));
                app.setCondiviso(rs.getBoolean("condiviso"));
                lista.add(app);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}

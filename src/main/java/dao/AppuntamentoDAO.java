package dao;

import model.Appuntamento;
import java.sql.*;
import java.util.*;

public class AppuntamentoDAO {

    // Restituisce la lista degli appuntamenti di un dato utente
    public List<Appuntamento> findByUtente(int idUtente) {
        List<Appuntamento> lista = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM APPUNTAMENTI WHERE ID_UTENTE=? ORDER BY DATA ASC";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUtente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appuntamento app = new Appuntamento();
                app.setId(rs.getInt("ID"));
                app.setTitolo(rs.getString("TITOLO"));
                app.setDescrizione(rs.getString("DESCRIZIONE"));
                app.setDataOra(rs.getTimestamp("DATA"));
                app.setIdUtente(rs.getInt("ID_UTENTE"));
                app.setCondiviso(rs.getBoolean("CONDIVISO"));
                app.setIdCategoria(rs.getInt("ID_CATEGORIA"));
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
            String sql = "INSERT INTO APPUNTAMENTI (TITOLO, DESCRIZIONE, DATA, ID_UTENTE, CONDIVISO, ID_CATEGORIA) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, app.getTitolo());
            ps.setString(2, app.getDescrizione());
            ps.setTimestamp(3, new Timestamp(app.getDataOra().getTime()));
            ps.setInt(4, app.getIdUtente());
            ps.setBoolean(5, app.isCondiviso());
            if (app.getIdCategoria() > 0) {
                ps.setInt(6, app.getIdCategoria());
            } else {
                ps.setNull(6, java.sql.Types.INTEGER);
            }
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
            String sql = "SELECT * FROM APPUNTAMENTI WHERE ID=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Appuntamento app = new Appuntamento();
                app.setId(rs.getInt("ID"));
                app.setTitolo(rs.getString("TITOLO"));
                app.setDescrizione(rs.getString("DESCRIZIONE"));
                app.setDataOra(rs.getTimestamp("DATA"));
                app.setIdUtente(rs.getInt("ID_UTENTE"));
                app.setCondiviso(rs.getBoolean("CONDIVISO"));
                app.setIdCategoria(rs.getInt("ID_CATEGORIA"));
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
            String sql = "UPDATE APPUNTAMENTI SET TITOLO=?, DESCRIZIONE=?, DATA=?, CONDIVISO=?, ID_CATEGORIA=? WHERE ID=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, app.getTitolo());
            ps.setString(2, app.getDescrizione());
            ps.setTimestamp(3, new Timestamp(app.getDataOra().getTime()));
            ps.setBoolean(4, app.isCondiviso());
            if (app.getIdCategoria() > 0) {
                ps.setInt(5, app.getIdCategoria());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }
            ps.setInt(6, app.getId());
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
            String sql = "DELETE FROM APPUNTAMENTI WHERE ID=?";
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
            String sql = "SELECT a.*, u.USERNAME FROM APPUNTAMENTI a " +
                    "JOIN UTENTI u ON a.ID_UTENTE = u.ID " +
                    "WHERE a.CONDIVISO = true ORDER BY a.DATA ASC";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appuntamento app = new Appuntamento();
                app.setId(rs.getInt("ID"));
                app.setTitolo(rs.getString("TITOLO"));
                app.setDescrizione(rs.getString("DESCRIZIONE"));
                app.setDataOra(rs.getTimestamp("DATA"));
                app.setIdUtente(rs.getInt("ID_UTENTE"));
                app.setUsername(rs.getString("USERNAME"));
                app.setCondiviso(rs.getBoolean("CONDIVISO"));
                app.setIdCategoria(rs.getInt("ID_CATEGORIA"));
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
            String sql = "SELECT * FROM APPUNTAMENTI WHERE ID_UTENTE=? AND DATA BETWEEN ? AND ? ORDER BY DATA ASC";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUtente);
            ps.setTimestamp(2, new Timestamp(dataInizio.getTime()));
            ps.setTimestamp(3, new Timestamp(dataFine.getTime()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appuntamento app = new Appuntamento();
                app.setId(rs.getInt("ID"));
                app.setTitolo(rs.getString("TITOLO"));
                app.setDescrizione(rs.getString("DESCRIZIONE"));
                app.setDataOra(rs.getTimestamp("DATA"));
                app.setIdUtente(rs.getInt("ID_UTENTE"));
                app.setCondiviso(rs.getBoolean("CONDIVISO"));
                app.setIdCategoria(rs.getInt("ID_CATEGORIA"));
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
            String sql = "SELECT * FROM APPUNTAMENTI WHERE ID_UTENTE=? AND " +
                    "(TITOLO LIKE ? OR DESCRIZIONE LIKE ?) ORDER BY DATA ASC";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUtente);
            ps.setString(2, "%" + termine + "%");
            ps.setString(3, "%" + termine + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appuntamento app = new Appuntamento();
                app.setId(rs.getInt("ID"));
                app.setTitolo(rs.getString("TITOLO"));
                app.setDescrizione(rs.getString("DESCRIZIONE"));
                app.setDataOra(rs.getTimestamp("DATA"));
                app.setIdUtente(rs.getInt("ID_UTENTE"));
                app.setCondiviso(rs.getBoolean("CONDIVISO"));
                app.setIdCategoria(rs.getInt("ID_CATEGORIA"));
                lista.add(app);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Trova tutti gli appuntamenti per admin (con username)
    public List<Appuntamento> findAllForAdmin() {
        List<Appuntamento> lista = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT a.*, u.USERNAME FROM APPUNTAMENTI a " +
                    "JOIN UTENTI u ON a.ID_UTENTE = u.ID " +
                    "ORDER BY a.DATA ASC";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appuntamento app = new Appuntamento();
                app.setId(rs.getInt("ID"));
                app.setTitolo(rs.getString("TITOLO"));
                app.setDescrizione(rs.getString("DESCRIZIONE"));
                app.setDataOra(rs.getTimestamp("DATA"));
                app.setIdUtente(rs.getInt("ID_UTENTE"));
                app.setUsername(rs.getString("USERNAME"));
                app.setCondiviso(rs.getBoolean("CONDIVISO"));
                app.setIdCategoria(rs.getInt("ID_CATEGORIA"));
                lista.add(app);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}

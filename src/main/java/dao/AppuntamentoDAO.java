package dao;

import model.Appuntamento;
import java.sql.*;
import java.util.*;

public class AppuntamentoDAO {

    // Restituisce la lista degli appuntamenti di un dato utente
    public List<Appuntamento> findByUtente(int idUtente) {
        List<Appuntamento> lista = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT a.*, c.NOME as NOME_CATEGORIA, c.COLORE as COLORE_CATEGORIA " +
                    "FROM APPUNTAMENTI a " +
                    "LEFT JOIN CATEGORIE c ON a.ID_CATEGORIA = c.ID " +
                    "WHERE a.ID_UTENTE=? ORDER BY a.DATA ASC";
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
                app.setNomeCategoria(rs.getString("NOME_CATEGORIA"));
                app.setColoreCategoria(rs.getString("COLORE_CATEGORIA"));
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

    // Inserisci un nuovo appuntamento per un utente specifico (utile per admin)
    public boolean insertForUser(Appuntamento app, int targetUserId) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO APPUNTAMENTI (TITOLO, DESCRIZIONE, DATA, ID_UTENTE, CONDIVISO, ID_CATEGORIA) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, app.getTitolo());
            ps.setString(2, app.getDescrizione());
            ps.setTimestamp(3, new Timestamp(app.getDataOra().getTime()));
            ps.setInt(4, targetUserId); // Usa l'ID utente specificato
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
            String sql = "SELECT a.*, c.NOME as NOME_CATEGORIA, c.COLORE as COLORE_CATEGORIA " +
                    "FROM APPUNTAMENTI a " +
                    "LEFT JOIN CATEGORIE c ON a.ID_CATEGORIA = c.ID " +
                    "WHERE a.ID=?";
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
                app.setNomeCategoria(rs.getString("NOME_CATEGORIA"));
                app.setColoreCategoria(rs.getString("COLORE_CATEGORIA"));
                return app;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Aggiorna un appuntamento eliminando il vecchio e inserendo il nuovo
    public boolean updateOrReplace(Appuntamento app) {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false); // Inizia transazione

            // Step 1: Elimina il vecchio appuntamento
            String deleteSql = "DELETE FROM APPUNTAMENTI WHERE ID=?";
            PreparedStatement deletePs = con.prepareStatement(deleteSql);
            deletePs.setInt(1, app.getId());
            int deletedRows = deletePs.executeUpdate();

            System.out.println("DEBUG: Righe eliminate: " + deletedRows);

            if (deletedRows == 0) {
                con.rollback();
                return false; // Appuntamento non trovato
            }

            // Step 2: Inserisci il nuovo appuntamento con i dati aggiornati
            String insertSql = "INSERT INTO APPUNTAMENTI (TITOLO, DESCRIZIONE, DATA, ID_UTENTE, CONDIVISO, ID_CATEGORIA) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement insertPs = con.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS);
            insertPs.setString(1, app.getTitolo());
            insertPs.setString(2, app.getDescrizione());
            insertPs.setTimestamp(3, new Timestamp(app.getDataOra().getTime()));
            insertPs.setInt(4, app.getIdUtente());
            insertPs.setBoolean(5, app.isCondiviso());

            if (app.getIdCategoria() > 0) {
                insertPs.setInt(6, app.getIdCategoria());
            } else {
                insertPs.setNull(6, java.sql.Types.INTEGER);
            }

            int insertedRows = insertPs.executeUpdate();
            System.out.println("DEBUG: Righe inserite: " + insertedRows);

            if (insertedRows > 0) {
                // Ottieni il nuovo ID generato
                ResultSet generatedKeys = insertPs.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int newId = generatedKeys.getInt(1);
                    app.setId(newId); // Aggiorna l'ID nell'oggetto
                    System.out.println("DEBUG: Nuovo ID generato: " + newId);
                }

                con.commit(); // Conferma transazione
                return true;
            } else {
                con.rollback(); // Annulla transazione
                return false;
            }

        } catch (Exception e) {
            try {
                if (con != null) {
                    con.rollback(); // Annulla transazione in caso di errore
                }
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true); // Ripristina auto-commit
                    con.close();
                }
            } catch (Exception closeEx) {
                closeEx.printStackTrace();
            }
        }
    }

    // Mantieni il vecchio metodo update per compatibilità (ora deprecato)
    @Deprecated
    public boolean update(Appuntamento app) {
        return updateOrReplace(app);
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
            String sql = "SELECT a.*, u.USERNAME, c.NOME as NOME_CATEGORIA, c.COLORE as COLORE_CATEGORIA " +
                    "FROM APPUNTAMENTI a " +
                    "JOIN UTENTI u ON a.ID_UTENTE = u.ID " +
                    "LEFT JOIN CATEGORIE c ON a.ID_CATEGORIA = c.ID " +
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
                app.setNomeCategoria(rs.getString("NOME_CATEGORIA"));
                app.setColoreCategoria(rs.getString("COLORE_CATEGORIA"));
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
            String sql = "SELECT a.*, c.NOME as NOME_CATEGORIA, c.COLORE as COLORE_CATEGORIA " +
                    "FROM APPUNTAMENTI a " +
                    "LEFT JOIN CATEGORIE c ON a.ID_CATEGORIA = c.ID " +
                    "WHERE a.ID_UTENTE=? AND (a.TITOLO LIKE ? OR a.DESCRIZIONE LIKE ?) " +
                    "ORDER BY a.DATA ASC";
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
                app.setNomeCategoria(rs.getString("NOME_CATEGORIA"));
                app.setColoreCategoria(rs.getString("COLORE_CATEGORIA"));
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
            String sql = "SELECT a.*, u.USERNAME, c.NOME as NOME_CATEGORIA, c.COLORE as COLORE_CATEGORIA " +
                    "FROM APPUNTAMENTI a " +
                    "JOIN UTENTI u ON a.ID_UTENTE = u.ID " +
                    "LEFT JOIN CATEGORIE c ON a.ID_CATEGORIA = c.ID " +
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
                app.setNomeCategoria(rs.getString("NOME_CATEGORIA"));
                app.setColoreCategoria(rs.getString("COLORE_CATEGORIA"));
                lista.add(app);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Trova appuntamenti per più utenti (utile per admin)
    public List<Appuntamento> findByMultipleUsers(List<Integer> userIds) {
        List<Appuntamento> lista = new ArrayList<>();
        if (userIds == null || userIds.isEmpty()) {
            return lista;
        }

        try (Connection con = DBConnection.getConnection()) {
            StringBuilder sql = new StringBuilder("SELECT a.*, u.USERNAME FROM APPUNTAMENTI a ");
            sql.append("JOIN UTENTI u ON a.ID_UTENTE = u.ID ");
            sql.append("WHERE a.ID_UTENTE IN (");
            for (int i = 0; i < userIds.size(); i++) {
                if (i > 0)
                    sql.append(",");
                sql.append("?");
            }
            sql.append(") ORDER BY a.DATA ASC");

            PreparedStatement ps = con.prepareStatement(sql.toString());
            for (int i = 0; i < userIds.size(); i++) {
                ps.setInt(i + 1, userIds.get(i));
            }

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

    // Conta appuntamenti per utente
    public int countByUser(int idUtente) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT COUNT(*) FROM APPUNTAMENTI WHERE ID_UTENTE=?";
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

    // Verifica se un utente può modificare un appuntamento
    public boolean canUserModifyAppointment(int appointmentId, int userId, boolean isAdmin) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT ID_UTENTE FROM APPUNTAMENTI WHERE ID=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, appointmentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int ownerId = rs.getInt("ID_UTENTE");
                return isAdmin || ownerId == userId;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Trasferisci appuntamenti da un utente a un altro (utile per admin)
    public boolean transferAppointments(int fromUserId, int toUserId) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "UPDATE APPUNTAMENTI SET ID_UTENTE=? WHERE ID_UTENTE=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, toUserId);
            ps.setInt(2, fromUserId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

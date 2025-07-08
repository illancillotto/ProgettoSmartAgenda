package dao;

import model.Categoria;
import java.sql.*;
import java.util.*;

public class CategoriaDAO {

    // Inserisci una nuova categoria
    public boolean insert(Categoria categoria) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO CATEGORIE (NOME, COLORE, ID_UTENTE) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, categoria.getNome());
            ps.setString(2, categoria.getColore());
            ps.setInt(3, categoria.getIdUtente());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Trova tutte le categorie di un utente
    public List<Categoria> findByUtente(int idUtente) {
        List<Categoria> lista = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM CATEGORIE WHERE ID_UTENTE = ? ORDER BY NOME";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUtente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setId(rs.getInt("ID"));
                categoria.setNome(rs.getString("NOME"));
                categoria.setColore(rs.getString("COLORE"));
                categoria.setIdUtente(rs.getInt("ID_UTENTE"));
                lista.add(categoria);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Trova tutte le categorie (per admin)
    public List<Categoria> findAll() {
        List<Categoria> lista = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT c.*, u.USERNAME FROM CATEGORIE c " +
                    "JOIN UTENTI u ON c.ID_UTENTE = u.ID " +
                    "ORDER BY u.USERNAME, c.NOME";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setId(rs.getInt("ID"));
                categoria.setNome(rs.getString("NOME"));
                categoria.setColore(rs.getString("COLORE"));
                categoria.setIdUtente(rs.getInt("ID_UTENTE"));
                lista.add(categoria);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Trova categoria per ID
    public Categoria findById(int id) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM CATEGORIE WHERE ID = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setId(rs.getInt("ID"));
                categoria.setNome(rs.getString("NOME"));
                categoria.setColore(rs.getString("COLORE"));
                categoria.setIdUtente(rs.getInt("ID_UTENTE"));
                return categoria;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Aggiorna una categoria
    public boolean update(Categoria categoria) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "UPDATE CATEGORIE SET NOME = ?, COLORE = ? WHERE ID = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, categoria.getNome());
            ps.setString(2, categoria.getColore());
            ps.setInt(3, categoria.getId());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Elimina una categoria
    public boolean delete(int id) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "DELETE FROM CATEGORIE WHERE ID = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Verifica se il nome categoria esiste già per l'utente
    public boolean nomeExists(String nome, int idUtente) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT COUNT(*) FROM CATEGORIE WHERE NOME = ? AND ID_UTENTE = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nome);
            ps.setInt(2, idUtente);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Crea categorie di default per nuovo utente
    public void createDefaultCategories(int idUtente) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO CATEGORIE (NOME, COLORE, ID_UTENTE) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);

            // Categoria "Lavoro"
            ps.setString(1, "Lavoro");
            ps.setString(2, "#007bff");
            ps.setInt(3, idUtente);
            ps.executeUpdate();

            // Categoria "Personale"
            ps.setString(1, "Personale");
            ps.setString(2, "#28a745");
            ps.setInt(3, idUtente);
            ps.executeUpdate();

            // Categoria "Famiglia"
            ps.setString(1, "Famiglia");
            ps.setString(2, "#dc3545");
            ps.setInt(3, idUtente);
            ps.executeUpdate();

            // Categoria "Salute"
            ps.setString(1, "Salute");
            ps.setString(2, "#ffc107");
            ps.setInt(3, idUtente);
            ps.executeUpdate();

            // Categoria "Tempo libero"
            ps.setString(1, "Tempo libero");
            ps.setString(2, "#6f42c1");
            ps.setInt(3, idUtente);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Verifica se la categoria è utilizzata in appuntamenti
    public boolean isUsedInAppuntamenti(int idCategoria) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT COUNT(*) FROM APPUNTAMENTI WHERE ID_CATEGORIA = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idCategoria);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Verifica se una categoria appartiene a un utente specifico
    public boolean belongsToUser(int idCategoria, int idUtente) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT COUNT(*) FROM CATEGORIE WHERE ID = ? AND ID_UTENTE = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idCategoria);
            ps.setInt(2, idUtente);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
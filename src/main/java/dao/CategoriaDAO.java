package dao;

import model.Categoria;
import java.sql.*;
import java.util.*;

public class CategoriaDAO {

    // Inserisce una nuova categoria
    public boolean insert(Categoria categoria) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO categorie (nome, colore, id_utente) VALUES (?, ?, ?)";
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

    // Trova categorie per un utente
    public List<Categoria> findByUtente(int idUtente) {
        List<Categoria> lista = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM categorie WHERE id_utente = ? ORDER BY nome";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUtente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setId(rs.getInt("id"));
                categoria.setNome(rs.getString("nome"));
                categoria.setColore(rs.getString("colore"));
                categoria.setIdUtente(rs.getInt("id_utente"));
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
            String sql = "SELECT * FROM categorie WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setId(rs.getInt("id"));
                categoria.setNome(rs.getString("nome"));
                categoria.setColore(rs.getString("colore"));
                categoria.setIdUtente(rs.getInt("id_utente"));
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
            String sql = "UPDATE categorie SET nome = ?, colore = ? WHERE id = ?";
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
            String sql = "DELETE FROM categorie WHERE id = ?";
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
    public boolean existsNome(String nome, int idUtente) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT COUNT(*) FROM categorie WHERE nome = ? AND id_utente = ?";
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

    // Crea categorie di default per un nuovo utente
    public boolean createDefaultCategories(int idUtente) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO categorie (nome, colore, id_utente) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);

            // Lavoro - Rosso
            ps.setString(1, "Lavoro");
            ps.setString(2, "#dc3545");
            ps.setInt(3, idUtente);
            ps.executeUpdate();

            // Personale - Verde
            ps.setString(1, "Personale");
            ps.setString(2, "#28a745");
            ps.setInt(3, idUtente);
            ps.executeUpdate();

            // Studio - Giallo
            ps.setString(1, "Studio");
            ps.setString(2, "#ffc107");
            ps.setInt(3, idUtente);
            ps.executeUpdate();

            // Salute - Arancione
            ps.setString(1, "Salute");
            ps.setString(2, "#fd7e14");
            ps.setInt(3, idUtente);
            ps.executeUpdate();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Conta appuntamenti per categoria
    public int countAppuntamenti(int idCategoria) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT COUNT(*) FROM appuntamenti WHERE id_categoria = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idCategoria);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
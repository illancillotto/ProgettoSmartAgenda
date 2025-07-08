package model;

public class Categoria {
    private int id;
    private String nome;
    private String colore;
    private int idUtente;

    public Categoria() {
    }

    public Categoria(String nome, String colore, int idUtente) {
        this.nome = nome;
        this.colore = colore;
        this.idUtente = idUtente;
    }

    // Getter e Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getColore() {
        return colore;
    }

    public void setColore(String colore) {
        this.colore = colore;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }
}
package model;

import java.util.Date;

public class Notifica {
    private int id;
    private int idUtente;
    private String titolo;
    private String messaggio;
    private String tipo; // info, warning, error, success
    private boolean letta;
    private Date dataCreazione;
    private Date dataScadenza;

    public Notifica() {
    }

    public Notifica(int idUtente, String titolo, String messaggio, String tipo) {
        this.idUtente = idUtente;
        this.titolo = titolo;
        this.messaggio = messaggio;
        this.tipo = tipo;
        this.letta = false;
        this.dataCreazione = new Date();
    }

    // Getter e Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isLetta() {
        return letta;
    }

    public void setLetta(boolean letta) {
        this.letta = letta;
    }

    public Date getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(Date dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public Date getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(Date dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    // Metodi di utilità
    public boolean isScaduta() {
        return dataScadenza != null && dataScadenza.before(new Date());
    }

    public String getCssClass() {
        switch (tipo) {
            case "success":
                return "alert-success";
            case "warning":
                return "alert-warning";
            case "error":
                return "alert-danger";
            default:
                return "alert-info";
        }
    }
}
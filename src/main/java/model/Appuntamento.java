package model;

import java.util.Date;

public class Appuntamento {
    private int id;
    private String titolo;
    private String descrizione;
    private Date dataOra; // Data e ora insieme (java.util.Date oppure java.sql.Timestamp)
    private int idUtente;
    private String username; // NON serve nel DB, ma comodo nel model Java per join!
    private boolean condiviso;
    private Date dataCreazione;
    private Date dataModifica;
    private int idCategoria;
    private String nomeCategoria; // Campo per join con categorie
    private String coloreCategoria; // Campo per join con categorie

    public Appuntamento() {
    }

    public Appuntamento(String titolo, String descrizione, Date dataOra, int idUtente, boolean condiviso) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.dataOra = dataOra;
        this.idUtente = idUtente;
        this.condiviso = condiviso;
        this.dataCreazione = new Date();
        this.dataModifica = new Date();
    }

    // Getter e Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Date getDataOra() {
        return dataOra;
    }

    public void setDataOra(Date dataOra) {
        this.dataOra = dataOra;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isCondiviso() {
        return condiviso;
    }

    public void setCondiviso(boolean condiviso) {
        this.condiviso = condiviso;
    }

    public Date getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(Date dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public Date getDataModifica() {
        return dataModifica;
    }

    public void setDataModifica(Date dataModifica) {
        this.dataModifica = dataModifica;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    public String getColoreCategoria() {
        return coloreCategoria;
    }

    public void setColoreCategoria(String coloreCategoria) {
        this.coloreCategoria = coloreCategoria;
    }

    // Metodi di utilità
    public boolean isPassato() {
        return dataOra != null && dataOra.before(new Date());
    }

    public boolean isOggi() {
        if (dataOra == null)
            return false;
        Date oggi = new Date();
        return isSameDay(dataOra, oggi);
    }

    public boolean isDomani() {
        if (dataOra == null)
            return false;
        Date domani = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
        return isSameDay(dataOra, domani);
    }

    private boolean isSameDay(Date date1, Date date2) {
        java.util.Calendar cal1 = java.util.Calendar.getInstance();
        java.util.Calendar cal2 = java.util.Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR) &&
                cal1.get(java.util.Calendar.DAY_OF_YEAR) == cal2.get(java.util.Calendar.DAY_OF_YEAR);
    }

    public String getDataFormatted() {
        if (dataOra == null)
            return "";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(dataOra);
    }

    public String getDataFormattedShort() {
        if (dataOra == null)
            return "";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM HH:mm");
        return sdf.format(dataOra);
    }
}

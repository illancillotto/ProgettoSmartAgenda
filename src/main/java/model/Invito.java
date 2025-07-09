package model;

import java.util.Date;

public class Invito {
    private int id;
    private int idAppuntamento;
    private int idUtenteInvitante;
    private int idUtenteInvitato;
    private String stato; // Valori possibili: "in_attesa", "accettato", "rifiutato"
    private Date dataInvito;
    private Date dataRisposta;
    private String messaggio;

    // Campi aggiuntivi per le join
    private String usernameInvitante;
    private String usernameInvitato;
    private String titoloAppuntamento;
    private Date dataAppuntamento;
    private String descrizione; // Descrizione dell'appuntamento (join con tabella appuntamenti)

    public Invito() {
    }

    public Invito(int idAppuntamento, int idUtenteInvitante, int idUtenteInvitato, String messaggio) {
        this.idAppuntamento = idAppuntamento;
        this.idUtenteInvitante = idUtenteInvitante;
        this.idUtenteInvitato = idUtenteInvitato;
        this.messaggio = messaggio;
        this.stato = "in_attesa";
        this.dataInvito = new Date();
    }

    // Getter e Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdAppuntamento() {
        return idAppuntamento;
    }

    public void setIdAppuntamento(int idAppuntamento) {
        this.idAppuntamento = idAppuntamento;
    }

    public int getIdUtenteInvitante() {
        return idUtenteInvitante;
    }

    public void setIdUtenteInvitante(int idUtenteInvitante) {
        this.idUtenteInvitante = idUtenteInvitante;
    }

    public int getIdUtenteInvitato() {
        return idUtenteInvitato;
    }

    public void setIdUtenteInvitato(int idUtenteInvitato) {
        this.idUtenteInvitato = idUtenteInvitato;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public Date getDataInvito() {
        return dataInvito;
    }

    public void setDataInvito(Date dataInvito) {
        this.dataInvito = dataInvito;
    }

    public Date getDataRisposta() {
        return dataRisposta;
    }

    public void setDataRisposta(Date dataRisposta) {
        this.dataRisposta = dataRisposta;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

    public String getUsernameInvitante() {
        return usernameInvitante;
    }

    public void setUsernameInvitante(String usernameInvitante) {
        this.usernameInvitante = usernameInvitante;
    }

    public String getUsernameInvitato() {
        return usernameInvitato;
    }

    public void setUsernameInvitato(String usernameInvitato) {
        this.usernameInvitato = usernameInvitato;
    }

    public String getTitoloAppuntamento() {
        return titoloAppuntamento;
    }

    public void setTitoloAppuntamento(String titoloAppuntamento) {
        this.titoloAppuntamento = titoloAppuntamento;
    }

    public Date getDataAppuntamento() {
        return dataAppuntamento;
    }

    public void setDataAppuntamento(Date dataAppuntamento) {
        this.dataAppuntamento = dataAppuntamento;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    // Metodi di utilità per compatibilità con la JSP
    public String getUsernameMittente() {
        return usernameInvitante;
    }

    public String getUsernameDestinatario() {
        return usernameInvitato;
    }

    // Metodi di utilità
    public boolean isPending() {
        return stato != null && (
            "in_attesa".equalsIgnoreCase(stato.trim())
            || "pending".equalsIgnoreCase(stato.trim())
        );
    }

    public boolean isAccepted() {
        return "accettato".equals(stato);
    }

    public boolean isDeclined() {
        return "rifiutato".equals(stato);
    }
}
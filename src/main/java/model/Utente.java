package model;

import java.util.Date;

public class Utente {
	private int id;
	private String username;
	private String password;
	private String email;
	private String ruolo;
	private Date dataRegistrazione;
	private String stato; // "attivo", "inattivo", "bloccato"
	private Date ultimoAccesso;

	// Costruttore vuoto e completo
	public Utente() {
	}

	public Utente(int id, String username, String password, String email, String ruolo) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.ruolo = ruolo;
		this.stato = "attivo"; // Default
	}

	// Getter e Setter
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRuolo() {
		return ruolo;
	}

	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}

	public Date getDataRegistrazione() {
		return dataRegistrazione;
	}

	public void setDataRegistrazione(Date dataRegistrazione) {
		this.dataRegistrazione = dataRegistrazione;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public Date getUltimoAccesso() {
		return ultimoAccesso;
	}

	public void setUltimoAccesso(Date ultimoAccesso) {
		this.ultimoAccesso = ultimoAccesso;
	}

	// Metodi di utilità
	public boolean isAttivo() {
		return "attivo".equals(stato);
	}

	public boolean isBloccato() {
		return "bloccato".equals(stato);
	}

	public boolean isInattivo() {
		return "inattivo".equals(stato);
	}

	public boolean isAdmin() {
		return "admin".equals(ruolo);
	}

	public boolean isUtente() {
		return "utente".equals(ruolo);
	}

	public boolean isOspite() {
		return "ospite".equals(ruolo);
	}
}

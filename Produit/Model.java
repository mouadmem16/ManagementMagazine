package Produit;

public class Model {
	private long id;
	private String designation;
	private double prixAchat;
	private double prixVente;
	private Categorie.Model categorie;
	
	@Override
	public String toString() {
		return this.designation;
	}

	public Model(long id, String designation, double prixAchat, double prixVente, Categorie.Model categorie) {
		this.id = id;
		this.designation = designation;
		this.prixAchat = prixAchat;
		this.prixVente = prixVente;
		this.categorie = categorie;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public double getPrixAchat() {
		return prixAchat;
	}

	public void setPrixAchat(double prixAchat) {
		this.prixAchat = prixAchat;
	}

	public double getPrixVente() {
		return prixVente;
	}

	public void setPrixVente(double prixVente) {
		this.prixVente = prixVente;
	}

	public Categorie.Model getCategorie() {
		return categorie;
	}

	public void setCategorie(Categorie.Model categorie) {
		this.categorie = categorie;
	}

}

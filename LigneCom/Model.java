package LigneCom;

public class Model {
	private Produit.Model produit;
	private long vente;
	private int quantite;
	private double sousTotale;

	public long getVente() {
		return vente;
	}
	public void setVente(long vente) {
		this.vente = vente;
	}
	public Produit.Model getProduit() {
		return produit;
	}
	@Override
	public String toString() {
		return "Model [produit=" + produit + ", vente=" + vente + ", quantite=" + quantite + ", sousTotale="
				+ sousTotale + "]";
	}
	public void setProduit(Produit.Model produit) {
		this.produit = produit;
		this.sousTotale = produit.getPrixVente() * quantite;
	}
	public int getQuantite() {
		return quantite;
	}
	public void setQuantite(int quantite) {
		this.quantite = quantite;
		this.sousTotale = produit.getPrixVente() * quantite;
	}
	public double getSousTotale() {
		return sousTotale;
	}
	public Model(Produit.Model produit, long vente, int quantite) {
		this.produit = produit;
		this.quantite = quantite;
		this.sousTotale = produit.getPrixVente() * quantite;
		this.vente = vente;
	}	
}

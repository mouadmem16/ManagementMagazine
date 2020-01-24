package Paiement;

enum Mode{
	Espece,
	Cheque,
	Ligne,
	Trait
}

public class Model {
	private long id;
	private double totaleRecept;
	private Mode mode;
	private Vente.Model vente;

	
	@Override
	public String toString() {
		return "Model [id=" + id + ", totaleRecept=" + totaleRecept + ", mode=" + mode + ", vente=" + vente + "]";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Model(long id, double totaleRecept, Mode mode, Vente.Model vente) {
		this.id = id;
		this.totaleRecept = totaleRecept;
		this.mode = mode;
		this.vente = vente;
	}
	
	public Vente.Model getVente() {
		return vente;
	}
	
	public void setVente(Vente.Model vente) {
		this.vente = vente;
	}
	
	public Mode getMode() {
		return mode;
	}

	public double getTotaleRecept() {
		return totaleRecept;
	}
	
	public void setTotaleRecept(double totaleRecept) {
		this.totaleRecept = totaleRecept;
	}
	
	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public void valideRecept(double somme){
		this.totaleRecept += somme;
	}
}
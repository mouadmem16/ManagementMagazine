package Vente;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Model {
	private Date dateNow = new Date();
	SimpleDateFormat ft =  new SimpleDateFormat("dd/MM/yyyy");
	private long id;
	private String date; // DD/MM/YYYY
	private List<LigneCom.Model> lignesCommande;
	private double totale; // calculated
	private Client.Model client;

	public Model(long id, String date, List<LigneCom.Model> lignesCommande, Client.Model client) {
		this.id = id;
		if(date.isEmpty()) this.date = ft.format(dateNow);
		this.date = date;
		this.client = client;
		this.lignesCommande = lignesCommande;
		calculeTotale();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public Client.Model getClient() {
		return client;
	}

	public void setClient(Client.Model client) {
		this.client = client;
	}

	public String getDate() {
		if(date.isEmpty()) this.date = ft.format(dateNow);
		return date;
	}

	public List<LigneCom.Model> getLignesCommande() {
		return lignesCommande;
	}

	@Override
	public String toString() {
		return "Model [id=" + id + ", date=" + date + ", lignesCommande=" + lignesCommande + ", totale=" + totale
				+ ", client=" + client + "]";
	}

	public void setLignesCommande(List<LigneCom.Model> lignesCommande) {
		this.lignesCommande = lignesCommande;
		checkSameProduct();
		calculeTotale();
	}

	public double getTotale() {
		return totale;
	}
	
	private void checkSameProduct(){		
		List<LigneCom.Model> Reslines = new ArrayList<>();
		if(lignesCommande != null)
		for (LigneCom.Model line : lignesCommande) {
			int i = contains(Reslines, line);
			if(i != -1)
				line.setQuantite(line.getQuantite()+Reslines.remove(i).getQuantite());
			Reslines.add(line);
		}
		lignesCommande = Reslines;
	}
	
	private int contains(List<LigneCom.Model> lines, LigneCom.Model x){
		for (int i = 0; i < lines.size(); i++) {
			if(lines.get(i).getProduit().getId() == x.getProduit().getId())
				return i;
		}
		return -1;
	}
	
	private void calculeTotale(){
		double sum = 0;
		if(lignesCommande != null)
		for (LigneCom.Model line : lignesCommande) {
			sum += line.getProduit().getPrixVente() * line.getQuantite();
		}
		this.totale = sum;
	}
}

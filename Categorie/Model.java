package Categorie;

public class Model {
	private long id;
	private String intitule;
	
	@Override
	public String toString() {
		return intitule;
	}

	public Model(long id, String intitule) {
		this.id = id;
		this.intitule = intitule;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getIntitule() {
		return intitule;
	}
	public void setIntitule(String intitule) {
		this.intitule = intitule;
	}
}

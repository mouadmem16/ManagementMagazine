package LigneCom;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DAO extends Factories.database.SuperDAO<LigneCom.Model>{

	{
		tableName = "lignecom";
		connect = getConnection();
	}

	@Override
	public long create(LigneCom.Model obj) {
		PreparedStatement statement;
		try {
			statement = connect.prepareStatement("insert into "+tableName+"(produit, vente, quantite, sousTotale) values(?,?,?,?)", 1);
			statement.setLong(1, obj.getProduit().getId());
			statement.setLong(2, obj.getVente());
			statement.setInt(3, obj.getQuantite());
			statement.setDouble(4, obj.getSousTotale());
			statement.executeUpdate();
			ResultSet rs = statement.getGeneratedKeys();
			while(rs.next()) {
				return rs.getInt(1);
			}
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		return -1;
	}
	
	public void createALL(List<LigneCom.Model> obj, long idVente) {
		if(obj != null)
		for (LigneCom.Model line : obj) {
			line.setVente(idVente);
			this.create(line);
		}
	}

	@Override
	public LigneCom.Model retrieve(Long id) {
		PreparedStatement statement = null;
		try {
			statement = connect.prepareStatement("select produit, vente, quantite, sousTotale from "+tableName+" where id = ?");
			statement.setLong(1, id);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				return new LigneCom.Model((new Produit.DAO()).retrieve(rs.getLong("produit")), rs.getLong("vente"), rs.getInt("quantite"));
			}
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}

	@Override
	public List<LigneCom.Model> retrieveAll() {
		PreparedStatement statement = null;
		List<LigneCom.Model> liste = new ArrayList<LigneCom.Model>();
		try {
			statement = connect.prepareStatement("select produit, vente, quantite, sousTotale from "+tableName);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				liste.add(new LigneCom.Model((new Produit.DAO()).retrieve(rs.getLong("produit")), rs.getLong("vente"), rs.getInt("quantite")));
			}
			return liste;
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}

	@SuppressWarnings("hiding")
	@Override
	public <Integer> List<LigneCom.Model> retrieveAll(Integer key) {
		PreparedStatement statement = null;
		List<LigneCom.Model> liste = new ArrayList<LigneCom.Model>();
		try {
			statement = connect.prepareStatement("select produit, vente, quantite, sousTotale from "+tableName+" where quantite = ? ");
			statement.setInt(1,  (int) key);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				liste.add(new LigneCom.Model((new Produit.DAO()).retrieve(rs.getLong("produit")), rs.getLong("vente"), rs.getInt("quantite")));
			}
			return liste;
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}
	
	public List<LigneCom.Model> retrieveAllVente(long key) {
		PreparedStatement statement = null;
		List<LigneCom.Model> liste = new ArrayList<LigneCom.Model>();
		try {
			statement = connect.prepareStatement("select produit, vente, quantite, sousTotale from "+tableName+" where vente = ? ");
			statement.setLong(1,  key);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				liste.add(new LigneCom.Model((new Produit.DAO()).retrieve(rs.getLong("produit")), rs.getLong("vente"), rs.getInt("quantite")));
			}
			return liste;
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}

	@Override
	public boolean update(LigneCom.Model obj) {
		PreparedStatement statement = null;
		try {
			statement = connect.prepareStatement("update "+tableName+" set quantite=?, sousTotale=? where produit = ? and vente = ?");
			statement.setInt(1, obj.getQuantite());
			statement.setDouble(2, obj.getSousTotale());
			statement.setLong(3, obj.getProduit().getId());
			statement.setLong(4, obj.getVente());
			statement.executeUpdate();
			return true;
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean delete(LigneCom.Model obj) {
		PreparedStatement statement;
		try {
			
			statement = connect.prepareStatement("delete from "+tableName+" where vente = ? and produit = ?");
			statement.setLong(1, obj.getVente());
			statement.setLong(2, obj.getProduit().getId());
			statement.executeUpdate();
			return true;
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	public boolean deleteVente(Long id) {
		PreparedStatement statement;
		try {
			
			statement = connect.prepareStatement("delete from "+tableName+" where vente = ?");
			statement.setLong(1, id);
			statement.executeUpdate();
			return true;
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
}


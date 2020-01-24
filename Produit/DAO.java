package Produit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DAO extends Factories.database.SuperDAO<Produit.Model>{

	{
		tableName = "produit";
		connect = getConnection();
	}

	@Override
	public long create(Produit.Model obj) {
		PreparedStatement statement;
		try {
			statement = connect.prepareStatement("insert into "+tableName+"(designation, prixAchat, prixVente, categorie) values(?,?,?,?)", 1);
			statement.setString(1, obj.getDesignation());
			statement.setDouble(2, obj.getPrixAchat());
			statement.setDouble(3, obj.getPrixVente());
			statement.setLong(4, obj.getCategorie().getId());
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

	@Override
	public Produit.Model retrieve(Long id) {
		PreparedStatement statement = null;
		try {
			statement = connect.prepareStatement("select id, designation, prixAchat, prixVente, categorie from "+tableName+" where id = ?");
			statement.setLong(1, id);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				return new Produit.Model(rs.getInt("id"), rs.getString("designation"), rs.getDouble("prixAchat"), rs.getDouble("prixVente"), (new Categorie.DAO()).retrieve(rs.getLong("categorie")));
			}
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}

	@Override
	public List<Produit.Model> retrieveAll() {
		PreparedStatement statement = null;
		List<Produit.Model> liste = new ArrayList<Produit.Model>();
		try {
			statement = connect.prepareStatement("select id, designation, prixAchat, prixVente, categorie from "+tableName);

			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				liste.add(new Produit.Model(rs.getInt("id"), rs.getString("designation"), rs.getDouble("prixAchat"), rs.getDouble("prixVente"), (new Categorie.DAO()).retrieve(rs.getLong("categorie"))));
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
	public <String> List<Produit.Model> retrieveAll(String key) {
		PreparedStatement statement = null;
		List<Produit.Model> liste = new ArrayList<Produit.Model>();
		try {
			statement = connect.prepareStatement("select id, designation, prixAchat, prixVente, categorie from "+tableName+" where designation like ?");
			statement.setString(1, (java.lang.String) key);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				liste.add(new Produit.Model(rs.getInt("id"), rs.getString("designation"), rs.getDouble("prixAchat"), rs.getDouble("prixVente"), (new Categorie.DAO()).retrieve(rs.getLong("categorie"))));
			}
			return liste;
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}
	
	public List<Produit.Model> retrieveAllCategorie(long key) {
		PreparedStatement statement = null;
		List<Produit.Model> liste = new ArrayList<Produit.Model>();
		try {
			statement = connect.prepareStatement("select id, designation, prixAchat, prixVente, categorie from "+tableName+" where categorie like ?");
			statement.setLong(1, key);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				liste.add(new Produit.Model(rs.getInt("id"), rs.getString("designation"), rs.getDouble("prixAchat"), rs.getDouble("prixVente"), (new Categorie.DAO()).retrieve(rs.getLong("categorie"))));
			}
			return liste;
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}

	@Override
	public boolean update(Produit.Model obj) {
		PreparedStatement statement = null;
		try {
			statement = connect.prepareStatement("update "+tableName+" set designation=? , prixAchat=? , prixVente=? , categorie=? where id = ?");
			statement.setString(1, obj.getDesignation());
			statement.setDouble(2, obj.getPrixAchat());
			statement.setDouble(3, obj.getPrixVente());
			statement.setLong(4, obj.getCategorie().getId());
			statement.setLong(5, obj.getId());
			statement.executeUpdate();
			return true;
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean delete(Produit.Model obj) {
		PreparedStatement statement;
		try {
			
			statement = connect.prepareStatement("delete from "+tableName+" where id = ?");
			statement.setLong(1, obj.getId());
			statement.executeUpdate();
			return true;
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
}

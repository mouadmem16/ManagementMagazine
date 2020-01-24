package Categorie;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DAO extends Factories.database.SuperDAO<Categorie.Model>{

	{
		tableName = "categorie";
		connect = getConnection();
	}

	@Override
	public long create(Categorie.Model obj) {
		PreparedStatement statement;
		try {
			statement = connect.prepareStatement("insert into "+tableName+"(intitule) values(?)", 1);
			statement.setString(1, obj.getIntitule());
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
	public Categorie.Model retrieve(Long id) {
		PreparedStatement statement = null;
		try {
			statement = connect.prepareStatement("select id, intitule from "+tableName+" where id = ?");
			statement.setLong(1, id);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				return new Categorie.Model(rs.getInt("id"), rs.getString("intitule"));
			}
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}

	@Override
	public List<Categorie.Model> retrieveAll() {
		PreparedStatement statement = null;
		List<Categorie.Model> liste = new ArrayList<Categorie.Model>();
		try {
			statement = connect.prepareStatement("select id, intitule from "+tableName);

			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				liste.add(new Categorie.Model(rs.getInt("id"), rs.getString("intitule")));
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
	public <String> List<Categorie.Model> retrieveAll(String key) {
		PreparedStatement statement = null;
		List<Categorie.Model> liste = new ArrayList<Categorie.Model>();
		try {
			statement = connect.prepareStatement("select id, intitule from "+tableName+" where intitule like ? ");
			statement.setString(1, (java.lang.String) key);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				liste.add(new Categorie.Model(rs.getInt("id"), rs.getString("intitule")));
			}
			return liste;
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}

	@Override
	public boolean update(Categorie.Model obj) {
		PreparedStatement statement = null;
		try {
			statement = connect.prepareStatement("update "+tableName+" set intitule=? where id = ?");
			statement.setString(1, obj.getIntitule());
			statement.setLong(2, obj.getId());
			statement.executeUpdate();
			return true;
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean delete(Categorie.Model obj) {
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

package Client;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DAO extends Factories.database.SuperDAO<Client.Model>{
	{
		tableName = "client";
		connect = getConnection();
	}

	@Override
	public long create(Client.Model obj) {
		PreparedStatement statement;
		try {
			statement = connect.prepareStatement("insert into "+tableName+"(nom, prenom, tele, email, adresse) values(?,?,?,?,?)", 1);
			statement.setString(1, obj.getNom());
			statement.setString(2, obj.getPrenom());
			statement.setString(3, obj.getTele());
			statement.setString(4, obj.getEmail());
			statement.setString(5, obj.getAdresse());
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
	public Client.Model retrieve(Long id) {
		PreparedStatement statement = null;
		try {
			statement = connect.prepareStatement("select id, nom, prenom, tele, email, adresse from "+tableName+" where id = ?");
			statement.setLong(1, id);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				return new Client.Model(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("tele"), rs.getString("email"), rs.getString("adresse"));
			}
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}

	@Override
	public List<Client.Model> retrieveAll() {
		PreparedStatement statement = null;
		List<Client.Model> liste = new ArrayList<Client.Model>();
		try {
			statement = connect.prepareStatement("select id, nom, prenom, tele, email, adresse from "+tableName);

			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				liste.add(new Client.Model(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("tele"), rs.getString("email"), rs.getString("adresse")));
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
	public <String> List<Client.Model> retrieveAll(String key) {
		PreparedStatement statement = null;
		List<Client.Model> liste = new ArrayList<Client.Model>();
		try {
			statement = connect.prepareStatement("select id, nom, prenom, tele, email, adresse from "+tableName+" where nom like ? or prenom like ? or email like ? or tele like ? or adresse like ?");
			statement.setString(1, (java.lang.String) key);
			statement.setString(2, (java.lang.String) key);
			statement.setString(3, (java.lang.String) key);
			statement.setString(4, (java.lang.String) key);
			statement.setString(5, (java.lang.String) key);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				liste.add(new Client.Model(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("tele"), rs.getString("email"), rs.getString("adresse")));
			}
			return liste;
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}

	@Override
	public boolean update(Client.Model obj) {
		PreparedStatement statement = null;
		try {
			statement = connect.prepareStatement("update "+tableName+" set nom=? , prenom=? , tele=? , email=? , adresse=? where id = ?");
			statement.setString(1, obj.getNom());
			statement.setString(2, obj.getPrenom());
			statement.setString(3, obj.getTele());
			statement.setString(4, obj.getEmail());
			statement.setString(5, obj.getAdresse());
			statement.setLong(6, obj.getId());
			statement.executeUpdate();
			return true;
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean delete(Client.Model obj) {
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

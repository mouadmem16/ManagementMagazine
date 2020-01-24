package Vente;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DAO extends Factories.database.SuperDAO<Vente.Model>{
	{
		tableName = "vente";
		connect = getConnection();
	}

	@Override
	public long create(Vente.Model obj) {
		PreparedStatement statement;
		try {
			statement = connect.prepareStatement("insert into "+tableName+"(date, totale, client) values(?,?,?)", 1);
			statement.setString(1, obj.getDate());
			statement.setDouble(2, obj.getTotale());
			statement.setLong(3, obj.getClient().getId());
			statement.executeUpdate();
			ResultSet rs = statement.getGeneratedKeys();
			long ret = 0;
			while(rs.next()) {
				ret = rs.getInt(1);
			}
			(new LigneCom.DAO()).createALL(obj.getLignesCommande(), ret);
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		return -1;
	}

	@Override
	public Vente.Model retrieve(Long id) {
		PreparedStatement statement = null;
		try {
			statement = connect.prepareStatement("select id, date, totale, client from "+tableName+" where id = ?");
			statement.setLong(1, id);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				return new Vente.Model(rs.getInt("id"), rs.getString("date"), (new LigneCom.DAO()).retrieveAllVente(id), (new Client.DAO()).retrieve(rs.getLong("client")));
			}
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}

	@Override
	public List<Vente.Model> retrieveAll() {
		PreparedStatement statement = null;
		List<Vente.Model> liste = new ArrayList<Vente.Model>();
		try {
			statement = connect.prepareStatement("select id, date, totale, client from "+tableName);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				liste.add(new Vente.Model(rs.getInt("id"), rs.getString("date"), (new LigneCom.DAO()).retrieveAllVente(rs.getInt("id")), (new Client.DAO()).retrieve(rs.getLong("client"))));
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
	public <String> List<Vente.Model> retrieveAll(String key) {
		PreparedStatement statement = null;
		List<Vente.Model> liste = new ArrayList<Vente.Model>();
		try {
			statement = connect.prepareStatement("select id, date, totale, client from "+tableName+" where date like ? or totale like ?");
			statement.setString(1, (java.lang.String) key);
			statement.setString(2, (java.lang.String) key);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				liste.add(new Vente.Model(rs.getInt("id"), rs.getString("date"), (new LigneCom.DAO()).retrieveAllVente(rs.getInt("id")), (new Client.DAO()).retrieve(rs.getLong("client"))));
			}
			return liste;
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}

	@Override
	public boolean update(Vente.Model obj) {
		PreparedStatement statement = null;
		try {
			statement = connect.prepareStatement("update "+tableName+" set date=? , totale=? , client=? where id = ?");
			statement.setString(1, obj.getDate());
			statement.setDouble(2, obj.getTotale());
			statement.setLong(3, obj.getClient().getId());
			statement.setLong(4, obj.getId());
			if(statement.executeUpdate() != 0){
				(new LigneCom.DAO()).deleteVente(obj.getId());
				(new LigneCom.DAO()).createALL(obj.getLignesCommande(), obj.getId());
				return true;
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean delete(Vente.Model obj) {
		PreparedStatement statement;
		try {
			statement = connect.prepareStatement("delete from "+tableName+" where id = ?");
			statement.setLong(1, obj.getId());
			(new LigneCom.DAO()).deleteVente(obj.getId());
			(new Paiement.DAO()).deleteVente(obj.getId());
			statement.executeUpdate();
			return true;
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
}
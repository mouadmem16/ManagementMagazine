package Paiement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DAO extends Factories.database.SuperDAO<Paiement.Model>{

	{
		tableName = "paiement";
		connect = getConnection();
	}

	@Override
	public long create(Paiement.Model obj) {
		PreparedStatement statement;
		if(obj.getTotaleRecept() == 0.0) return -1;
		try {
			statement = connect.prepareStatement("insert into "+tableName+"(totaleRecept, mode, vente) values(?,?,?)", 1);
			statement.setDouble(1, obj.getTotaleRecept());
			statement.setString(2, obj.getMode().toString());
			statement.setLong(3, obj.getVente().getId());
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
	public Paiement.Model retrieve(Long id) {
		PreparedStatement statement = null;
		try {
			statement = connect.prepareStatement("select id, totaleRecept, mode, vente from "+tableName+" where id = ?");
			statement.setLong(1, id);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				return new Paiement.Model(id, rs.getDouble("totaleRecept"), Mode.valueOf(rs.getString("mode")), (new Vente.DAO()).retrieve(rs.getLong("vente")));
			}
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}

	@Override
	public List<Paiement.Model> retrieveAll() {
		PreparedStatement statement = null;
		List<Paiement.Model> liste = new ArrayList<Paiement.Model>();
		try {
			statement = connect.prepareStatement("select id, totaleRecept, mode, vente from "+tableName);

			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				liste.add(new Paiement.Model(rs.getLong("id"), rs.getDouble("totaleRecept"), Mode.valueOf(rs.getString("mode")), (new Vente.DAO()).retrieve(rs.getLong("vente"))));
			}
			return liste;
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}

	public List<Paiement.Model> retrieveAllVente(Long id) {

		PreparedStatement statement = null;
		List<Paiement.Model> liste = new ArrayList<Paiement.Model>();
		try {
			statement = connect.prepareStatement("select id, totaleRecept, mode, vente from "+tableName+" where vente = ?");
			statement.setLong(1, id);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				liste.add(new Paiement.Model(rs.getLong("id"), rs.getDouble("totaleRecept"), Mode.valueOf(rs.getString("mode")), (new Vente.DAO()).retrieve(rs.getLong("vente"))));
			}
			return liste;
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}
	
	public double TotalReceptVente(Long id) {
		PreparedStatement statement = null;
		double liste = 0;
		try {
			statement = connect.prepareStatement("select totaleRecept from "+tableName+" where vente = ?");
			statement.setLong(1, id);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				liste += rs.getDouble("totaleRecept");
			}
			return liste;
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		return 0;
	}

	@SuppressWarnings("hiding")
	@Override
	public <String> List<Paiement.Model> retrieveAll(String key) {
		PreparedStatement statement = null;
		List<Paiement.Model> liste = new ArrayList<Paiement.Model>();
		try {
			statement = connect.prepareStatement("select id, totaleRecept, mode, vente from "+tableName+" where totaleRecept = ? or mode like ? ");
			statement.setString(1, (java.lang.String) key);
			statement.setString(2, (java.lang.String) key);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				liste.add(new Paiement.Model(rs.getLong("id"), rs.getDouble("totaleRecept"), Mode.valueOf(rs.getString("mode")), (new Vente.DAO()).retrieve(rs.getLong("vente"))));
			}
			return liste;
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}

	@Override
	public boolean update(Paiement.Model obj) {
		PreparedStatement statement = null;
		try {
			statement = connect.prepareStatement("update "+tableName+" set totaleRecept=?, mode=?, vente=? where id = ?");
			statement.setDouble(1, obj.getTotaleRecept());
			statement.setString(2, obj.getMode().toString());
			statement.setLong(3, obj.getVente().getId());
			statement.setLong(4, obj.getId());
			statement.executeUpdate();
			return true;
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean delete(Paiement.Model obj) {
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
	
	public boolean deleteVente(long key) {
		PreparedStatement statement;
		try {
			statement = connect.prepareStatement("delete from "+tableName+" where vente = ?");
			statement.setLong(1, key);
			statement.executeUpdate();
			return true;
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
}

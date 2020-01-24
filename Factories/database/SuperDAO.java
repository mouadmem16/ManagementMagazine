package Factories.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

abstract public class SuperDAO<T> {
	public static Connection connect= null;
	protected String tableName;
    
    public static Connection getConnection(){
    	if(connect == null){       
	    	String url = "jdbc:mysql://localhost:3306/GestMagazine";
	        try{
	            Class.forName("com.mysql.jdbc.Driver");
	            connect = DriverManager.getConnection(url, "root", "");
	        }catch(Exception e){
	            System.out.println(String.format("Manager ko !!! %s", e.getMessage()));
	        }   
    	}
    	return connect;
    }
	
	abstract public long create(T obj);
	abstract public T retrieve(Long id);
	abstract public List<T> retrieveAll();
	abstract public <TKey> List<T> retrieveAll(TKey key);
	abstract public boolean update(T obj);
	abstract public boolean delete(T obj);
	
}

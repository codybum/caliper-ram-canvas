package HANA;

import java.sql.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import payload.LMSEvent;


public class DB_Helpers {

	   

	private String user_name;
	private String server_name;
	private String password;
	private String schema;
	private Connection connection;
	
	public DB_Helpers(String server_name, String user_name, String password, String schema)
	{
	  this.user_name = user_name;
	  this.server_name = server_name;
	  this.password = password;
	  this.schema = schema;
	  
      
      try 
      {  
    	  //Class.forName("com.sap.db.jdbc.Driver");
    	 connection = DriverManager.getConnection("jdbc:sap://" + server_name + ":30015/?autocommit=false",user_name,password);                  
      
      } 
      catch (SQLException e) 
      {
         System.err.println("SQL Error : Connection Failed. : " + e.toString());
         return;
      }
      catch(Exception ex)
      {
    	  System.err.println("Error : " + ex.toString());
           
      }
      
   }

	public boolean runUpdate(String query)
	{
		if (connection != null) 
	      {
	         try 
	         {
	        	//LMSEvent event = LMSEventFromJson(eventStr);
	     		
	            System.out.println("Connection to HANA successful!");
	            //Statement stmt = connection.createStatement();
	            PreparedStatement stmt = connection
	                    .prepareStatement(query);
	            
	            //int resultSet = stmt.executeUpdate(query);
	            int resultSet = stmt.executeUpdate();
	            connection.commit();
	            //System.out.println(stmt.getWarnings());
	            System.out.println(resultSet);
	            return true;
	         } 
	         catch (SQLException e) 
	         {
	        	 System.err.println("SQL Exception: " + e.toString());
	         }
	      }
		return false;
	}
	public boolean runQuery(String query)
	{
		if (connection != null) 
	      {
	         try 
	         {
	        	//LMSEvent event = LMSEventFromJson(eventStr);
	     		
	            System.out.println("Connection to HANA successful!");
	            //Statement stmt = connection.createStatement();
	            PreparedStatement stmt = connection.prepareStatement(query);
	            ResultSet resultSet = stmt.executeQuery();
	            resultSet.next();
	            
	            //System.out.println("ResultSet Size: " + resultSet.);
	            
	            String hello = resultSet.getString(3);
	            System.out.println(hello);
	            
	            return true;
	         } 
	         catch (SQLException e) 
	         {
	        	 System.err.println("SQL Exception: " + e.toString());
	         }
	      }
		return false;
	}
	
	private static LMSEvent LMSEventFromJson(String json)
	{
    	Gson gson = new GsonBuilder().create();
		LMSEvent event = gson.fromJson(json, LMSEvent.class);
        return event;
	}
	
}
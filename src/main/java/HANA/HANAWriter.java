package HANA;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

import payload.CanvasEvent;
import payload.LMSEvent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import core.StreamEngine;


public class HANAWriter implements Runnable {

	private static Gson gson;
	private String user_name;
	private String server_name;
	private String password;
	private Connection connection;
	
	public HANAWriter(String server_name, String user_name, String password)
	{
	  this.user_name = user_name;
	  this.server_name = server_name;
	  this.password = password;
	  gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
	  //gson = new Gson();
	 	
   }
	private boolean getConnection()
	{
		
		try 
	      {
			  connection = DriverManager.getConnection("jdbc:sap://" + server_name + ":30015/?autocommit=false",user_name,password);                  
		      return true;
	      } 
	      catch (SQLException e) 
	      {
	         System.err.println("Connection Failed. User/Passwd Error?");
	      }
		return false;
	}
	private static CanvasEvent CanvasEventFromJson(String json)
	{
    	
    	JsonReader reader = new JsonReader(new StringReader(json));
    	reader.setLenient(true);
    	CanvasEvent event = gson.fromJson(json, CanvasEvent.class);
        return event;
      
	}
    public void run() 
	{
		try
		{
			if(getConnection())
			  {
				StreamEngine.HANAWriterActive = true;
			  }
			
		while (StreamEngine.HANAWriterActive) 
    	{
			String message = StreamEngine.incomingHANAQueue.poll();
			if(message != null)
			{
				//System.out.println("*"+ message +"*");
				try
				{
					//test(message);
					//LMSEvent event = LMSEventFromJson(message); //create object from event
					CanvasEvent event = CanvasEventFromJson(message);
					UUID key = UUID.randomUUID();
					
					if(!runUpdateMetadata(key.toString(),event))
					{
						System.out.println("MetadataHANA Event Update Failed!");
					}
					if(!runUpdateBody(key.toString(),event))
					{
						System.out.println("BodyHANA Event Update Failed!");
					}
					
				}
				catch(Exception ex)
				{
					System.out.println("HANAWriter : HANAWriterActive Loop : Error: " + ex.toString());
				}
			}
			else
			{
				Thread.sleep(1000);
			}
    	}
		connection.close();
		}
		catch(Exception ex)
		{
			System.out.println("QueryNode Error: " + ex.toString());
		}
		
	}    
    
    public boolean runUpdateMetadata(String key,CanvasEvent event)
	{
    	try
    	{
    		String insertQuery = "INSERT INTO CALIPER_RAM.CANVAS_METADATA (uuid," +
    				"root_account_id," +
    			    "user_id," +
    			    "user_login," + 
    			    "hostname," +
    			    "user_agent," +
    			    "context_type," +
    			    "context_id," +
    			    "context_role," +
    			    "request_id," +
    			    "session_id," +
    			    "event_name," +
    			    //"EVENT_TIMESTAMP)" +
    			    "event_time)" +
    			   "VALUES ('" + key + "'," +
    			   	 "'" + String.valueOf(event.metadata.root_account_id) +"'," +
   			    	 "'" + String.valueOf(event.metadata.user_id) +"'," +
    			     "'" + event.metadata.user_login +"'," +
    			     "'" + event.metadata.hostname + "'," +
    			     "'" + event.metadata.user_agent + "'," +
    			     "'" + event.metadata.context_type + "'," +
    			     "'" + event.metadata.context_id + "'," +
    			     "'" + event.metadata.context_role + "'," +
    			     "'" + event.metadata.request_id + "'," +
    			     "'" + event.metadata.session_id + "'," +
    			     "'" + event.metadata.event_name + "'," +
    			     "'" + event.metadata.event_time +"')";

    					 
    		//System.out.println(insertQuery);
    	if (connection.isClosed()) 
	    {
			connection = null;
			getConnection();
	    }
    	
		     try 
	         {
	        	Statement stmt = connection.createStatement();
	        	int resultSet = stmt.executeUpdate(insertQuery);
	        	//System.out.println("Added : " + resultSet + " rows.");
	            connection.commit();
	            return true;
	         } 
	         catch (SQLException e) 
	         {
	        	 System.err.println("SQL Exception: " + e.toString());
	         }
	      
    	}
    	catch(Exception ex)
    	{
    		System.err.println("executeUpdate: " + ex.toString());
    	}
		return false;
	}
    
    public boolean runUpdateBody(String key,CanvasEvent event)
	{
    	try
    	{
    		String insertQuery = "INSERT INTO CALIPER_RAM.CANVAS_BODY (uuid," +
    				"asset_type," +
    			    "asset_id," +
    			    "asset_subtype," +
    			    "category," +
    			    "role," +
    			    "level)" +
    			   "VALUES ('" + key + "'," +
    			     "'" + event.body.asset_type + "'," +
  			         "'" + String.valueOf(event.body.asset_id) + "'," +
    			     "'" + event.body.asset_subtype + "'," +
    			     "'" + event.body.category + "'," +
    			     "'" + event.body.role + "'," +
    			     "'" + event.body.level +"')";

    					 
    		//System.out.println(insertQuery);
    	if (connection.isClosed()) 
	    {
			connection = null;
			getConnection();
	    }
    	
		     try 
	         {
	        	Statement stmt = connection.createStatement();
	        	int resultSet = stmt.executeUpdate(insertQuery);
	        	//System.out.println("Added : " + resultSet + " rows.");
	            connection.commit();
	            return true;
	         } 
	         catch (SQLException e) 
	         {
	        	 System.err.println("SQL Exception: " + e.toString());
	         }
	      
    	}
    	catch(Exception ex)
    	{
    		System.err.println("executeUpdate: " + ex.toString());
    	}
		return false;
	}
    
    
    private static LMSEvent LMSEventFromJson(String json)
	{
    	
    	JsonReader reader = new JsonReader(new StringReader(json));
    	reader.setLenient(true);
    	//Userinfo userinfo1 = gson.fromJson(reader, Userinfo.class);
    	//Gson gson = new GsonBuilder().create();
		LMSEvent event = gson.fromJson(json, LMSEvent.class);
        return event;
      
	}
    private void test(String result)
    {
    	JsonParser parser = new JsonParser();
    	JsonReader jreader = new JsonReader(new StringReader(result));
    	jreader.setLenient(true);

    	try {
    	    JsonElement elem = parser.parse(jreader); //throws malformed json error
    	    JsonArray contacts = elem.getAsJsonArray();

    	    Gson converter = new Gson();
    	    LMSEvent obj = null;

    	    Type cons = new TypeToken<ArrayList<LMSEvent>>(){}.getType();
    	    //temp = converter.fromJson(contacts, cons);
    	} catch(JsonSyntaxException ex) {
    	    // Inform then user that the the Json data contains invalid syntax
    	}
    }
    
    public static class NullStringToEmptyAdapterFactory<T> implements TypeAdapterFactory {
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {

            Class<T> rawType = (Class<T>) type.getRawType();
            if (rawType != String.class) {
                return null;
            }
            return (TypeAdapter<T>) new StringAdapter();
        }
    }

    public static class StringAdapter extends TypeAdapter<String> {
        public String read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return "";
            }
            return reader.nextString();
        }
        public void write(JsonWriter writer, String value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        }
    }
    
}

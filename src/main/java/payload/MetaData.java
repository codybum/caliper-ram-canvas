package payload;

import java.lang.reflect.Field;

public class MetaData {
	
    public long root_account_id;
    public long user_id;
    public String user_login;
    public String hostname;
    public String user_agent;
    public String context_type;
    public String context_id;
    public String context_role;
    public String request_id;
    public String session_id;
    public String event_name;
    public String event_time;
    
    
    
    public MetaData(long root_account_id,long user_id,String user_login,String hostname,String user_agent,String context_type,String context_id,String context_role,String request_id,String session_id,String event_name,String event_time)
    {
    	this.root_account_id = root_account_id;
        this.user_id= user_id;
        this.user_login = user_login;
        this.hostname = hostname;
        this.user_agent = user_agent;
        this.context_type = context_type;
        this.context_id = context_id;
        this.context_role = context_role;
        this.request_id = request_id;
        this.session_id = session_id;
        this.event_name = event_name;
        this.event_time = event_time;
    }
    		
	
	
	public String toString() {
		  StringBuilder result = new StringBuilder();
		  String newLine = System.getProperty("line.separator");

		  //result.append( this.getClass().getName() );
		  //result.append( " Object {" );
		  //result.append(newLine);

		  //determine fields declared in this class only (no fields of superclass)
		  Field[] fields = this.getClass().getDeclaredFields();

		  //print field names paired with their values
		  for ( Field field : fields  ) {
		    //result.append("  ");
		    try {
		      result.append( field.getName() );
		      result.append(":");
		      //requires access to private field:
		      result.append( field.get(this) );
		    } catch ( IllegalAccessException ex ) {
		      System.out.println(ex);
		    }
		    //result.append(newLine);
		    result.append("  ");
		    
		  }
		  //result.append("}");
		  return result.toString().substring(0, result.length() - 1);
		  //return result.toString();
		  
		}
	
	
}

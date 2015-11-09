package payload;

import java.lang.reflect.Field;

public class BodyData {
	/*
	 "body": {
        "asset_type": "assignment",
        "asset_id": 10000008305530,
        "asset_subtype": null,
        "category": "assignments",
        "role": "StudentEnrollment",
        "level": "submit"
    }
	  
	 */
    public String asset_type;
    public long asset_id;
    public String asset_subtype;
    public String category;
    public String role;
    public String level;
    
    
    
    
    
    
    public BodyData(String asset_type, long asset_id, String asset_subtype, String category, String role, String level)
    {
    	this.asset_type = asset_type;
        this.asset_id = asset_id;
        this.asset_subtype = asset_subtype;
        this.category = category;
        this.role = role;
        this.level = level;
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

package payload;

import java.lang.reflect.Field;

public class CanvasEvent {

	public MetaData metadata;
	public BodyData body;
	
	//public LMSEvent(String mediaType, String eventType, String pubMediaId, String userRole, String availability, String isDeleted, String createdAt, String sectionId, String isAvailable, String userId, String institutionName, String lessonId)
	public CanvasEvent(MetaData metadata,BodyData body)
	{
		this.metadata = metadata;
		this.body = body;
	}
	
	
	//return "source:" + source + " urn:" + urn + " metric:" + metric + " timestamp:" + ts + " value:" + value;
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

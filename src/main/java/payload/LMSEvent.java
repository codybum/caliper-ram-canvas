package payload;

import java.lang.reflect.Field;

public class LMSEvent {

	//public String mediaType;
	public String eventType;
	public String pubMediaId;
	public String userRole;
	public String availability;
	public String isDeleted;
	public String createdAt;
	public String sectionId;
	public String isAvailable;
	public String userId;
	public String institutionName;
	public String lessonId;
	
	//public LMSEvent(String mediaType, String eventType, String pubMediaId, String userRole, String availability, String isDeleted, String createdAt, String sectionId, String isAvailable, String userId, String institutionName, String lessonId)
	public LMSEvent(String eventType, String pubMediaId, String userRole, String availability, String isDeleted, String createdAt, String sectionId, String isAvailable, String userId, String institutionName, String lessonId)
	{
		//this.mediaType = mediaType;
		this.eventType = eventType;
		this.pubMediaId = pubMediaId;
		this.userRole = userRole;
		this.availability = availability;
		this.isDeleted = isDeleted;
		this.createdAt = createdAt;
		this.sectionId = sectionId;
		this.isAvailable = isAvailable;
		this.userId = userId;
		this.institutionName = institutionName;
		this.lessonId = lessonId;
	}
	
	
	//public String getMediaType() {return mediaType;}
	public String getEventType() {return eventType;}
	public String getPubMediaId() {return pubMediaId;}
	public String getUserRole() {return userRole;}
	public String getAvailability() {return availability;}
	public String getIsDeleted() {return isDeleted;}
	public String getCreatedAt() {return createdAt;}
	public String getSectionId() {return sectionId;}
	public String getIsAvailable() {return isAvailable;}
	public String getUserId() {return userId;}
	public String getInstitutionName() {return institutionName;}
	public String getLessonId() {return lessonId;}
	
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

package core;


import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;

import AMPQ.AMPQWriter;
import HANA.HANAWriter;
import SQS.SQSReader;


public class StreamEngine {

	public static boolean SQSReaderActive = false;
	public static boolean AMPQWriterActive = false;
	public static boolean HANAWriterActive = false;
	
	public static ConcurrentLinkedQueue<String> incomingAMPQQueue;
	public static ConcurrentLinkedQueue<String> incomingHANAQueue;
	
	public static Config conf;
	
    public static void main(String[] args) throws Exception 
    {
    	final Thread mainThread = Thread.currentThread();
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() {
		    			    }
		});
		
		try
		{
			conf = new Config(checkConfig(args));
			
			incomingAMPQQueue = new ConcurrentLinkedQueue<String>();
			incomingHANAQueue = new ConcurrentLinkedQueue<String>();
			//enableamqp=<no=0, yes=1>
			//enablesqs=<no=0, yes=1>
			//enablehana=<no=0, yes=1>
					
			if(conf.getParam("general","enableamqp"))
			{
				System.out.println("Starting AMPQWriter...");
				AMPQWriter ampq = new AMPQWriter(conf.getAMPQServer(), conf.getAMPQLogin(), conf.getAMPQPassword(),conf.getAMPQOutExchange());
				Thread ampq_thread = new Thread(ampq);
				ampq_thread.start();
				System.out.println("AMPQWriter Active...");
				while(!AMPQWriterActive)
				{
					Thread.sleep(1000);
				}
			}
			
			if(conf.getParam("general","enablehana"))
			{	
				System.out.println("Starting HANAWriter...");
				HANAWriter hana = new HANAWriter(conf.getHANAServer(),conf.getHANALogin(), conf.getHANAPassword());
				Thread hana_thread = new Thread(hana);
				hana_thread.start();
				while(!HANAWriterActive)
				{
					Thread.sleep(1000);
				}
				System.out.println("HANAWriter Active...");
			}
			
    		if(conf.getParam("general","enablesqs"))
    		{	
    			System.out.println("Starting SQSReader...");
    			SQSReader sqr = new SQSReader(conf.getSQSAccessKey(), conf.getSQSSecretKey(), conf.getSQSQueueUrl());
    			Thread sqr_thread = new Thread(sqr);
    			sqr_thread.start();
    			System.out.println("SQSReader Active...");
    		}
		}
		catch(Exception ex)
		{
			System.out.println("StreamEngine : Error : " + ex.toString());
		}
				
		//public String getMediaType() {return mediaType;}
		//String eventStr = "{\"mediaType\" : \"models.PublishableMedia$VideoPresentation$@3448f119\", \"eventType\" : \"MediaPublished\", \"pubMediaId\" : \"3b6b7a6e-cdcb-441a-9a73-377eb7db59bc\", \"userRole\" : \"Admin\", \"availability\" : \"Immediate\", \"isDeleted\" : false, \"createdAt\" : \"2015-09-08T15:53:39.864Z\", \"sectionId\" : \"171bf0be-783a-46c1-ab90-40b3008c08ec\", \"isAvailable\" : true, \"userId\" : \"device\", \"institutionName\" : \"University of Kentucky\", \"lessonId\" : \"f0126ba3-143b-4b5d-92d3-6a1702a8ada1\"}";
		
		
		//incomingAMPQQueue.offer(eventStr);
		//incomingHANAQueue.offer(eventStr);
			
		
		
		//String eventStr = " \"mediaType\" : \"models.PublishableMedia$VideoPresentation$@3448f119\", \"eventType\" : \"MediaPublished\", \"pubMediaId\" : \"3b6b7a6e-cdcb-441a-9a73-377eb7db59bc\", \"userRole\" : \"Admin\", \"availability\" : \"Immediate\", \"isDeleted\" : false, \"createdAt\" : \"2015-09-08T15:53:39.864Z\", \"sectionId\" : \"171bf0be-783a-46c1-ab90-40b3008c08ec\", \"isAvailable\" : true, \"userId\" : \"device\", \"institutionName\" : \"University of Kentucky\", \"lessonId\" : \"f0126ba3-143b-4b5d-92d3-6a1702a8ada1\"";
		
		//LMSEvent event = LMSEventFromJson(eventStr);
		
		//System.out.println(event);
		/*
		String[] sline = eventStr.split(",");
		for(String str : sline)
		{
			String[] sline2 = str.split("\"");
			String firstL = sline2[1].substring(0, 1).toUpperCase();
			String restline = sline2[1].substring(1, sline2[1].length());
			System.out.println("public String get" +  firstL + restline + "() {return " +  sline2[1] + ";}");
			
		}
		*/
    }
    
	public static String checkConfig(String[] args)
	{
		String errorMgs = "StreamReporter\n" +
    			"Usage: java -jar caliper-ram-router.jar" +
    			" -f <configuration_file>\n";
    			
    	if (args.length != 2)
    	{
    	  System.err.println(errorMgs);
    	  System.err.println("ERROR: Invalid number of arguements.");
      	  System.exit(1);
    	}
    	else if(!args[0].equals("-f"))
    	{
    	  System.err.println(errorMgs);
    	  System.err.println("ERROR: Must specify configuration file.");
      	  System.exit(1);
    	}
    	else
    	{
    		File f = new File(args[1]);
    		if(!f.exists())
    		{
    			System.err.println("The specified configuration file: " + args[1] + " is invalid");
    			System.exit(1);	
    		}
    	}
    return args[1];	
	}

    
    
}

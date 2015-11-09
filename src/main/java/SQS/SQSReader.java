package SQS;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import payload.CanvasEvent;
import payload.LMSEvent;
import HANA.HANAWriter.NullStringToEmptyAdapterFactory;
import HANA.HANAWriter.StringAdapter;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import core.StreamEngine;

public class SQSReader implements Runnable {
	
	private static Gson gson;
	private AmazonSQS sqs;
	private String queue_url;
	
	public SQSReader(String aws_access_key_id, String aws_secret_access_key, String queue_url)
	{
		gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
		this.queue_url = queue_url;
		AWSCredentials credentials = null;
		
        try {
            //credentials = new ProfileCredentialsProvider().getCredentials();
            credentials = new BasicAWSCredentials(aws_access_key_id, aws_secret_access_key);
        } catch (Exception e) {
            throw new AmazonClientException(
            		"Provided credentials are invalid. ",
                    //"Cannot load the credentials from the credential profiles file. " +
                    //"Please make sure that your credentials file is at the correct " +
                    //"location (~/.aws/credentials), and is in valid format.",
                    e);
        }

        sqs = new AmazonSQSClient(credentials);
        //Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        Region usEast1 = Region.getRegion(Regions.US_EAST_1);
        
        sqs.setRegion(usEast1);
        
        System.out.println("===========================================");
        System.out.println("Amazon SQS : Init Complete");
        System.out.println("===========================================\n");

	}
	
	public void run() 
	{
		try
		{
			
			StreamEngine.SQSReaderActive = true;
			while (StreamEngine.SQSReaderActive) 
			{
				try
				{
					ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queue_url);
					List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
		            
					if(messages != null)
					{
						for (Message message : messages) 
		            	{
							StreamEngine.incomingAMPQQueue.offer(message.getBody());
							
							StreamEngine.incomingHANAQueue.offer(message.getBody());
							
							
							//System.out.println(ce.body.asset_id);
							//StreamEngine.SQSReaderActive = false;
		            	}
						/*
		            	for (Message message : messages) 
		            	{
			                System.out.println("  Message");
			                System.out.println("    MessageId:     " + message.getMessageId());
			                System.out.println("    ReceiptHandle: " + message.getReceiptHandle());
			                System.out.println("    MD5OfBody:     " + message.getMD5OfBody());
			                System.out.println("    Body:          " + message.getBody());
			                
			                for (Entry<String, String> entry : message.getAttributes().entrySet()) {
			                    System.out.println("  Attribute");
			                    System.out.println("    Name:  " + entry.getKey());
			                    System.out.println("    Value: " + entry.getValue());
			                }
			            }
			            */
					}
					else
					{
						Thread.sleep(1000);
					}
					
				}
			 catch (AmazonServiceException ase) {
	            System.out.println("Caught an AmazonServiceException, which means your request made it " +
	                    "to Amazon SQS, but was rejected with an error response for some reason.");
	            System.out.println("Error Message:    " + ase.getMessage());
	            System.out.println("HTTP Status Code: " + ase.getStatusCode());
	            System.out.println("AWS Error Code:   " + ase.getErrorCode());
	            System.out.println("Error Type:       " + ase.getErrorType());
	            System.out.println("Request ID:       " + ase.getRequestId());
	        } catch (AmazonClientException ace) {
	            System.out.println("Caught an AmazonClientException, which means the client encountered " +
	                    "a serious internal problem while trying to communicate with SQS, such as not " +
	                    "being able to access the network.");
	            System.out.println("Error Message: " + ace.getMessage());
	        }
				catch(Exception ex)
				{
					System.out.println("SQSReader: receiveMessageRequest Error: " + ex.toString());
				}
			}
		}
		catch(Exception ex)
		{
			System.out.println("SQSReader Error: " + ex.toString());
		}
	}
	
	
    

	
	
		
}

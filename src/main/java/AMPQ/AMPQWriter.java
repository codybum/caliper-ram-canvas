package AMPQ;



import java.text.ParseException;
import java.util.concurrent.ConcurrentHashMap;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import core.StreamEngine;


public class AMPQWriter implements Runnable {

	//AMQP
	private ConnectionFactory factory;    
	private Connection connection;
	private QueueingConsumer consumer;
	private static Channel tx_channel;
	private String amqp_server;
	private String amqp_login;
	private String amqp_password;
	private static String outExchange;
	
    
    public AMPQWriter(String amqp_server, String amqp_login, String amqp_password,String outExchange)
    {
    	this.amqp_server = amqp_server;
    	this.amqp_login = amqp_login;
    	this.amqp_password = amqp_password;
    	this.outExchange = outExchange;
    }
	
    public void run() 
	{
		try
		{
			
			// START AMQP
			factory = new ConnectionFactory();
			factory.setHost(amqp_server);
			factory.setUsername(amqp_login);
			factory.setPassword(amqp_password);
			factory.setConnectionTimeout(10000);
			
			connection = factory.newConnection();
			
			
			while(!connection.isOpen())
			{
				System.out.println("Waiting for AMPQ connection to open.");
			}
			
			
				
			//TX Channel
			tx_channel = connection.createChannel();
			tx_channel.exchangeDeclare(outExchange, "fanout");
			//END TX
			
			
		StreamEngine.AMPQWriterActive = true;
		while (StreamEngine.AMPQWriterActive) 
    	{
			String message = StreamEngine.incomingAMPQQueue.poll();
			if(message != null)
			{
				try
				{
					tx_channel.basicPublish(outExchange, "", null, message.getBytes());
				}
				catch(Exception ex)
				{
					System.out.println("AMPQWriter : AMPQWriterActive Loop : Error: " + ex.toString());
				}
			}
			else
			{
				Thread.sleep(1000);
			}
    	}
		tx_channel.close();
		
		}
		catch(Exception ex)
		{
			System.out.println("QueryNode Error: " + ex.toString());
		}
		
	}    
    
    
    
}

package core;

import java.io.File;
import java.io.IOException;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;

public class Config {

	private Wini ini;
	
	public Config(String configFile) 
	{
		try 
		{
			ini = new Wini(new File(configFile));
		} 
		catch (InvalidFileFormatException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean getParam(String group, String param)
	{
		String sparam = ini.get(group,param);
		System.out.println(group + " " + param + " =" + sparam);
		if(sparam.equals("1"))
		{
			System.out.println("return true");
			return true;
		}
		System.out.println("return false");
		return false;
		
	}
	
	public String getAMPQServer()
	{
		return ini.get("ampq","ampq_server");
	}
	public String getAMPQLogin()
	{
		return ini.get("ampq","ampq_login");
	}
	public String getAMPQPassword()
	{
		return ini.get("ampq","ampq_password");
	}
	public String getAMPQOutExchange()
	{
		return ini.get("ampq","ampq_outexchange");
	}
	
	public String getSQSQueueUrl()
	{
		return ini.get("sqs","sqs_queue_url");
	}
	public String getSQSAccessKey()
	{
		return ini.get("sqs","aws_access_key_id");
	}
	public String getSQSSecretKey()
	{
		return ini.get("sqs","aws_secret_access_key");
	}
	
	public String getHANALogin()
	{
		return ini.get("hana","hana_login");
	}
	public String getHANAServer()
	{
		return ini.get("hana","hana_server");
	}
	public String getHANAPassword()
	{
		return ini.get("hana","hana_password");
	}
	
	
}
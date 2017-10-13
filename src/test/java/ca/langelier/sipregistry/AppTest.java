package ca.langelier.sipregistry;

import static org.junit.Assert.fail;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import io.vertx.core.Vertx;
import net.sf.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AppTest {

	private String targetHostname = "localhost";
	private int targetPort = 10000;
	private String targetAOR = "0142e2fa3543cb32bf000100620002";
	private String targetBadAOR = "blablabla";
	Vertx vertxTCPServer = Vertx.vertx();
	
	@Before
	public void before() throws Exception { 
		
	    vertxTCPServer.deployVerticle("ca.langelier.sipregistry.TCPServer");
        Thread.sleep(5000);
	}

	@After
	public void after() throws Exception {
		
		Thread.sleep(10000);
		vertxTCPServer.close();
		vertxTCPServer.undeploy("ca.langelier.sipregistry.TCPServer");
	}
	
	@Test
	public void testResponseFormat() throws Exception {
		
		Socket clientSocket;
		
		List<String> jsonKeys = new ArrayList<String>();
		jsonKeys.add("addressOfRecord");
		jsonKeys.add("tenantId");
		jsonKeys.add("uri");
		jsonKeys.add("path");
		jsonKeys.add("source");
		jsonKeys.add("target");
		jsonKeys.add("userAgent");
		jsonKeys.add("rawUserAgent");
		jsonKeys.add("created");
		jsonKeys.add("lineId");
			
		try {
			
			clientSocket = new Socket(targetHostname, targetPort);
	        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
	        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	        outToServer.writeBytes(targetAOR + "\r\n");
	        JSONObject jsonResponse = JSONObject.fromObject(inFromServer.readLine());
	        
	        Iterator<String> iterator = jsonKeys.iterator();
			while (iterator.hasNext()) {
				
				String jsonKey = iterator.next();		
				if (!jsonResponse.containsKey(jsonKey)){
					fail("Unable to find the json key "+ jsonKey + " in the response payload");
				}
			}

	        clientSocket.close();
		}
		catch (Exception e) {
			fail("Unable to send or process the request: " + e.getMessage());
		}           
	}
	
	@Test
	public void testNotfoundAOR() throws Exception{
		
	Socket clientSocket;
			
		try {
			
			clientSocket = new Socket(targetHostname, targetPort);
	        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
	        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	        outToServer.writeBytes(targetBadAOR + "\r\n");
	        String textFromServer = inFromServer.readLine();
	        if (!textFromServer.equals("")) {
				fail("The server did not send an empty string");
	        }
	    
	        clientSocket.close();
		}

	   
		catch (Exception e) {
			fail("Unable to send or process the request: " + e.getMessage());
		}           
	}
	
	// TODO fail randomly
	//@Test
	public void testMultipleRequests() throws Exception{
	
	Socket clientSocket;
			
		try {
			
			clientSocket = new Socket(targetHostname, targetPort);
	        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
	        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	        outToServer.writeBytes(targetAOR + "\r\n");
	        JSONObject jsonResponse = JSONObject.fromObject(inFromServer.readLine());
	        if (!jsonResponse.get("addressOfRecord").equals(targetAOR)){
	        	fail("The first resquest hasn't been processed properly.");
	        }

	        
	        Thread.sleep(3000);
	        outToServer.writeBytes(targetAOR + "\r\n");
	        jsonResponse = JSONObject.fromObject(inFromServer.readLine());	  
	        if (!jsonResponse.get("addressOfRecord").equals(targetAOR)){
	        	fail("The third resquest hasn't been processed properly.");
	        }
	        
	        clientSocket.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			fail("Unable to send or process the request: " + e.getMessage());
		} 
		
	}
	
	
	@Test(expected = java.net.SocketException.class)
	public void testConnectionIdleTimeout() throws Exception{
		
		Socket clientSocket;
		clientSocket = new Socket(targetHostname, targetPort);
	    
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
	    Thread.sleep(11000);
	    outToServer.writeBytes(targetAOR + "\r\n");  
	    
	    clientSocket.close();
	}
    
}

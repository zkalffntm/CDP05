package tools;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * JSON client to specific host.
 * Kind of factory pattern.
 * Keeps connection alive while any request remains.
 * Connections and reqeust counts are managed with HashMap.
 * 
 * @author Kyuho
 */
public class JSONTransactionClient
{
	private static Map<String, JSONTransactionClient> clientList = 
			new HashMap<String, JSONTransactionClient>();
	
	private String key;
	private int requestCount;
	private Socket socket;
	
	private BufferedReader bufferedReader;
	private PrintWriter printWriter;
	
	
	/**
	 * Generate or get exist connected JSON client to specific host
	 * 
	 * @param ip	destination ip or hostname
	 * @param port	destination port
	 * @return		transaction client
	 * @throws		IOException errors while socket creation
	 */
	public static JSONTransactionClient getClient(String ip, int port) throws IOException
	{
		synchronized(JSONTransactionClient.class)
		{
			JSONTransactionClient instance;
			instance = clientList.get(ip + ":" + port);
			
			if(instance != null) return instance;
			
			return new JSONTransactionClient(ip, port);
		}
	}
	
	private JSONTransactionClient(String ip, int port) throws IOException
	{
		socket = new Socket(ip, port);
	    bufferedReader = new BufferedReader(
	        new InputStreamReader(new DataInputStream(socket.getInputStream())));
	    printWriter = new PrintWriter(
	        new DataOutputStream(socket.getOutputStream()));
	    
		requestCount = 0;
		key = ip + ":" + port;
		clientList.put(key, this);
	}
	
	/**
	 * Request image and save with the file path using data in the tag 'value'
	 * 
	 * @param requestKey
	 * @param requestValue
	 * @param filepath			file path that received image to be saved
	 * @throws JSONException	if the parse fails or doesn't yield a JSONObject or error in creating JSONObject
	 * @throws IOException		if any error while sending or receiving JSON message or saving image
	 */
	public void requestImage(String requestKey, Object requestValue, String filepath) 
			throws JSONException, IOException
	{
		requestCount++;
		
		synchronized(this)
		{
			try
			{
				// Generate JSON Message
			    JSONObject sendJson = new JSONObject();
			    sendJson.put(PacketLiteral.KEY, requestKey);
			    sendJson.put(PacketLiteral.VALUE, requestValue);
			    
				// Send and Receive JSON Message
			    printWriter.println(sendJson.toString());
			    printWriter.flush();
			    String line = bufferedReader.readLine();
			    JSONObject recvJson = new JSONObject(line);
			    
			    // Save image with the filepath
			    ImageManager.writeImageFromByteString(
			            recvJson.getString(PacketLiteral.VALUE), filepath);
			}
			catch(JSONException e)
			{
				lookRequestCount();
				throw e;
			}
			catch(IOException e)
			{
				lookRequestCount();
				throw e;
			}
			
			lookRequestCount();
		}
	}
	
	/**
	 * 
	 * @param requestKey
	 * @param requestValue
	 * @return
	 * @throws JSONException	if the parse fails or doesn't yield a JSONObject or error in creating JSONObject
	 * @throws IOException		if any error while sending or receiving JSON message
	 */
	public JSONObject requestObject(String requestKey, Object requestValue) 
			throws JSONException, IOException
	{
		JSONObject result;
		requestCount++;
		
		synchronized(this)
		{
			try
			{
				// Generate JSON Message
			    JSONObject sendJson = new JSONObject();
			    sendJson.put(PacketLiteral.KEY, requestKey);
			    sendJson.put(PacketLiteral.VALUE, requestValue);
			    printWriter.println(sendJson.toString());
			    printWriter.flush();

				// Send and Receive JSON Message
			    String line = bufferedReader.readLine();
			    result = new JSONObject(line);
			}
			catch(JSONException e)
			{
				lookRequestCount();
				throw e;
			}
			catch(IOException e)
			{
				lookRequestCount();
				throw e;
			}
			
			lookRequestCount();
		}

		return result;
	}
	
	/**
	 * 
	 * @param requestKey
	 * @param requestValue
	 * @return
	 * @throws JSONException	if the parse fails or doesn't yield a JSONObject or error in creating JSONObject
	 * @throws IOException		if any error while sending or receiving JSON message
	 */
	public JSONObject requestObject(String requestKey, Object[] requestValue) 
			throws JSONException, IOException
	{
		JSONObject result;
		requestCount++;

		synchronized(this)
		{
			try
			{
				// Generate JSON Message
			    JSONObject sendJson = new JSONObject();
			    sendJson.put(PacketLiteral.KEY, requestKey);
			    sendJson.put(PacketLiteral.VALUE, requestValue);
			    printWriter.println(sendJson.toString());
			    printWriter.flush();

				// Send and Receive JSON Message
			    String line = bufferedReader.readLine();
			    result = new JSONObject(line);
			}
			catch(JSONException e)
			{
				lookRequestCount();
				throw e;
			}
			catch(IOException e)
			{
				lookRequestCount();
				throw e;
			}
			
			lookRequestCount();
		}

		lookRequestCount();
		return result;
	}

	/**
	 * 
	 * @param requestKey
	 * @param requestValue
	 * @return
	 * @throws JSONException	if the parse fails or doesn't yield a JSONObject or error in creating JSONObject
	 * @throws IOException		if any error while sending or receiving JSON message
	 */
	public JSONArray requestArray(String requestKey, Object requestValue) 
			throws JSONException, IOException
	{
		JSONArray result;
		requestCount++;

		synchronized(this)
		{
			try
			{
				// Generate JSON Message
			    JSONObject sendJson = new JSONObject();
			    sendJson.put(PacketLiteral.KEY, requestKey);
			    sendJson.put(PacketLiteral.VALUE, requestValue);
			    printWriter.println(sendJson.toString());
			    printWriter.flush();

				// Send and Receive JSON Message
			    String line = bufferedReader.readLine();
			    result = new JSONArray(line);
			}
			catch(JSONException e)
			{
				lookRequestCount();
				throw e;
			}
			catch(IOException e)
			{
				lookRequestCount();
				throw e;
			}
			
			lookRequestCount();
		}

		lookRequestCount();
		return result;
		
	}

	/**
	 * 
	 * @param requestKey
	 * @param requestValue
	 * @return
	 * @throws JSONException	if the parse fails or doesn't yield a JSONObject or error in creating JSONObject
	 * @throws IOException		if any error while sending or receiving JSON message
	 */
	public JSONArray requestArray(String requestKey, Object[] requestValue) 
			throws JSONException, IOException
	{
		JSONArray result;
		requestCount++;

		synchronized(this)
		{
			try
			{
				// Generate JSON Message
			    JSONObject sendJson = new JSONObject();
			    sendJson.put(PacketLiteral.KEY, requestKey);
			    sendJson.put(PacketLiteral.VALUE, requestValue);
			    printWriter.println(sendJson.toString());
			    printWriter.flush();

				// Send and Receive JSON Message
			    String line = bufferedReader.readLine();
			    result = new JSONArray(line);
			}
			catch(JSONException e)
			{
				lookRequestCount();
				throw e;
			}
			catch(IOException e)
			{
				lookRequestCount();
				throw e;
			}
			
			lookRequestCount();
		}
		
		lookRequestCount();
		return result;
	}
	
	private void lookRequestCount()
	{
		if(--requestCount == 0)
		{
			try { socket.close(); } catch (IOException e) { }
			clientList.remove(key);
		}
	}
}

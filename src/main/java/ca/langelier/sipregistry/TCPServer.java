package ca.langelier.sipregistry;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.json.JSONObject;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.NetSocket;

public class TCPServer extends AbstractVerticle {

    private LoadRegistry registry = new LoadRegistry();
    private HashMap<String, JSONObject> responseMap = registry.getRegistryContentMap();
    private int idleTimeout = 10;
    private final static Logger LOG = Logger.getLogger(TCPServer.class.getName());
    
    @Override
    public void start() throws Exception {
    	
    	NetServerOptions serverOptions = new NetServerOptions();
    	serverOptions.setIdleTimeout(idleTimeout);
    	
        NetServer server = vertx.createNetServer(serverOptions);
        LOG.log(Level.INFO, "**** TCPServer Started ****");
     
        server.connectHandler(new Handler<NetSocket>() {

            @Override
            public void handle(NetSocket netSocket) {
            	LOG.log(Level.INFO, "New connection from: " + netSocket.remoteAddress().host());

                netSocket.handler(new Handler<Buffer>() {

                    @Override
                    public void handle(Buffer inBuffer) {	                     
                        
                        String mapKey = stripRequestEndofLine(inBuffer.toString());
                        String response = mapLookup(mapKey, responseMap);
                        
                        LOG.log(Level.INFO, "Requested AOR from " + netSocket.remoteAddress().host() + ": " + mapKey);
                        
                        Buffer outBuffer = Buffer.buffer();                                         
                        outBuffer.appendString(response);
                        netSocket.write(outBuffer);
                    }
                });
            }
        });
        server.listen(10000);
    }
    
    private String stripRequestEndofLine (String request) {
    	return request.substring(0, request.length() - 2);
    }
    
    private String mapLookup(String key, HashMap<String, JSONObject> map) {
    	JSONObject value = map.get(key);
        if (value != null) {
        	return value.toString() + "\r\n";
        }
        else {
            return "\r\n";
        } 	
    }
  
}

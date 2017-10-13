package ca.langelier.sipregistry;

import io.vertx.core.Vertx;

//TODO update vertx version to 3.4.2
public class App 
{

    public static void main( String[] args )
    {
                
        Vertx vertxTCPServer = Vertx.vertx();
        vertxTCPServer.deployVerticle("ca.langelier.sipregistry.TCPServer");
     
    }
    
}

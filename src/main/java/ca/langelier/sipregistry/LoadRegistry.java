package ca.langelier.sipregistry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import net.sf.json.JSONObject;

// TODO log the number of entry loaded
public class LoadRegistry {
	
	private String registryFilePath = "src/main/resources/regs.bin";
	private HashMap<String, JSONObject> registryContentMap = new HashMap<String, JSONObject>();
	private final static Logger LOG = Logger.getLogger(LoadRegistry.class.getName());
	
	LoadRegistry (){
		processRegistryFile();
	}
	
	public void setRegistryFile (String filePath){
		this.registryFilePath = filePath;
	}
	
	public String getRegistryFilePath (){
		return this.registryFilePath;
	}
	
	public HashMap<String, JSONObject> getRegistryContentMap (){
		return this.registryContentMap;
	}
	
	public void processRegistryFile (){
		try {
			Stream<String> registryFileStream;
			registryFileStream = Files.lines(Paths.get(registryFilePath));
			registryFileStream.forEach( line ->{
            	validateRegistryFileLine(line);
            });
			registryFileStream.close();
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void validateRegistryFileLine(String line) {
		try {
			LOG.log(Level.FINE, "Parsing of the json line: " + line);
			String addressOfRecord;
			JSONObject lineJson = JSONObject.fromObject(line);
			addressOfRecord = lineJson.get("addressOfRecord").toString();
			
			// TODO check if the key already (duplicate)
			registryContentMap.put(addressOfRecord, lineJson);
			
		}
	    catch (Exception e) {
	        LOG.log(Level.WARNING, "Unable to parse the json line: \n" + line ,e);
	      }
	}
		
}

package logicElements.sensor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.mannheim.wifo2.fesas.logicRepositoryStructure.data.metadata.logic.AbstractLogic;
import de.mannheim.wifo2.fesas.logicRepositoryStructure.data.metadata.logic.LogicType;
import de.mannheim.wifo2.fesas.logicRepositoryStructure.data.metadata.logic.logicInterfaces.ISensorLogic;
import de.mannheim.wifo2.fesas.sasStructure.data.adaptationLogic.information.InformationType;
import de.mannheim.wifo2.fesas.sasStructure.data.adaptationLogic.knowledge.IKnowledgeRecord;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Description from meta data:
 * 
 * This file has been generated by the FESAS development tool set.
 * 
 * @author FESAS
 *
 */
public class ICasaSensor extends AbstractLogic implements ISensorLogic {

	public ICasaSensor() {
		super();
		supportedInformationTypes.add(InformationType.Probe_SIMPLE_MANAGED_RESOURCES);

		this.informationType = InformationType.Sensor_DEFAULT;
		type = LogicType.SENSOR;
		shortName = "ICasaSensor";
	}


	private static final String USER_AGENT = "Mozilla/5.0";
	public static final String SENSOR_ZONE = "Zone";
	public static final String SENSOR_WORKTIME = "WorkTime";
	public static final String SENSOR_PRESENCE = "PresenceSensor";
    public static final String SENSOR_THERMOMETER = "Thermometer";
    public static final String SENSOR_ILLUMINANCE = "Illuminance";
    public static final String SENSOR_CO2 = "CO2-Level";
    public static final String SENSOR_CO = "CO-Level";
    public static final String SENSOR_FLOOD = "FloodSensor";
	
	List<SensorZone> zonesList;
	SensorDataWindow dataWindow;

	//to hold information about the IDs of the sensor devices within each zone
	class SensorZone {
		public String zoneId;
		public String zoneType;
		public String presenceSensor;
		public String thermometer;
		public String photometer;
		public String co2Sensor;
		public String coSensor;
		public String floodSensor;
		
		public SensorZone(String zoneId){
			this.zoneId = zoneId;
//			String[] parts = zoneId.split("_");
//			String zoneType = parts[0]; 
			String zoneType = "";
			//check zone type
			if (zoneId.equals("Outside_Area")) {
				this.zoneType = "Outside";
			} else {
				this.zoneType = "Inside";
			}
		}
		
		public void addDevice(String deviceId){
			String[] parts = deviceId.split("-");
			String deviceType = parts[0];
			switch(deviceType){
				case "PresenceSensor":
					this.presenceSensor = deviceId;
					break;
				case "Thermometer":
					this.thermometer = deviceId;
					break;
				case "Photometer":
					this.photometer = deviceId;
					break;
				case "CO2GasSensor":
					this.co2Sensor = deviceId;
					break;
				case "COGasSensor":
					this.coSensor = deviceId;
					break;
				case "FloodSensor": 
					this.floodSensor = deviceId;
					break;
			}
		}
		
		public String toString (){
			return this.zoneId + ":" 
					+ "\n PresenceSensor: " + this.presenceSensor 
					+ "\n Thermometer: " 	+ this.thermometer
					+ "\n Photometer: " 	+ this.photometer
//					+ "\n CO2GasSensor: " 	+ this.co2Sensor
//					+ "\n COGasSensor: " 	+ this.coSensor
					+ "\n FloodSensor: " 	+ this.floodSensor;
		}
	}
	
	//popup window to display the current data measured by the sensors
	class SensorDataWindow extends JFrame{
		
		private static final long serialVersionUID = 1L;
		private JTextArea textArea = new JTextArea();

		 public SensorDataWindow (){					        
		     JFrame showSensorData = new JFrame("Show Sensor Data");		           
		     showSensorData.setSize(500,500);		          
		     showSensorData.add(new JScrollPane(textArea));		    
		     showSensorData.setVisible(true);	
		     textArea.setEditable(false);
		}
			
		 public void addSensorData(String data){   	 
		   	 textArea.setText(data);
		   	 this.validate();	    	     	 
		}
	}

	@Override
	public void initializeLogic(HashMap<String, String> properties) {
		//use this method for initializing variables, etc.
		JsonParser parser = new JsonParser();
		dataWindow = new SensorDataWindow();
		zonesList = new LinkedList<SensorZone>();
		try {
			
			//create a new SensorZone for each zone and add it to the zones list
			String zones = this.sendGet("http://localhost:9000/icasa/zones/zones");
			System.out.println("Zones received: " + zones);
			JsonArray zonesJson = (JsonArray) parser.parse(zones);
			for (JsonElement zoneEntry : zonesJson) {
				this.zonesList.add(new SensorZone(zoneEntry.getAsJsonObject().getAsJsonPrimitive("id").getAsString()));
			}
			
			//add/map devices to SensorZones
			String devices = this.sendGet("http://localhost:9000/icasa/devices/devices");
			JsonArray devicesJson = (JsonArray) parser.parse(devices);
			for (JsonElement deviceEntry : devicesJson) {
				String deviceZoneId = deviceEntry.getAsJsonObject().getAsJsonPrimitive("location").getAsString();
				System.out.println("Device in Zone " + deviceZoneId);
				for(SensorZone sensorZone : zonesList){
					if (sensorZone.zoneId.equals(deviceZoneId)) {
						sensorZone.addDevice(deviceEntry.getAsJsonObject().getAsJsonPrimitive("id").getAsString());
						System.out.println("Adding Device " + deviceEntry.getAsJsonObject().getAsJsonPrimitive("id").getAsString() + " in " + sensorZone.zoneId);
					}
				}
			}
			
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String callLogic(IKnowledgeRecord data) {
		
		System.out.println("CallLogic from ICasaSensor called");
		
		JsonParser parser = new JsonParser();
		JsonObject sensorDataJson = new JsonObject();
		String windowLog = "";
		
		for(SensorZone zone : zonesList){
			//collect sensor data for specific zone in zoneData-Object
			JsonObject zoneData = new JsonObject();
			zoneData.addProperty("Zone", zone.zoneType);
			
			try {
				//presenceSensor
				boolean presenceValue = false;
				if (zone.presenceSensor != null){
					String presence = this.sendGet("http://localhost:9000/icasa/adaptation/agent/presence/" + zone.presenceSensor + "/state");
					JsonObject presenceJson = (JsonObject) parser.parse(presence);
					presenceValue = presenceJson.getAsJsonPrimitive("state").getAsBoolean();
				}
				zoneData.addProperty(SENSOR_PRESENCE, presenceValue);
				
				//thermometer
				int thermometerValue = 0;
				if (zone.thermometer != null){
					String thermometer = this.sendGet("http://localhost:9000/icasa/adaptation/agent/thermometer/" + zone.thermometer + "/state");
					JsonObject thermometerJson = (JsonObject) parser.parse(thermometer);
					thermometerValue = thermometerJson.getAsJsonPrimitive("state").getAsInt();
				}
				zoneData.addProperty(SENSOR_THERMOMETER, thermometerValue);
				
				//photometer
				int photometerValue = 0;
				if (zone.photometer != null){
					String photometer = this.sendGet("http://localhost:9000/icasa/adaptation/agent/photometer/" + zone.photometer + "/state");
					JsonObject photometerJson = (JsonObject) parser.parse(photometer);
					photometerValue = photometerJson.getAsJsonPrimitive("state").getAsInt();
				}
				zoneData.addProperty(SENSOR_ILLUMINANCE, photometerValue);
				
//				//co2Sensor
//				int co2Value = 0;
//				if (zone.co2Sensor != null){
//					String co2 = this.sendGet("http://localhost:9000/icasa/adaptation/agent/co2/" + zone.co2Sensor + "/state");
//					JsonObject co2Json = (JsonObject) parser.parse(co2);
//					co2Value = co2Json.getAsJsonPrimitive("state").getAsInt();
//				}
//				zoneData.addProperty(SENSOR_CO2, co2Value);
//				
//				//coSensor
//				int coValue = 0;
//				if (zone.coSensor != null){
//					String co= this.sendGet("http://localhost:9000/icasa/adaptation/agent/carbonmonoxide/" + zone.coSensor + "/state");
//					JsonObject coJson = (JsonObject) parser.parse(co);
//					coValue = coJson.getAsJsonPrimitive("state").getAsInt();
//				}
//				zoneData.addProperty(SENSOR_CO, coValue);
				
				//floodSensor
				boolean floodValue = false;
				if (zone.floodSensor != null){
					String flood = this.sendGet("http://localhost:9000/icasa/adaptation/agent/floodsensor/" + zone.floodSensor + "/state");
					JsonObject floodJson = (JsonObject) parser.parse(flood);
					floodValue = floodJson.getAsJsonPrimitive("state").getAsBoolean();
				}
				zoneData.addProperty(SENSOR_FLOOD, floodValue);
				
				//create string to be displayed in the sensor data window
				windowLog = windowLog + "\n Zone: "     + zone.zoneId 		+ "\n"
							+ SENSOR_PRESENCE 	 + ": " + presenceValue 	+ "\n"
							+ SENSOR_THERMOMETER + ": " + thermometerValue  + "\n"
							+ SENSOR_ILLUMINANCE + ": " + photometerValue   + "\n"
//							+ SENSOR_CO2 		 + ": " + co2Value 			+ "\n"
//							+ SENSOR_CO    		 + ": " + coValue    		+ "\n"
							+ SENSOR_FLOOD 		 + ": " + floodValue 		+ "\n";
				
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}	
			// add the zone's sensor data to general JSON
			sensorDataJson.add(zone.zoneId, zoneData);		
		
		}
		System.out.println("Sending " + sensorDataJson.toString());
		this.sendData(sensorDataJson.toString());
		this.dataWindow.addSensorData(windowLog);
		return "sensorDataJson sent to AL";
	}

	/**
	 * Sends a HTTP GET request
	 * @param url Target URL
	 * @return Response
	 * @throws Exception Error occurred during sending
	 */
	private String sendGet(String url) throws Exception {

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url); 
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response.toString();
	}
	
}


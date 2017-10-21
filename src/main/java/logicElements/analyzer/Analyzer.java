package logicElements.analyzer;

import java.util.HashMap;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import de.mannheim.wifo2.fesas.logicRepositoryStructure.data.metadata.logic.AbstractLogic;
import de.mannheim.wifo2.fesas.logicRepositoryStructure.data.metadata.logic.LogicType;
import de.mannheim.wifo2.fesas.logicRepositoryStructure.data.metadata.logic.logicInterfaces.IAnalyzerLogic;
import de.mannheim.wifo2.fesas.sasStructure.data.adaptationLogic.information.InformationType;
import de.mannheim.wifo2.fesas.sasStructure.data.adaptationLogic.knowledge.IKnowledgeRecord;
import de.mannheim.wifo2.fesas.sasStructure.data.adaptationLogic.knowledge.KnowledgeRecord;
import dependencies.utils.Context;
import logicElements.knowledge.Synchronizer;

/**
 * Description from meta data:
 * 
 * This file has been generated by the FESAS development tool set.
 * 
 * @author FESAS
 *
 */
public class Analyzer extends AbstractLogic implements IAnalyzerLogic {

	public Analyzer() {
		super();
		supportedInformationTypes.add(InformationType.Monitoring_DEFAULT);

		this.informationType = InformationType.Analyzing_DEFAULT;
		type = LogicType.ANALYZER;
		shortName = "Analyzer";
	}

	// do not change anything above this line (except of adding import
	// statements)

	// add variables here

	@Override
	public void initializeLogic(HashMap<String, String> properties) {
		// use this method for initializing variables, etc.
		// if there is nothing to do, delete it
	}

	/**
	 * Receives the context data including the information from the managed
	 * recourses and runs the rule Engine to evaluate action should be the next
	 * step.
	 */
	@Override
	public String callLogic(IKnowledgeRecord data) {
		if (data instanceof KnowledgeRecord) {
			if (data.getData() instanceof Context) { //substitute Context with the expected data type if needed
				// data.getData() return the actual data. The other properties
				// of data is metadata (e.g., time stamps).

				Context monitorData = (Context) data.getData();
				
				String analyzerResult = ""; // here "hardcode" some reaction
				
				// first string: zone; list of sensors (sensor key and value)
				HashMap<String, List<HashMap<String, String>>> zoneMap = monitorData.getAllObjects();
				
				JsonObject analyzerResultJson = new JsonObject();
				
				// evaluate presence in the Play_Area
				List<HashMap<String, String>> playAreaSensors = zoneMap.get("Play_Area");
				for(HashMap<String, String> sensor : playAreaSensors) {
					System.out.println(sensor.get("ATTRIBUTE_NAME"));
					System.out.println(sensor.get("VALUE"));	
					if(sensor.get("ATTRIBUTE_NAME").equalsIgnoreCase("PresenceSensor")) {
						System.out.println("Presence Sensor Found");
						if(sensor.get("VALUE").equalsIgnoreCase("true")) {
							System.out.println("Person in Play_Area");
							analyzerResultJson.addProperty("personInPlayArea", new Boolean(true));
						} else {
							analyzerResultJson.addProperty("personInPlayArea", new Boolean(false));
						}
					}
				}
				
				// evaluate presence in the Outside Area
				List<HashMap<String, String>> outsideAreaSensors = zoneMap.get("Outside_Area");
				for(HashMap<String, String> sensor : outsideAreaSensors) {
					System.out.println(sensor.get("ATTRIBUTE_NAME"));
					System.out.println(sensor.get("VALUE"));	
					if(sensor.get("ATTRIBUTE_NAME").equalsIgnoreCase("PresenceSensor")) {
						System.out.println("Presence Sensor Found");
						if(sensor.get("VALUE").equalsIgnoreCase("true")) {
							System.out.println("Person in Outside_Area");
							analyzerResultJson.addProperty("personInOutsideArea", new Boolean(true));
						} else {
							analyzerResultJson.addProperty("personInOutsideArea", new Boolean(false));
						}
					}
				}
				
				// evaluate presence in the Dining Area
				List<HashMap<String, String>> diningAreaSensors = zoneMap.get("Dining_Area");
				for(HashMap<String, String> sensor : diningAreaSensors) {
					System.out.println(sensor.get("ATTRIBUTE_NAME"));
					System.out.println(sensor.get("VALUE"));	
					if(sensor.get("ATTRIBUTE_NAME").equalsIgnoreCase("PresenceSensor")) {
						System.out.println("Presence Sensor Found");
						if(sensor.get("VALUE").equalsIgnoreCase("true")) {
							System.out.println("Person in Dining_Area");
							analyzerResultJson.addProperty("personInDiningArea", new Boolean(true));
						} else {
							analyzerResultJson.addProperty("personInDiningArea", new Boolean(false));
						}
					}
				}
				
				
				// evaluate presence in the Dining Area
				List<HashMap<String, String>> cloakroomSensors = zoneMap.get("Cloakroom");
				for(HashMap<String, String> sensor : cloakroomSensors) {
					System.out.println(sensor.get("ATTRIBUTE_NAME"));
					System.out.println(sensor.get("VALUE"));	
					if(sensor.get("ATTRIBUTE_NAME").equalsIgnoreCase("PresenceSensor")) {
						System.out.println("Presence Sensor Found");
						if(sensor.get("VALUE").equalsIgnoreCase("true")) {
							System.out.println("Person in Cloakroom");
							analyzerResultJson.addProperty("personInCloakroom", new Boolean(true));
						} else {
							analyzerResultJson.addProperty("personInCloakroom", new Boolean(false));
						}
					}
				}				
				
				
			
				this.sendData(analyzerResultJson.toString());

				return "Analyzer - Expected Data Type received! The Value is " + data.getData();
			}
			Synchronizer.getInstance().getSemaphore().release();
			return "Analyzer - Not the expected data type received! It is: "
					+ data.getData().getClass().getSimpleName();
		}
		Synchronizer.getInstance().getSemaphore().release();
		return "Analyzer - Not a KnowledgeRecord received! It is: " + data.getClass().getSimpleName();
	}
}



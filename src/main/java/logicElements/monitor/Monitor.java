package logicElements.monitor;

import java.lang.reflect.Type;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import de.mannheim.wifo2.fesas.logicRepositoryStructure.data.metadata.logic.AbstractLogic;
import de.mannheim.wifo2.fesas.logicRepositoryStructure.data.metadata.logic.LogicType;
import de.mannheim.wifo2.fesas.logicRepositoryStructure.data.metadata.logic.logicInterfaces.IMonitorLogic;
import de.mannheim.wifo2.fesas.sasStructure.data.adaptationLogic.information.InformationType;
import de.mannheim.wifo2.fesas.sasStructure.data.adaptationLogic.knowledge.IKnowledgeRecord;
import de.mannheim.wifo2.fesas.sasStructure.data.adaptationLogic.knowledge.KnowledgeRecord;
import dependencies.utils.Context;
import dependencies.utils.ContextException;
import logicElements.knowledge.Synchronizer;
import logicElements.sensor.ICasaSensor;

/**
 * Description from meta data:
 * 
 * This file has been generated by the FESAS development tool set.
 * 
 * @author FESAS
 *
 */
public class Monitor extends AbstractLogic implements IMonitorLogic {

	private static final String ATTR_WORKTIME = "WorkTime";
	
	public Monitor() {
		super();
		supportedInformationTypes.add(InformationType.Sensor_DEFAULT);

		this.informationType = InformationType.Monitoring_DEFAULT;
		type = LogicType.MONITOR;
		shortName = "Monitor";
	}

	// do not change anything above this line (except of adding import
	// statements)

	// add variables here

	@Override
	public void initializeLogic(HashMap<String, String> properties) {
		// use this method for initializing variables, etc.
		// if there is nothing to do, delete it
	}

	@Override
	public String callLogic(IKnowledgeRecord data) {
		if (data instanceof KnowledgeRecord) { //substitute Object with the expected data type
			if (data.getData() instanceof String) { //substitute OBJECT with the expected data type
				//data.getData() return the actual data. The other properties of data is metadata (e.g., time stamps).
				
				String jsonData = (String) data.getData();
				
				try
				{
					HashMap<String, HashMap<String, String>> mapData = jsonToHashMap(jsonData);
					Context context = new Context();
					
					for (HashMap.Entry<String, HashMap<String, String>> zone : mapData.entrySet()) {

						context.putEntry(zone.getKey(), ICasaSensor.SENSOR_ZONE, zone.getValue().get(ICasaSensor.SENSOR_ZONE), Context.UNIT_NONE, Context.TYPE_STRING);
						context.putEntry(zone.getKey(), ICasaSensor.SENSOR_PRESENCE, zone.getValue().get(ICasaSensor.SENSOR_PRESENCE), Context.UNIT_NONE, Context.TYPE_BOOLEAN);
						context.putEntry(zone.getKey(), ICasaSensor.SENSOR_THERMOMETER, zone.getValue().get(ICasaSensor.SENSOR_THERMOMETER), "K", Context.TYPE_DOUBLE);
						context.putEntry(zone.getKey(), ICasaSensor.SENSOR_ILLUMINANCE, zone.getValue().get(ICasaSensor.SENSOR_ILLUMINANCE), "Lux", Context.TYPE_DOUBLE);
//						context.putEntry(zone.getKey(), ICasaSensor.SENSOR_CO2, zone.getValue().get(ICasaSensor.SENSOR_CO2), "ppm", Context.TYPE_DOUBLE);
//						context.putEntry(zone.getKey(), ICasaSensor.SENSOR_CO, zone.getValue().get(ICasaSensor.SENSOR_CO), "ppm", Context.TYPE_DOUBLE);
						context.putEntry(zone.getKey(), ICasaSensor.SENSOR_FLOOD, zone.getValue().get(ICasaSensor.SENSOR_FLOOD), Context.UNIT_NONE, Context.TYPE_BOOLEAN);
						context.putEntry(zone.getKey(), ATTR_WORKTIME, System.currentTimeMillis() + "", "ms", Context.TYPE_LONG);
					}
					
					System.out.println("Context: " + context.getAllObjects());
					this.sendData(context); //for sending an object
				} catch (ContextException | JsonSyntaxException e) {
					String eText = "Monitor - The data not valid! An exception occured: " + e.getClass() + ": " + e.getMessage();
					System.out.println(eText);
					Synchronizer.getInstance().getSemaphore().release();
					return eText;
				}
				
				// or
				// this.sendArrayList(List); // for a list
				return "Monitor - Expected Data Type received! The Value is " + data.getData();
			}
			Synchronizer.getInstance().getSemaphore().release();
			return "Monitor - Not the expected data type received! It is: " + data.getData().getClass().getSimpleName();
		}
		Synchronizer.getInstance().getSemaphore().release();
		return "Monitor - Not a KnowledgeRecord received! It is: " + data.getClass().getSimpleName();
	}

	// add further methods if needed
	
	/**
	 * Converts the JSON String to a HashMap Structure that is suitable to create a Context
	 * @param jsonString String containing a valid JSON object
	 * @return HashMap that can be used to create a context model
	 * @throws JsonSyntaxException Invalid String
	 */
	private static HashMap<String, HashMap<String, String>> jsonToHashMap(String jsonString) throws JsonSyntaxException
	{
		Gson gson = new Gson();
		Type stringStringMapType = new TypeToken<HashMap<String, HashMap<String, String>>>(){}.getType();
		HashMap<String, HashMap<String, String>> map = gson.fromJson(jsonString, stringStringMapType);
		return map;
	}
}

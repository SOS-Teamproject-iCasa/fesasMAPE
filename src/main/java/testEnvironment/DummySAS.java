package testEnvironment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import de.mannheim.wifo2.fesas.logicRepositoryStructure.data.metadata.logic.logicInterfaces.IAnalyzerLogic;
import de.mannheim.wifo2.fesas.logicRepositoryStructure.data.metadata.logic.logicInterfaces.IExecutorLogic;
import de.mannheim.wifo2.fesas.logicRepositoryStructure.data.metadata.logic.logicInterfaces.IMonitorLogic;
import de.mannheim.wifo2.fesas.logicRepositoryStructure.data.metadata.logic.logicInterfaces.IPlannerLogic;
import de.mannheim.wifo2.fesas.sasStructure.data.adaptationLogic.information.InformationCategory;
import de.mannheim.wifo2.fesas.sasStructure.data.adaptationLogic.information.InformationType;
import de.mannheim.wifo2.fesas.sasStructure.data.adaptationLogic.knowledge.KnowledgeRecord;
import logicElements.analyzer.Analyzer;
import logicElements.executor.Executor;
import logicElements.monitor.Monitor;
import logicElements.planner.Planner;
import logicElements.sensor.ICasaSensor;

public class DummySAS {

	private static DummySAS instance;

	private ArrayList<KnowledgeRecord> testData = new ArrayList<KnowledgeRecord>();

	private IMonitorLogic monitorLogic;
	private IAnalyzerLogic analyzerLogic;
	private IPlannerLogic plannerLogic;
	private IExecutorLogic executorLogic;

	private AdaptationLogicTemplate monitor;
	private AdaptationLogicTemplate analyzer;
	private AdaptationLogicTemplate planner;
	private AdaptationLogicTemplate executor;

	private DummySAS() {
		initializeTestData();
		initializeLogicElements();
		initializeAL();
	}

	public static DummySAS getInstance() {
		if (instance == null) {
			instance = new DummySAS();
		}
		return instance;
	}

	public void start() {
		/*
		 * please specify that element that should start the test. In case there
		 * is some specific data assumed, please edit the variable "probeData".
		 */
		AdaptationLogicTemplate startingComponent = monitor;
		runTest(startingComponent);
	}

	private void initializeTestData() {
		String ownerID = "fesasID-000_1_007";
		long timeStamp = 1456129173400l;

		HashMap<String, HashMap<String, String>> map = new HashMap<String, HashMap<String, String>>();

		HashMap<String, String> dataMap = new HashMap<String, String>();

		Gson gson = new Gson();

		dataMap.put(ICasaSensor.SENSOR_ZONE, "Roses");
		dataMap.put(ICasaSensor.SENSOR_THERMOMETER, "290");
		dataMap.put(ICasaSensor.SENSOR_PRESENCE, "true");
		dataMap.put(ICasaSensor.SENSOR_ILLUMINANCE, "500");
		dataMap.put(ICasaSensor.SENSOR_FLOOD, "false");
		dataMap.put(ICasaSensor.SENSOR_CO, "299");
		dataMap.put(ICasaSensor.SENSOR_CO2, "1400");

		map.put("Roses_1", dataMap);

		dataMap = new HashMap<String, String>();

		dataMap.put(ICasaSensor.SENSOR_ZONE, "Tulips");
		dataMap.put(ICasaSensor.SENSOR_THERMOMETER, "290");
		dataMap.put(ICasaSensor.SENSOR_PRESENCE, "true");
		dataMap.put(ICasaSensor.SENSOR_ILLUMINANCE, "500");
		dataMap.put(ICasaSensor.SENSOR_FLOOD, "false");
		dataMap.put(ICasaSensor.SENSOR_CO, "299");
		dataMap.put(ICasaSensor.SENSOR_CO2, "1400");

		map.put("Tulips_1", dataMap);
		//
		String jsonString1 = gson.toJson(map);

		System.out.println(jsonString1);

		String jsonString = "{\"Tulips_1\":{" + "\"Zone\":\"Tulips\"," + "\"PresenceSensor\":\"true\","
				+ "\"Thermometer\":\"250\"," + "\"Illuminance\":\"499\"," + "\"CO2-Level\":\"200\","
				+ "\"CO-Level\":\"124\"," + "\"FloodSensor\":\"true\"" + "}" + ",\"Tulips_2\":{ "
				+ "\"Zone\": \"Tulips\", " + "\"PresenceSensor\":\"false\", " + "\"Thermometer\":\"286\", "
				+ "\"Illuminance\":\"299\", " + "\"CO2-Level\":\"300\"," + "\"CO-Level\":\"200\","
				+ "\"FloodSensor\":\"false\"" + "}" + ",\"Tulips_3\": { " + "\"Zone\": \"Tulips\", "
				+ "\"PresenceSensor\":\"true\"," + "\"Thermometer\":\"297\"," + "\"Illuminance\":\"500\","
				+ "\"CO2-Level\":\"1000\"," + "\"CO-Level\":\"399\"," + "\"FloodSensor\":\"false\"" + "}"
				+ ",\"Tulips_4\":{" + "\"Zone\":\"Tulips\", " + "\"PresenceSensor\":\"false\","
				+ "\"Thermometer\":\"302\"," + "\"Illuminance\":\"0\"," + "\"CO2-Level\":\"1400\","
				+ "\"CO-Level\":\"400\"," + "\"FloodSensor\":\"false\"" + "}" + ",\"Tulips_5\": { "
				+ "\"Zone\": \"Tulips\", " + "\"PresenceSensor\":\"false\"," + "\"Thermometer\":\"303\","
				+ "\"Illuminance\":\"1800\"," + "\"CO2-Level\":\"1500\"," + "\"CO-Level\":\"500\","
				+ "\"FloodSensor\":\"false\"" + "}" + ",\"Roses_4\": { " + "\"Zone\": \"Roses\", "
				+ "\"PresenceSensor\": \"false\", " + "\"Thermometer\":\"302\", " + "\"Illuminance\": \"0\", "
				+ "\"CO2-Level\": \"1400\", " + "\"CO-Level\": \"400\", " + "\"FloodSensor\": \"false\"" + "}"
				+ ",\"Roses_5\": { " + "\"Zone\": \"Roses\", " + "\"PresenceSensor\": \"false\", "
				+ "\"Thermometer\": \"303\", " + "\"Illuminance\": \"1800\", " + "\"CO2-Level\": \"1500\", "
				+ "\"CO-Level\": \"500\", " + "\"FloodSensor\": \"false\"" + "}" + ",\"Roses_3\": { "
				+ "\"Zone\": \"Roses\", " + "\"PresenceSensor\": \"true\", " + "\"Thermometer\": \"297\", "
				+ "\"Illuminance\": \"500\", " + "\"CO2-Level\": \"1000\", " + "\"CO-Level\": \"399\", "
				+ "\"FloodSensor\": \"false\"" + "}" + ",\"Roses_2\": { " + "\"Zone\": \"Roses\", "
				+ "\"PresenceSensor\": \"false\", " + "\"Thermometer\": \"286\", " + "\"Illuminance\": \"299\", "
				+ "\"CO2-Level\": \"300\", " + "\"CO-Level\": \"200\", " + "\"FloodSensor\": \"false\"" + "}"
				+ ",\"Roses_1\": { " + "\"Zone\": \"Roses\", " + "\"PresenceSensor\": \"true\", "
				+ "\"Thermometer\": \"250\", " + "\"Illuminance\": \"499\", " + "\"CO2-Level\": \"200\", "
				+ "\"CO-Level\": \"124\", " + "\"FloodSensor\": \"true\"" + "}" + "}";
		System.out.println(jsonString);

		Type stringStringMapType = new TypeToken<HashMap<String, HashMap<String, String>>>() {
		}.getType();
		HashMap<String, HashMap<String, String>> map2 = gson.fromJson(jsonString, stringStringMapType);

		buildKnowledgeRecord(InformationType.Sensor_DEFAULT, InformationCategory.SENSOR, ownerID, timeStamp,
				jsonString1);

	}

	private void buildKnowledgeRecord(InformationType infoType, InformationCategory infoCat, String ownerID,
			long timeStamp, Object data) {
		KnowledgeRecord probeData = new KnowledgeRecord(data, infoType.toString(), infoCat.toString(), ownerID,
				timeStamp);
		testData.add(probeData);
	}

	private void initializeLogicElements() {

		monitorLogic = new Monitor();
		analyzerLogic = new Analyzer();
		plannerLogic = new Planner();
		executorLogic = new Executor();

	}

	private void initializeAL() {

		executor = new AdaptationLogicTemplate("executor", null, InformationCategory.EFFECTOR);
		executor.implementLogic(executorLogic);
		planner = new AdaptationLogicTemplate("planner", executor, InformationCategory.EXECUTOR);
		planner.implementLogic(plannerLogic);
		analyzer = new AdaptationLogicTemplate("analyzer", planner, InformationCategory.PLANNER);
		analyzer.implementLogic(analyzerLogic);
		monitor = new AdaptationLogicTemplate("monitor", analyzer, InformationCategory.ANALYZER);
		monitor.implementLogic(monitorLogic);

	}

	private void runTest(AdaptationLogicTemplate startingPoint) {
		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < testData.size(); j++) {
				PseudoLogger.log(getClass().getName(),
						"*******************************************************************", null);
				PseudoLogger.log(getClass().getName(), "Run : " + j, "");
				PseudoLogger.log(getClass().getName(), "Data : " + testData.get(j).getData().toString(), null);
				startingPoint.start(testData.get(j));

				try {
					Thread.sleep(0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}

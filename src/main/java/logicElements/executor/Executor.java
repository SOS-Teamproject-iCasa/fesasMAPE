package logicElements.executor;

import java.util.HashMap;

import de.mannheim.wifo2.fesas.logicRepositoryStructure.data.metadata.logic.AbstractLogic;
import de.mannheim.wifo2.fesas.logicRepositoryStructure.data.metadata.logic.LogicType;
import de.mannheim.wifo2.fesas.logicRepositoryStructure.data.metadata.logic.logicInterfaces.IExecutorLogic;
import de.mannheim.wifo2.fesas.sasStructure.data.adaptationLogic.information.InformationType;
import de.mannheim.wifo2.fesas.sasStructure.data.adaptationLogic.knowledge.IKnowledgeRecord;
import de.mannheim.wifo2.fesas.sasStructure.data.adaptationLogic.knowledge.KnowledgeRecord;
import logicElements.knowledge.Synchronizer;

/**
 * Description from meta data: 
 * 
 * This file has been generated by the FESAS development tool set.
 * 
 * @author FESAS 
 *
 */
public class Executor extends AbstractLogic implements IExecutorLogic {
	
	public Executor() {
		super();	
		supportedInformationTypes.add(InformationType.Planning_DEFAULT);
	
		this.informationType = InformationType.Executing_DEFAULT;
		type = LogicType.EXECUTOR;
		shortName = "Executor";
	}

	// do not change anything above this line (except of adding import statements)

	//add variables here
	
	@Override
	public void initializeLogic(HashMap<String, String> properties) {
		//use this method for initializing variables, etc.
		// if there is nothing to do, delete it
	}
	
	@Override
	public String callLogic(IKnowledgeRecord data) {
		if (data instanceof KnowledgeRecord) {
			if (data.getData() instanceof String) { //substitute String with the expected data type if needed
				//data.getData() return the actual data. The other properties of data is metadata (e.g., time stamps).
				
				/*
				 * 
				 * Your implementation
				 * 
				 */
				
				String executorResult = "{\"Roses_1\": {\"Heater\": [1.0], \"DimmerLight\": [0.5]}, \"Tulips_1\": {\"Sprinkler\": [\"ON\"]}}"; // Example serialized to JSON
				// Available actuators: Heater, Cooler, DimmerLight, Sprinkler, BinaryLightCO, BinaryLightCO2, BinaryLightGardener, SirenCO, SirenCO2, SirenBurglar
				this.sendData(executorResult);
				
				return "Executor - Expected Data Type received! The Value is " + data.getData();
			}
			Synchronizer.getInstance().getSemaphore().release();
			return "Executor - Not the expected data type received! It is: " + data.getData().getClass().getSimpleName();
		}
		Synchronizer.getInstance().getSemaphore().release();
		return "Executor - Not a KnowledgeRecord received! It is: " + data.getClass().getSimpleName();
	}
	
	// add further methods if needed
}
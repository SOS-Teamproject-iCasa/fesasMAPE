package logicElements.analyzer;

import java.util.HashMap;

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

				Context monitorData = (Context)data.getData();
				
				
				/*
				 * 
				 * Your implementation
				 * 
				 */
				
				String analyzerResult = "";
				this.sendData(analyzerResult);

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
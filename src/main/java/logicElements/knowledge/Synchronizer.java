package logicElements.knowledge;

import java.util.concurrent.Semaphore;

public class Synchronizer {
	
	private static Synchronizer singleton;
	private Semaphore sema;
	
	public synchronized static Synchronizer getInstance() {
		if(singleton == null)
			singleton = new Synchronizer();
		
		return singleton;
	}
	
	/**
	 * Default constructor - Semaphore has one permit -> mutual exclusion
	 */
	private Synchronizer()
	{
		this.sema = new Semaphore(1);
	}
	
	public Semaphore getSemaphore()
	{
		return sema;
	}
}

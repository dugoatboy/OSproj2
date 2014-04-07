// import com.sun.org.apache.xml.internal.security.utils.HelperNodeList;


public class Santa implements Runnable {

	enum SantaState {SLEEPING, READY_FOR_CHRISTMAS, WOKEN_UP_BY_ELVES, WOKEN_UP_BY_REINDEER,TERMINATED};
	private SantaState state;
    private SantaScenario s;
	
	public Santa(SantaScenario scenario) {
		this.state = SantaState.SLEEPING;
        this.s = scenario;
	}
	
	public void wakeSanta(int cause) {
        if (cause == 1) {
            this.state = SantaState.WOKEN_UP_BY_REINDEER;
        } else {
            this.state = SantaState.WOKEN_UP_BY_ELVES;
        }
    }

	@Override
	public void run() {
		while(true) {
			// wait a day...
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
                state=SantaState.TERMINATED;
                Thread.currentThread().interrupt();
                return;
			}
			switch(state) {
			case SLEEPING: // if sleeping, continue to sleep
				break;
			case WOKEN_UP_BY_ELVES:
                int size = this.s.santasDoor.size();
                for (int i = 0; i < size; i++){
                   this.s.santasDoor.remove().setState(Elf.ElfState.WORKING);
                }
                state =SantaState.SLEEPING;
				break;
			case WOKEN_UP_BY_REINDEER: 
				// FIXME: assemble the reindeer to the sleigh then change state to ready 
				break;
			case READY_FOR_CHRISTMAS: // nothing more to be done
				break;
			}
		}
	}

	
	/**
	 * Report about my state
	 */
	public void report() {
		System.out.println("Santa : " + state);
	}
	
	
}

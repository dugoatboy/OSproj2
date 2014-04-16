// import com.sun.org.apache.xml.internal.security.utils.HelperNodeList;


public class Santa implements Runnable {

	enum SantaState {SLEEPING, READY_FOR_CHRISTMAS, WOKEN_UP_BY_ELVES, WOKEN_UP_BY_REINDEER,TERMINATED};
	private SantaState state;
    private SantaScenario s;
	private boolean elfesatdoor;

    public void setElfesAtDoor(boolean state){
        this.elfesatdoor = true;
    }

    public boolean getElfesAtDoor(){
        return this.elfesatdoor;
    }

	public Santa(SantaScenario scenario) {
		this.state = SantaState.SLEEPING;
        this.s = scenario;
	}
	
	public void wakeSanta(int cause) {
        if(this.state == SantaState.SLEEPING) {
            if (cause == 1) {
                this.state = SantaState.WOKEN_UP_BY_REINDEER;
            } else {
                this.state = SantaState.WOKEN_UP_BY_ELVES;
            }
        }
    }

	@Override
	public void run() {
        setElfesAtDoor(false);

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
                try {
                    this.s.door.acquire();
                    int size = this.s.santasDoor.size();
                    for (int i = 0; i < size; i++){
                        this.s.santasDoor.remove().setState(Elf.ElfState.WORKING);
                    }
                    this.s.door.release();

                } catch (InterruptedException e) {
                    this.s.door.release();
                    state=SantaState.TERMINATED;
                    Thread.currentThread().interrupt();
                    return;
                }

                state =SantaState.SLEEPING;
				break;
			case WOKEN_UP_BY_REINDEER:
                assembleReindeer();
				state = SantaState.READY_FOR_CHRISTMAS;
                break;
			case READY_FOR_CHRISTMAS: // nothing more to be done
				break;
			}
		}
	}
    //Assembles all of the Reindeer
	private void assembleReindeer(){
        for(Reindeer reindeer: s.reindeers) {
            reindeer.assemble();
            s.reindeer.release();
        }

    }
	/**
	 * Report about my state
	 */
	public void report() {
		System.out.println("Santa : " + state);
	}
	
	
}

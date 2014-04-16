import java.util.Random;


public class Reindeer implements Runnable {

	public enum ReindeerState {AT_BEACH, AT_WARMING_SHED, AT_THE_SLEIGH,TERMINATED};
	private ReindeerState state;
	private SantaScenario scenario;
	private Random rand = new Random();

	/**
	 * The number associated with the reindeer
	 */
	private int number;
	
	public Reindeer(int number, SantaScenario scenario) {
		this.number = number;
		this.scenario = scenario;
		this.state = ReindeerState.AT_BEACH;
	}

	@Override
	public void run() {
		while(true) {
		// wait a day
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {

			state= ReindeerState.TERMINATED;
            Thread.currentThread().interrupt();
            return;
		}
		// see what we need to do:
		switch(state) {
		case AT_BEACH: { // if it is December, the reindeer might think about returning from the beach
			if (scenario.isDecember) {
				if (rand.nextDouble() < 0.1) {
					state = ReindeerState.AT_WARMING_SHED;
                    scenario.readyReideer ++;
				}
			}
			break;			
		}
		case AT_WARMING_SHED:

            try {
                if(scenario.readyReideer > 8){
                    scenario.santa.wakeSanta(1);
                }
                scenario.reindeer.acquire();

            } catch (InterruptedException e) {
                state= ReindeerState.TERMINATED;
                Thread.currentThread().interrupt();
            }

            break;
		case AT_THE_SLEIGH: 
			// keep pulling
			break;
		}
		}
	};
	public void assemble(){
        this.state = ReindeerState.AT_THE_SLEIGH;
    }
	/**
	 * Report about my state
	 */
	public void report() {
		System.out.println("Reindeer " + number + " : " + state);
	}
	
}

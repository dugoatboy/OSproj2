import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class SantaScenario {

	public Santa santa;
	public List<Elf> elves;
	//public List<Reindeer> reindeers;
    public Queue<Elf> santasDoor;
    public List<Thread> elvesTh;
    //public List<Thread> reindeersTh;
    public Thread SantaTh;
	public boolean isDecember;
    public boolean isDone;
	
	public static void main(String args[]) {
		SantaScenario scenario = new SantaScenario();
		scenario.isDecember = false;
		// create the participants
		// Santa
		scenario.santa = new Santa(scenario);
		Thread th = new Thread(scenario.santa);
        scenario.SantaTh = th;
        scenario.SantaTh.start();
		// The elves: in this case: 10
		scenario.elves = new ArrayList<>();
        scenario.elvesTh = new ArrayList<>();

		for(int i = 0; i != 10; i++) {
			Elf elf = new Elf(i+1, scenario);
			scenario.elves.add(elf);
			th = new Thread(elf);
            scenario.elvesTh.add(th);
            scenario.elvesTh.get(i).start();
		}
		/*/ The reindeer: in this case: 9
		scenario.reindeers = new ArrayList<>();
        scenario.reindeersTh = new ArrayList<>();
		for(int i=0; i != 9; i++) {
			Reindeer reindeer = new Reindeer(i+1, scenario);
			scenario.reindeers.add(reindeer);
			th = new Thread(reindeer);
            scenario.reindeersTh.add(th);
            scenario.reindeersTh.get(i).start();
		}*/
        scenario.santasDoor = new LinkedList<>();
		// now, start the passing of time
		for(int day = 0; day < 500; day++) {
			// wait a day
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
			// turn on December
			if (day > (365 - 31)) {
				scenario.isDecember = true;
			}
            if(day > 370){
                if (scenario.isDone == false){
                    scenario.isDone = true;
                    scenario.SantaTh.interrupt();
                    for (Thread t: scenario.elvesTh){
                        t.interrupt();
                    }
                    /*for (Thread t: scenario.reindeersTh){
                        t.interrupt();
                    }*/
                }
            }
			// print out the state:
			System.out.println("***********  Day " + day + " *************************");

            scenario.santa.report();
			for(Elf elf: scenario.elves) {
				elf.report();


			}
			/*for(Reindeer reindeer: scenario.reindeers) {
				reindeer.report();
			}*/
		}
	}
	
	
	
}

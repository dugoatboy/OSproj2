import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Semaphore;


public class SantaScenario {

	public Santa santa;
	public List<Elf> elves;
	public List<Reindeer> reindeers;
    public Queue<Elf> santasDoor;
    public Queue<Elf> inTrouble;
    public List<Thread> elvesTh;
    public List<Thread> reindeersTh;
    public Thread SantaTh;
    public int readyReideer;
	public boolean isDecember;
    public boolean isDone;

    public final Semaphore trouble =  new Semaphore(1,true);
    public final Semaphore reindeer =  new Semaphore(0,true);
    public final Semaphore door =  new Semaphore(1,true);
    public final Semaphore waitTrouble =  new Semaphore(0,true);


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
		// The reindeer: in this case: 9
		scenario.reindeers = new ArrayList<>();
        scenario.reindeersTh = new ArrayList<>();
		for(int i=0; i != 9; i++) {
			Reindeer reindeer = new Reindeer(i+1, scenario);
			scenario.reindeers.add(reindeer);
			th = new Thread(reindeer);
            scenario.reindeersTh.add(th);
            scenario.reindeersTh.get(i).start();
		}
        scenario.readyReideer = 0;
        scenario.santasDoor = new LinkedList<>();
        scenario.inTrouble = new LinkedList<>();

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
                    for (Thread t: scenario.reindeersTh){
                        t.interrupt();
                    }
                }
            }
			// print out the state:
			System.out.println("***********  Day " + day + " *************************_");

            scenario.santa.report();
			for(Elf elf: scenario.elves) {
				elf.report();
			}
            try {
                scenario.trouble.acquire();
                int size = scenario.inTrouble.size();
                scenario.trouble.release();

                if(size > 2 && day < 370) {
                    //More than 2 Elfes waiting
                    scenario.door.acquire();
                    if (scenario.santasDoor.isEmpty()){
                       //No other Elfes at santas door
                        scenario.trouble.acquire();
                        for (int i = 0; i < size; i++) {
                            Elf a = scenario.inTrouble.remove();
                            a.setState(Elf.ElfState.AT_SANTAS_DOOR);
                            scenario.santasDoor.add(a);
                            scenario.waitTrouble.release();

                        }
                        scenario.trouble.release();
                    }
                    scenario.door.release();
                }

            } catch (InterruptedException e) {
                scenario.trouble.release();
                scenario.door.release();
                Thread.currentThread().interrupt();
                return;
            }

			for(Reindeer reindeer: scenario.reindeers) {
				reindeer.report();
			}
		}
	}


	
	
}

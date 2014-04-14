This problem demonstrates the use of semaphores to coordinate three types of processes. Santa Claus sleeps in his shop at the North Pole and can only be woken by either:
All nine reindeer being back from their vacation in the South Pacific or
Some of the elves having difficulties making toys.
To allow Santa to get some sleep, the elves can only wake him when three of them are having problems. When three elves are having their problems solved, any other elves wishing to visit Santa must wait for those elves to return. It is assumed that the reindeer do not want to leave the tropics, and therefore stay there until the last possible moment – they only return in December. The last reindeer to arrive must get Santa while the others wait in a warming hut before being harnessed to the sleigh. 

The objective of this project is for to to implement this problem as a multithreaded Java using the Java synchronization primitives. 

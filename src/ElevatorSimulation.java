import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
/****************************************************************************** *
* Name: Colton Conley
* Block: A
* Date: 10/12/14
*
* Program B: Elevator Simulation Description:
* 	This elevator simulation runs on the premise that in each time click, an 
* elevator picks up and drops off passengers before moving one floor (if necessary).
* For n number of elevators, the elevators are numbered 1 to n.  The same numbering
* system applied to floors.
* 	The strategy for deploying elevators is similar to that of real elevators in
* a hotel, however there are a few differences.  Since a request can come in on any
* floor, all elevators stay in the middle of all the floors in an attempt to equalize 
* the wait time for each passenger.  When a request does come in, a person is placed
* on the appropriate floor and the first available elevator that is at the halfway 
* point or closer is sent to pick the person up.  This means that elevator 1 is
* sent first, then elevator 2 and so on.  However, if another elevator is traveling 
* in the same direction a person wishes to go, a new person is not dispatched and the
* person is loaded onto the already busy elevator.  If an elevator is already en route
* for a particular person, whether it was dispatched or is picking him/her up on the
* way, a second elevator is not sent.  
* 	This strategy works very well when there are many more floors than elevators because 
* the elevators are able to change direction depending where the people are. Since they 
* reside in the middle, obviously wait time will be shorter for people starting at a middle 
* floor. However, the wait times will on average be even for those on the top and bottom
* floors.  In the event that the elevator to floor ratio is high, this strategy may be a 
* little less efficient than elevators scattered across the floors moving in set paths
* 	The simulation will display who was picked up and dropped off at each time click, 
* the contents of each elevator an floor, and at the conclusion of the program the
* total number of people transported will be displayed.
* ******************************************************************************/
public class ElevatorSimulation {

	public static final int NUMBER_OF_ELEVATORS = 2;
	public static final int NUMBER_OF_FLOORS = 5;

	public static void main(String[] args) {
		boolean open = ElevatorRequestReader.openRequestFile();
		if(open)
		{

			Floor[] floors = new Floor[NUMBER_OF_FLOORS];
			Elevator[] elevators = new Elevator[NUMBER_OF_ELEVATORS];
			createFloors(floors);
			createElevators(elevators);
			//get input determining number of time clicks
			Scanner input = new Scanner(System.in);
			System.out.println("Please enter the desired number of time clicks");
			int numTimeClicks = input.nextInt();
			Statistics stats = new Statistics();

			for (int timeClicks = 1; timeClicks <= numTimeClicks; timeClicks++)
			{
				stats.clearPeopleDroppedOffAndPickedUp();
				//fill the floors with people for any incoming requests
				fillFloors(floors);
				//pickup and drop off people
				pickupAllElevators(elevators, floors, stats);
				dropoffAllElevators(elevators, stats);
				//in case status of the elevator changed to allow pickup of new passengers
				pickupAllElevators(elevators, floors, stats);
				//move elevators
				moveAllElevators(elevators, floors);
				//print everything from time click
				printUniverse(floors, elevators, timeClicks, stats);

			}
			//print total number of people transported
			System.out.println("******************************************************************");
			System.out.println(stats.getNumberOfDropoffs() + " people were dropped off in total");


		}

	}

	/**
	 * Fills an array with floor objects
	 * 
	 * 
	 * @param floors 	An array to be filled with floors
	 */
	private static void createFloors(Floor[] floors)
	{
		//create all floors with an appropriate number
		for(int i = 0; i < NUMBER_OF_FLOORS; i++)
		{
			floors[i] = new Floor(i + 1); 
		}
	}
	/**
	 * Fills an array with elevator objects
	 * 
	 * @param elevators		Array to be filled with elevators
	 */
	private static void createElevators(Elevator[] elevators)
	{
		//create all elevators with an appropriate number and starting location in middle
		for(int i = 0; i < NUMBER_OF_ELEVATORS; i++)
		{
			elevators[i] = new Elevator(i + 1, NUMBER_OF_FLOORS / 2); 
		}
	}

	/**
	 * Fills all the floors with person objects corresponding to the requests at that time click
	 * 
	 * @param floors	floors to receive Person objects
	 */
	private static void fillFloors(Floor[] floors)
	{
		//get the requests
		Request[] requests = ElevatorRequestReader.getRequests();
		for (int index = 0; index < requests.length; index++)
		{
			//create a new person and add it to the appropriate floor
			Person newPerson = new Person(requests[index].personName, requests[index].startFloor, requests[index].destinationFloor);
			floors[newPerson.getLocation() - 1].addPerson(newPerson);

		}
	}

	/**
	 * Drops off everybody who has arrived at their final destination
	 * from all elevators
	 * 
	 * @param elevators		Elevators containing people to be dropped off
	 * @param stats			Statistics object to record drop off
	 */
	private static void dropoffAllElevators (Elevator[] elevators, Statistics stats)
	{
		for (int i = 0; i < elevators.length; i++)
		{
			elevators[i].dropoff(stats);
		}
	}

	/**
	 * Picks up all people who are on the same floor as an elevator
	 * traveling in the same direction
	 * 
	 * @param elevators		elevators to pick people up
	 * @param floors		floors containing people to be picked up
	 * @param stats			Statistics object to record pickup
	 */
	private static void pickupAllElevators(Elevator[] elevators, Floor[] floors, Statistics stats)
	{
		for (int i = 0; i < elevators.length; i++)
		{
			elevators[i].pickup(floors[elevators[i].getCurrentLocation() - 1], stats);
		}
	}
	
	/**
	 * Checks to see if there are any people not going to be picked up by an elevator
	 * and sends any available elevators off to transport them.  Also moves all 
	 * elevators one floor in the appropriate direction.
	 * 
	 * @param elevators		Elevators to be moved and sent off
	 * @param floors		Floors containing people requiring pickup
	 */
	private static void moveAllElevators (Elevator[] elevators, Floor[] floors)
	{
		//dispatch any available elevators for waiting people
		for (int i = 0; i < floors.length; i++)
		{
			sendElevatorsForFloor(elevators, floors[i]);
		}
		//move all elevators in appropriate direction
		for (int i = 0; i < elevators.length; i++)
		{
			elevators[i].move(NUMBER_OF_FLOORS);
		}
	}

	/**
	 * Dispatches available elevators for people not currently being picked up
	 * 
	 * @param elevators		Array of elevators possibly containing available elevators
	 * @param floor			People requiring pickup
	 */
	private static void sendElevatorsForFloor (Elevator[] elevators, Floor floor)
	{

		for (int i = 0; i < floor.getLength(); i++)
		{
			if (elevatorOnWay(elevators, floor.getPerson(i)) == false)
			{
				sendElevator(elevators, floor.getPerson(i));
			}

		}
	}
	
	/**
	 * Checks for and dispatches a single available elevator to pick up a waiting person
	 * 
	 * @param elevators		Array containing elevators
	 * @param person		Person not yet picked up
	 */
	private static void sendElevator (Elevator[] elevators, Person person)
	{
		int counter = 0;
		while (counter < elevators.length)
		{
			if(elevators[counter].isBusy() == false && isCloserThanHalf(elevators[counter], person))
			{ 
				elevators[counter].assignToPerson(person);
				//if an elevator is sent exit loop
				counter = elevators.length;
			}
			counter ++;
		}
	}

	/**
	 * Determines whether or not an elevator is already en route to pick up
	 * a specified person
	 * 
	 * @param elevators		All elevators that could already be sent to pickup a
	 * 						specified person
	 * @param person		A person not yet picked up by an elevator
	 * @return				Returns a boolean determining whether or not an elevator
	 * 						is already going to pick up a specified person
	 */
	private static boolean elevatorOnWay(Elevator[] elevators, Person person)
	{
		boolean onWay = false;

		for (int i = 0; i < elevators.length; i++)
		{
			
			if (isInLineWithPerson(elevators[i], person) ||
					//for the case in which a person wants to go to their start floor
					elevators[i].getCurrentDestination() == person.getLocation() && elevators[i].isBusy())
			{
				onWay = true;
			}
		}

		return onWay;
	}
	
	
	/**
	 * Determines whether or not a busy elevator can pick up the specified person on the way
	 * 
	 * @param elevator		Any elevator
	 * @param person		A person not yet picked up
	 * @return				Returns a boolean determining whether or not the specified elevator
	 * 						can pick up the specified person
	 */
	private static boolean isInLineWithPerson (Elevator elevator, Person person)
	{
		boolean inLine = false;
		if(elevator.getCurrentDirectionIsUp() == person.getDirectionIsUp() && isCloserThanHalf(elevator, person) && elevator.isBusy())
		{
			if(elevator.getCurrentDirectionIsUp() == true && elevator.getCurrentLocation() < person.getLocation())
			{
				inLine = true;
			}
			else if (elevator.getCurrentDirectionIsUp() == false && elevator.getCurrentLocation() > person.getLocation())
			{
				inLine = true;
			}
		}
		return inLine;
	}

	/**
	 * Checks to see if an elevator is closer to a person than the middle floor 
	 * (where the elevators automatically reside)
	 * 
	 * @param elevator		Any elevator
	 * @param person		A person awaiting pickup
	 * @return				Returns a boolean determining if the elevator is closer 
	 * 						to the person than the middle floor is
	 */
	private static boolean isCloserThanHalf(Elevator elevator, Person person)
	{
		boolean isCloser = false;
		if (elevator.getCurrentLocation() >= NUMBER_OF_ELEVATORS / 2 && person.getLocation() >= NUMBER_OF_ELEVATORS / 2)
		{
			isCloser = true;
		}
		else if (elevator.getCurrentLocation() <= NUMBER_OF_ELEVATORS / 2 && person.getLocation() <= NUMBER_OF_ELEVATORS / 2)
		{
			isCloser = true;
		}

		return isCloser;
	}
	/**
	 * Prints the status of the universe
	 * 
	 * @param floors			All floors in the simulation
	 * @param elevators			All elevators in the simulation
	 * @param time				The current time click
	 */
	private static void printUniverse(Floor[] floors, Elevator[] elevators, int time, Statistics stats)
	{
		System.out.println("**** Time Click " + time + " **********************************************");
		System.out.println(" ");
		printStatistics(stats);
		System.out.println(" ");
		printElevators(elevators);
		printFloors(floors);//make separate methods to print floors, elevators and extras, probably create extras class to store data and pass as parameter
	}
	
	/**
	 * Prints the people picked up and dropped off in that time click
	 * 
	 * @param stats		A statistics object which contains ArrayLists of people
	 * 					picked up and dropped off
	 */
	private static void printStatistics(Statistics stats)
	{
		for(int index = 0; index < stats.getLengthPeoplePickedUp(); index++)
		{
			System.out.println(stats.getPeoplePickedUp().get(index).getName() + " was picked up on floor " + stats.getPeoplePickedUp().get(index).getLocation());
		}
		
		for(int index = 0; index < stats.getLengthPeopleDroppedOff(); index++)
		{
			System.out.println(stats.getPeopleDroppedOff().get(index).getName() + " was dropped off on floor " + stats.getPeopleDroppedOff().get(index).getDestination());
		}
	}
	
	/**
	 * Prints the current status of all elevators and their contents
	 * 
	 * @param elevators		All elevators in the simulation
	 */
	private static void printElevators(Elevator[] elevators)
	{
		for (int i = 0; i < elevators.length; i++)
		{
			if (elevators[i].getLength() > 0)
			{
				System.out.println("Elevator " + (i + 1) + " is on floor " + elevators[i].getCurrentLocation() + " and has:");
				for (int x = 0; x < elevators[i].getLength(); x++)
				{
					System.out.print(elevators[i].getPerson(x).getName());
					System.out.print(" going to floor ");
					System.out.println(elevators[i].getPerson(x).getDestination());
				}

				System.out.println(" ");
			}
			else
			{
				System.out.println("Elevator " + (i + 1) + " is on floor " + elevators[i].getCurrentLocation() + " and has nobody");
				System.out.println(" ");
			}
		}
	}

	
	/**
	 * Prints the status of all floors and their contents
	 * 
	 * @param floors 	All floors in the simulation
	 */
	private static void printFloors(Floor[] floors)
	{
		for (int i = 0; i < floors.length; i++)
		{
			if (floors[i].getLength() > 0)
			{
				System.out.println("Floor " + (i + 1) + " has");
				for (int x = 0; x < floors[i].getLength(); x++)
				{
					System.out.print(floors[i].getPerson(x).getName());
					System.out.print(" going to floor ");
					System.out.println(floors[i].getPerson(x).getDestination());
				}
			}
			else
			{
				System.out.println("Floor " + (i + 1) + " has nobody");
			}
		}

		System.out.println(" ");
	}

}



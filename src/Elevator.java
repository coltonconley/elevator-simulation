import java.util.ArrayList;
/*********
 * Elevator Class
 * 
 * Stores the direction, destination, location, number, 
 * occupation status and the Person objects it holds
 * 
 * It provides methods to move the elevator, drop off, 
 * and pickup passengers
 * 
 *********/

public class Elevator {

	private boolean currentDirectionIsUp; //true is up
	private ArrayList<Person> peopleInside = new ArrayList<Person>(0); //people inside an elevator
	private int currentDestination;//current destination the elevator is traveling to
	private boolean busy = false;//If an elevator is transporting or going to pick someone up, it is busy
	private int currentLocation;//current floor the elevator is on
	public final int number;//elevator number

/**
 * All setters and getters for the Elevator class
 */
	public Elevator()
	{
		number = -1; //come back to this and delete if all elevators are made with numbers and the numbers are used at some point
		currentLocation = 1;
	}

	public Elevator(int elevatorNumber)
	{
		number = elevatorNumber;
		currentLocation = 1;
	}

	public Elevator(int elevatorNumber, int location)
	{
		number = elevatorNumber;
		currentLocation = location;
		//currentDestination = location; //changed
	}


	public boolean getCurrentDirectionIsUp()
	{
		return currentDirectionIsUp;
	}

	public void setCurrentDirection(boolean direction)
	{
		currentDirectionIsUp = direction;
	}

	public void setCurrentDestination(int destination)
	{
		currentDestination = destination;
	}

	public int getCurrentDestination()
	{
		return currentDestination;
	}

	public void setCurrentLocation(int location)
	{
		currentLocation = location;
	}

	public int getCurrentLocation()
	{
		return currentLocation;
	}

	public void setBusy(boolean isBusy)
	{
		busy = isBusy;
	}

	public boolean isBusy()
	{
		return busy;
	}

	public Person getPerson(int index)
	{
		return peopleInside.get(index);
	}

	public int getLength()
	{
		return peopleInside.size();
	}

	/**
	 * Assigns all relevant attributes of a person to the
	 * elevator picking it up and sets busy equal to true
	 * 
	 * @param person
	 */
	public void assignToPerson(Person person)
	{
		busy = true;
		currentDestination = person.getLocation();
		//determine direction
		if (person.getLocation() > currentLocation)
		{
			currentDirectionIsUp = true;
		}
		else
		{
			currentDirectionIsUp = false;
		}
	}

	/**
	 * determines whether or not to pickup a passenger
	 * 
	 * @param passenger		potential passenger to be picked up
	 * @return				returns whether or not passenger should be picked up
	 */
	public boolean pickupOrNot(Person passenger)
	{
		boolean pickup = false;
		//determine whether or not the passenger should be picked up
		if (currentDirectionIsUp == passenger.getDirectionIsUp() || 
				busy == false || 
				(currentLocation == passenger.getLocation() && peopleInside.size() == 0))
		{
			pickup = true;
		}
		return pickup;

	}

	/**
	 * picks up all passengers from a floor as long as they are traveling in the same direction.
	 * removes passenger from the floor and changes the elevator's status.
	 * 
	 * @param floor		the floor containing potential passengers
	 * @param stats		Statistics object to record pickup
	 */
	public void pickup(Floor floor, Statistics stats)
	{
		int index = 0;

		Person passenger = new Person();
		//check if any person on this floor needs to be picked up
		for(int counter= 0; counter < floor.getLength(); counter++)
		{
			passenger = floor.getPeople().get(index);
			if(pickupOrNot(passenger))
			{
				pickupPerson(floor.getPerson(index), stats);
				floor.removePerson(index);

			}
			else
			{
				index++;
			}
		}
	}

	/**
	 * changes status of an elevator and person it is picking up
	 * 
	 * @param passenger		Person being picked up
	 * @param stats			Statistics object to record pickup
	 */
	private void pickupPerson(Person passenger, Statistics stats)
	{
		stats.addPersonPickedUp(passenger);
		peopleInside.add(passenger);
		busy = true;
		currentDirectionIsUp = passenger.getDirectionIsUp();
		currentDestination = passenger.getDestination();
	}

	/**
	 * Drops off any passengers who are traveling to the current floor.
	 * @param stats		statistics object to record drop off
	 */
	public void dropoff(Statistics stats){
		int length = getLength();
		int index = 0;
		int counter = 0;

		Person passenger = new Person();
		while(counter < length)
		{
			passenger = peopleInside.get(index);
			if(determineDropoff(passenger))
			{
				//change stats
				stats.addPersonDroppedOff(peopleInside.get(index));
				stats.addToNumberOfDropoffs();
				//remove person
				peopleInside.remove(index);
				//change relevant properties
				determineDestinationAndDirectionAfterDropoff();

			}
			else
			{
				index++;
			}
			counter++;
		}
		
	}

	/**
	 * Determines whether or not to drop off a selected passenger
	 * 
	 * @param person	Person to be dropped off
	 * @return			Returns a boolean dictating whether or not to drop off
	 */
	private boolean determineDropoff(Person person)
	{
		boolean dropoff = false;
		//make sure the elevator has arrived at the correct destination floor
		if(person.getDestination() == currentLocation)
		{
			dropoff = true;
		}
		return dropoff;
	}
	/**
	 * Moves an elevator by changing its location
	 * 
	 * @param numFloors	takes number of total floors in simulation
	 */
	public void move(int numFloors)
	{
		if (busy)
		{
			if(currentDirectionIsUp)
			{
				currentLocation ++;
			}
			else
			{
				currentLocation --;
			}
		}
		else
		{
			travelBackToMiddle(numFloors);
		}
	}

	/**
	 * Moves an elevator one floor towards the middle
	 * 
	 * @param numFloors		takes total number of floors in simulation.
	 */
	private void travelBackToMiddle(int numFloors)
	{
		if (currentLocation < (numFloors) / 2)
		{
			currentLocation ++;
		}
		else if (currentLocation > (numFloors) / 2)
		{
			currentLocation --;
		}
	}

	/**
	 * Determines the destination and direction of an elevator 
	 * for use after drop off.  
	 */
	public void determineDestinationAndDirectionAfterDropoff()
	{
		//if there are people still in the elevator, take them to their destination
		if(peopleInside.size() > 0)
		{
			currentDestination = peopleInside.get(0).getDestination();
			currentDirectionIsUp = peopleInside.get(0).getDirectionIsUp();
		}
		else
		{
			busy = false;  
		}


	}


}

/*********
 * Person Class
 * 
 * Stores the name, destination, location, and direction
 * 
 * It provides all setters and getters or private variables as 
 * well as a constructor to assign all attributes to a person at once
 * 
 *********/
public class Person {
	private String name; //person's name
	private int destination; //destination floor number
	private int location;// starting floor number
	private boolean directionIsUp; //true means up
	
	/**
	 * An empty constructor requiring no parameters
	 */
	public Person()
	{
		
	}
	
/**
 * All setters and getters for the Person class
 */
	public String getName()
	{
		return name;
	}
	
	public int getDestination()
	{
		return destination;
	}
	
	public int getLocation()
	{
		return location;
	}
	
	public void setDirectionIsUp(boolean personDirection)
	{
		directionIsUp = personDirection;
	}
	
	public boolean getDirectionIsUp()
	{
		return directionIsUp;
	}
	/**
	 * Constructor for Person class.
	 * initializes a Person object with a name, start floor and a destination
	 * 
	 * @param personName1			name given to person
	 * @param startFloor1			start floor given to person
	 * @param destinationFloor1		destination floor given to person
	 */
	public Person (String personName1, int startFloor1, int destinationFloor1)
	{
		name = personName1;
		location = startFloor1;
		destination = destinationFloor1;
		if(startFloor1 < destinationFloor1)
		{
			directionIsUp = true;
		}
	}
	

}

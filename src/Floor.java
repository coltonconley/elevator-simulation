import java.util.ArrayList;
/*********
 * Floor Class
 * 
 * Stores an ArrayList of people waiting on a floor and a floor number.  
 * 
 * It provides methods to manipulate the ArrayList of people currently on that floor.
 * 
 *********/
public class Floor {
	private ArrayList<Person> people; //people waiting on floor
	public final int number; //floor number (starts at 1)
	
	
	/**
	 * Constructor for Floor class
	 * Assigns a floor number and creates an ArrayList of people
	 * 
	 * @param floorNumber	The number to be designated to the new floor
	 */
	public Floor (int floorNumber)
	{
		number = floorNumber;
		people = new ArrayList<Person>();
	}
	
	/**
	 * Setters and getters for the Floor class
	 */
	public Person getPerson(int index)
	{
		return people.get(index);
	}
	
	public ArrayList<Person> getPeople()
	{
		return people;
	}
	
	
	public int getLength()
	{
		return people.size();
	}
	
	/**
	 * Adds a person to the floor
	 * 
	 * @param newPerson		Person to be added
	 */
	public void addPerson(Person newPerson)
	{
		people.add(newPerson);
	}
	
	/**
	 * removes a person from a floor at a specified index
	 * 
	 * @param index		Index at which a person is removed
	 */
	public void removePerson(int index)
	{
		people.remove(index);
	}
}

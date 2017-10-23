import java.util.ArrayList;

/*********
 * Statistics Class
 * 
 * Stores the number of drop offs, people picked up and people
 * dropped off at a given time click
 * 
 * It provides methods to add to the number of drop offs, get the 
 * number of drop offs, and manipulate the two ArrayLists of people
 * 
 *********/
public class Statistics {

	private int numberOfDropoffs = 0;
	private ArrayList<Person> peopleDroppedOff = new ArrayList<Person>(0);
	private ArrayList<Person> peoplePickedUp = new ArrayList<Person>(0);

	public void addToNumberOfDropoffs()
	{
		numberOfDropoffs++;
	}

	public int getNumberOfDropoffs()
	{
		return numberOfDropoffs;
	}


	public ArrayList<Person> getPeopleDroppedOff()
	{
		return peopleDroppedOff;
	}
	public ArrayList<Person> getPeoplePickedUp()
	{
		return peoplePickedUp;
	}

	/**
	 * Adds a person to the ArrayList of people dropped off
	 * 
	 * @param newPerson		Person to be added
	 */
	public void addPersonDroppedOff(Person newPerson)
	{
		peopleDroppedOff.add(newPerson);
	}

	/**
	 * Adds a person to the ArrayList of people dropped off
	 * 
	 * @param newPerson		Person to be added
	 */
	public void addPersonPickedUp(Person newPerson)
	{
		peoplePickedUp.add(newPerson);
	}
	
	public int getLengthPeoplePickedUp()
	{
		return peoplePickedUp.size();
	}
	
	public int getLengthPeopleDroppedOff()
	{
		return peopleDroppedOff.size();
	}

	/**
	 * Clears the entire people dropped off and picked up ArrayLists
	 */
	public void clearPeopleDroppedOffAndPickedUp()
	{
		int index = 0;
		
		for(int counter = 0; counter < peopleDroppedOff.size(); counter++)
		{
			peopleDroppedOff.remove(index);
		}
		
		for(int counter = 0; counter < peoplePickedUp.size(); counter++)
		{
			peoplePickedUp.remove(index);
		}
	}



}

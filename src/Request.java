/*********
 * Request Class
 * 
 * Stores the time, name, start floor and destination floor for an
 * incoming request
 * 
 * It provides a constructor allowing for all class scope variables to 
 * be filled at once
 * 
 *********/
public class Request {

	public int time; //time the request came in
	public String personName; //person's name
	public int startFloor; //starting floor
	public int destinationFloor;//desired floor
	
	
	/**
	 * Constructor for Request class
	 * assigns a time, name, start floor and destination floor
	 * 
	 * @param time1					Time request was made
	 * @param personName1			Name of person
	 * @param startFloor1			Floor to place person
	 * @param destinationFloor1		End floor
	 */
	public Request (int time1, String personName1, int startFloor1, int destinationFloor1)
	{
		time = time1;
		personName = personName1;
		startFloor = startFloor1;
		destinationFloor = destinationFloor1;
	}
	
	
}

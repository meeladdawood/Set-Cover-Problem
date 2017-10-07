import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

class Combination 
{ 
	private static int IDCounter = 0; 
	private int ID; 
	private HashSet<Integer> numbers = new HashSet<Integer>();
	private boolean checked = false;
	public Combination ()
	{
		this.ID = IDCounter++;
	}
	public void addNumber (int n) 
	{
		numbers.add(n);
	}
	public int getID () { return this.ID; }
	public HashSet<Integer> getNumbers()
	{
		return numbers;
	}
	public void setChecked() { this.checked = true; } 
	public boolean isChecked() { return this.checked; }
	public int contains (HashSet<Integer> b)
	{
		// How many numbers of this instance of Combination does b contain?
		if (b.isEmpty() || this.numbers.isEmpty()) return 0;
		HashSet<Integer> temporary = new HashSet<Integer>(numbers);
		int startSize = temporary.size();
		temporary.removeAll(b);
		return (startSize - temporary.size());
	}
	public int notContains (HashSet<Integer> c)
	{
		// How many numbers of this instance is c missing?
		if (c.isEmpty()) return numbers.size();
		HashSet<Integer> temporary = new HashSet<Integer>(numbers);
		temporary.removeAll(c);
		return temporary.size();
	}
}

public class TRIAL2
{
	static int smallest = Integer.MAX_VALUE;
	static ArrayList<Combination> possibilities = new ArrayList<Combination>(); 
	public static void main (String [] args)
	{
		int n = 10, k = 3, j = 3, l = 2;
		
		// Setup ticket range
		int[] elements = new int[n];
		for (int index = 0; index < n; index++)
			elements[index] = index + 1; 

		// Setup promised number range
		int[] givenData = new int[n];

		// Generate combinations of promised numbers j
		generateRecursive(elements, givenData, 0, n-1, 0, j);
	
			
		// Check off possibilities with tickets 
		for (Combination heil: possibilities)
		{
			System.out.println(heil.getNumbers());
		}
		System.out.println(possibilities.size());
		
		LinkedList<HashSet<Integer>> results = generateTickets(l, k);
		// DFS Search all combinations for possible smallest values
		// THIS IS IT, TAKES ALL THE TIME. 
		
		for(HashSet<Integer> d :results)
		{
			System.out.println(d.toString());
		}

	}
	
	static LinkedList<HashSet<Integer>> generateTickets (int requiredToContain, int ticketSize)
	{
		LinkedList<HashSet<Integer>> toReturn = new LinkedList<HashSet<Integer>>();
		boolean cont = true;
		while (cont)
		{
			HashSet<Integer> ticket = new HashSet<Integer>();
			System.out.println("New Ticket");
			boolean denyEntry = false;
			for (int index = 0; index < possibilities.size(); index++)
			{
				Combination toCheck = possibilities.get(index);
				if (!toCheck.isChecked()) // We are only operating on unchecked combinations
				{
					System.out.println("The match needs " + 
										(requiredToContain - (ticketSize - ticket.size()))
										+ " and toCheck has " + toCheck.contains(ticket));
					if (ticket.isEmpty()) 
					{
						// If the ticket is empty, get numbers of the first unchecked ticket
						ticket.addAll(toCheck.getNumbers());
						toCheck.setChecked();
						if (ticket.size() == ticketSize)
							denyEntry = true;
						System.out.println("Ticket now contains " + ticket.toString() + " and size = " + ticket.size());
					}
					else if (requiredToContain - (ticketSize - ticket.size()) <= toCheck.contains(ticket))//((ticket.size() - requiredToContain) == toCheck.contains(ticket)) 
					{
						if (!denyEntry) 
						{
							ticket.addAll(toCheck.getNumbers());
							System.out.println("Ticket now contains " + ticket.toString() + " and size = " + ticket.size());
							if (ticket.size() == ticketSize)
								denyEntry = true;
						}
						toCheck.setChecked();
						System.out.println("checked off " + toCheck.getID());
					}
					
				}
				
			}
			if (!ticket.isEmpty())
			{
				toReturn.add(ticket);
				System.out.println("Adding a ticket\t\t " + ticket.toString());
			}
			else cont = false;
		}
		return toReturn;
	}
	
	static void generateRecursive(int arr[], int data[], int start, int end, int index, int r)
	{
		if (index == r)
		{
			Combination toAdd = new Combination();
			for (int j=0; j<r; j++)
			{
				//System.out.print(data[j]+" ");
				toAdd.addNumber(data[j]);
			}
				possibilities.add(toAdd);
		
		}
		for (int i=start; i<=end && end-i+1 >= r-index; i++)
		{
			data[index] = arr[i];
			generateRecursive(arr, data, i+1, end, index+1, r);
		}
	}
}

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.LinkedList;

class Combination 
{ 
	private static int IDCounter = 0; 
	private int ID; 
	private TreeSet<Integer> numbers = new TreeSet<Integer>();
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
	public TreeSet<Integer> getNumbers()
	{
		return numbers;
	}
	public void setChecked() { this.checked = true; } 
	public boolean isChecked() { return this.checked; }
	public int contains (TreeSet<Integer> b)
	{
		// How many numbers of this instance of Combination does b contain?
		if (b.isEmpty() || this.numbers.isEmpty()) return 0;
		TreeSet<Integer> temporary = new TreeSet<Integer>(numbers);
		int startSize = temporary.size();
		temporary.removeAll(b);
		return (startSize - temporary.size());
	}
	// REDUNDANT XD
	public int notContains (TreeSet<Integer> c)
	{
		// How many numbers of this instance is c missing?
		if (c.isEmpty()) return numbers.size();
		TreeSet<Integer> temporary = new TreeSet<Integer>(numbers);
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
		int n = 5, k = 4, j = 2, l = 2;
		// n >= k >= j >= l
		
		// Setup ticket range
		int[] elements = new int[n];
		for (int index = 0; index < n; index++)
			elements[index] = index + 1; 

		// Setup promised number range
		int[] givenData = new int[n];

		// Generate combinations of promised numbers j
		generateRecursive(elements, givenData, 0, n-1, 0, j);
	
			
		// Check off possibilities with tickets 
		for (Combination s: possibilities)
		{
			System.out.println(s.getNumbers());
		}
		System.out.println(possibilities.size());
		
		
		// DO THE ENTIRE THING
		LinkedList<TreeSet<Integer>> results = generateTickets(l, k, n);
		
		// PRINT RESULTS
		System.out.println("RESULTS------------------");
		System.out.println("Number of Tickets == " + results.size());
		for(TreeSet<Integer> d :results)
		{
			System.out.println(d.toString());
		}

	}
	
	static LinkedList<TreeSet<Integer>> generateTickets (int requiredToContain, int ticketSize, int n)
	{
		LinkedList<TreeSet<Integer>> toReturn = new LinkedList<TreeSet<Integer>>();
		boolean cont = true;
		while (cont)
		{
			TreeSet<Integer> ticket = new TreeSet<Integer>();
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
				if (ticket.size() < ticketSize)
				{
					/* 	Runs only for the final ticket. If it works but is not
					 * 	full, fill with random values. 
					 */
					for (int addExtra = 1; addExtra <= n && ticket.size() < ticketSize; addExtra++)
					{
						if (!ticket.contains(addExtra)) ticket.add(addExtra);
					}
				}
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

import java.util.*;

class Ticket
{
	private static int IDCounter = 0;
	private int ID;  
	private HashSet<Integer> numbers = new HashSet<Integer>();
	public Ticket ()
	{
		this.ID = IDCounter++;
	}
	public Ticket (int id)
	{
		this.ID = id;
	}
	public void addNumber(int a)
	{
		numbers.add(a);
	}
	public int getID() { return this.ID; }
	public HashSet<Integer> getNumbers()
	{
		return numbers;
	}
	public boolean contains (Combination b, int requiredToContain)
	{
		//Ticket a contains ticket b
		if (this.numbers.isEmpty()) return false;
		int contains = 0;
		for (Integer number: b.getNumbers())
		{
			if (this.numbers.contains(number))
				contains++;
			if (contains >= requiredToContain) 
				return true;
		}
		return false;
	}
}
class Combination 
{ 
	private static int IDCounter = 0; 
	private int ID; 
	private HashSet<Integer> numbers = new HashSet<Integer>();
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
}

public class TRIAL 
{
	static int smallest = Integer.MAX_VALUE;
	static LinkedList<Ticket> bestPath = null;
	static ArrayList<Ticket> everything = new ArrayList<Ticket>();
	static ArrayList<Combination> possibilities = new ArrayList<Combination>(); 
	public static void main (String [] args)
	{
		int n = 5, k = 4, j = 2, l = 2;
		
		// Setup ticket range
		int[] elements = new int[n];
		for (int index = 0; index < n; index++)
			elements[index] = index + 1; 
		int [] data = new int [n];
		
		// Generate Possible Tickets of size k
		generateRecursive(elements, data, 0, n-1, 0, k, true);
		for (Ticket e:everything)
		{
			System.out.println(e.getID() + ":  " + e.getNumbers());
		}
		for (int checker:elements)
			System.out.print(checker + " ");
		System.out.println();
		
		// Setup promised number range
		int[] givenData = new int[n];

		// Generate combinations of promised numbers j
		generateRecursive(elements, givenData, 0, n-1, 0, j, false);
	
			
		// Check off possibilities with tickets 
		for (Combination s: possibilities)
		{
			System.out.println(s.getNumbers());
		}
		System.out.println(possibilities.size());
		
		
		// DFS Search all combinations for possible smallest values
		// THIS IS IT, TAKES ALL THE TIME. 
		runningSumBruteForce(new Ticket(-1), null, elements, l);
		
		System.out.println("RESULTS-----------------------");
		System.out.println("Smallest = " + smallest);
		if (bestPath != null)
		{
			for (Ticket winner:bestPath) 
				System.out.println(winner.getNumbers());
		}
	}
	
	static LinkedList<Ticket> runningSumBruteForce (Ticket root, LinkedList<Ticket> decisions, int[] data, int requiredToContain)
	{
		if (decisions == null) decisions = new LinkedList<Ticket>();
		
		//System.out.print("Checking " + root.getID() + " with ");
		//for (Ticket che:decisions)
		//	System.out.print(che.getID()+ " " );
		//System.out.println();
		// Check the combinations on this list of tickets
		boolean allFound = true;
		for (Combination each:possibilities)
		{
			if (!root.contains(each, requiredToContain))
			{
				boolean foundElsewhere = false;
				for (Ticket checkDecisions:decisions)
				{
					if (checkDecisions.contains(each, requiredToContain))
					{
						foundElsewhere = true;
						break;
					}
				}
				if (!foundElsewhere) allFound = false;
			}
		}
		
		
		// Not all combinations are covered, call recursively on larger sets
		if (!allFound) 
		{
			for (int index = root.getID() + 1; index < everything.size(); index++)
			{
				LinkedList<Ticket> newDecisions = new LinkedList<Ticket>(decisions);
				if (root.getID() != -1) 
					newDecisions.add(root);
				LinkedList<Ticket> found = runningSumBruteForce(everything.get(index), newDecisions, data, requiredToContain);
				if (found != null) 
				{
					if (found.size() < smallest)
					{
						bestPath = new LinkedList<Ticket>(found);
						smallest = found.size();
						System.out.println("FOUND NEW SMALLEST: " + smallest);

					}
				}
			}
			return null;
		}
		else 
		{
			// All combinations are covered, return the decisions list and root
			decisions.add(root);
			/*System.out.println("Found a working one");
			for (Ticket d:decisions) 
				System.out.println(d.getNumbers());*/
			return decisions;
		}
	}
	
	static void generateRecursive(int arr[], int data[], int start, int end, int index, int r, boolean ticket)
	{
		if (index == r)
		{
			if (ticket) 
			{
				Ticket toAdd = new Ticket();
				for (int j=0; j<r; j++)
				{
					//System.out.print(data[j]+" ");
					toAdd.addNumber(data[j]);
				}
				 everything.add(toAdd);	
			}
			else 
			{
				Combination toAdd = new Combination();
				for (int j=0; j<r; j++)
				{
					//System.out.print(data[j]+" ");
					toAdd.addNumber(data[j]);
				}
				possibilities.add(toAdd);
			}
		}
		for (int i=start; i<=end && end-i+1 >= r-index; i++)
		{
			data[index] = arr[i];
			generateRecursive(arr, data, i+1, end, index+1, r, ticket);
		}
	}
}

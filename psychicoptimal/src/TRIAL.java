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
	public void addNumber(int a)
	{
		numbers.add(a);
	}
	public int getID() { return this.ID; }
	public HashSet<Integer> getNumbers()
	{
		return numbers;
	}
	public static void IDReset () 
	{
		IDCounter = 0;
	}
	public boolean contains (Combination b, int requiredToContain)
	{
		//Ticket a contains ticket b
		//HashSet <Integer> aNumbers = this.getNumbers();
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
	static ArrayList<Ticket> everything = new ArrayList<Ticket>();
	static ArrayList<Combination> possibilities = new ArrayList<Combination>(); 
	public static void main (String [] args)
	{
		int n = 4, k = 3, j = 2, l = 2;
		//Set <Integer> something = new TreeSet <Integer>();
		
		// Setup ticket range
		int[] elements = new int[n];
		for (int index = 0; index < n; index++)
			elements[index] = index + 1; 
		int [] data = new int [n];
		generateRecursive(elements, data, 0, n-1, 0, k, true);
		for (Ticket e:everything)
		{
			System.out.println(e.getID() + ":  " + e.getNumbers());
		}
		for (int checker:elements)
			System.out.print(checker + " ");
		System.out.println();
		
		
		// Setup pro mised number range
		int[] givenData = new int[n];

		// Generate combinations of promised numbers
		generateRecursive(elements, givenData, 0, n-1, 0, j, false);
	
			
		// Check off possibilities with tickets 
		for (Combination heil: possibilities)
		{
			System.out.println(heil.getNumbers());
		}
		System.out.println(possibilities.size());
		
		
		runningSumBruteForce(everything.get(0), null, elements, l);
		/*
		for (int indexC = 0; indexC < possibilities.size(); indexC++)
		{
			Ticket e = possibilities.get(indexC);
			System.out.println(e.getID() + ":  " + e.getNumbers() + ", " + possibilityScan.get(indexC).toString());
		}
		
		*/
		
		
		
		/*
		// Check entries with brute-force
		int smallest = everything.size();
		ArrayList<Integer> smallestSet = new ArrayList<Integer>();
		for (int indexToCheck = 0; indexToCheck < everything.size(); indexToCheck++)
		{
			System.out.println("Starting smallest = " + smallest);
			ArrayList<Integer> decisions = new ArrayList<Integer>();
			decisions.add(everything.get(indexToCheck).getID());
			HashSet<Integer> runningSum = new HashSet<Integer>();
			runningSum.addAll(everything.get(indexToCheck).getNumbers());
			
			System.out.println("Checking: " + decisions.get(0));
			boolean alreadyDone = true;
			for (int checkNext: elements)
			{
				if (!runningSum.contains(checkNext))
				{
					// Not quite
					alreadyDone = false;
					break;
				}
			}
			if (alreadyDone) System.out.println("done early");
			if (!alreadyDone || alreadyDone)
			{
				for (int ii = indexToCheck+1; ii < everything.size(); ii++)
				{
					boolean [] passDown = new boolean[everything.size()];
					passDown[indexToCheck] = true;
					boolean []  result = runningSumTrial(everything.get(indexToCheck), everything.get(ii), 
						 passDown, runningSum, elements, smallest);

					int smallestSum = 0;
					for (int bb = 0; bb < everything.size(); bb++)
					{
						if (result[bb] == true) smallestSum++;
					}
					if (smallestSum < smallest)
					{
						smallestSet.clear();
						smallest = smallestSum;
						System.out.print("found a new smallest, at " + smallest + ":\nSmallest-- ");
						for (int zz = 0; zz < result.length; zz++)
						{
							if (result[zz]) 
							{
								smallestSet.add(zz);
								System.out.print(zz + " ");
							}
						}
						System.out.println("root = " + ii);
						
					}
				}
				//ArrayList<Integer> result = runningSumCalculator(everything.get(indexToCheck), decisions, runningSum, elements, smallest);
			}
			//System.out.println("Iteration done");
		}
		System.out.println("done, smallest = " + smallest + ", smallest set = " + smallestSet);
		// CHECKING OUT A BFS FOR SHORTEST, CHECKCOMBINATION )IS CURRENTLY A DFS
		//checkCombinationBFS();
		 */
		 
	}
	
	static LinkedList<Ticket> runningSumBruteForce (Ticket root, LinkedList<Ticket> decisions, int[] data, int requiredToContain)
	{
		if (decisions == null) decisions = new LinkedList<Ticket>();
		
		System.out.print("Checking " + root.getNumbers() + " with ");
		for (Ticket che:decisions)
			System.out.print(che.getNumbers() + " " );
		System.out.println();
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
			//System.out.println("Root contains " + each.getNumbers());
		}
		
		
		// Not all combinations are covered, call recursively on larger sets
		if (!allFound) 
		{
			System.out.println("Not all were found");
			for (int index = root.getID() + 1; index < everything.size(); index++)
			{
				LinkedList<Ticket> newDecisions = decisions;
				newDecisions.add(root);
				LinkedList<Ticket> found = runningSumBruteForce(everything.get(index), newDecisions, data, requiredToContain);
				if (found != null) 
				{
					/*System.out.println("Found a winner:" );
					for (Ticket winner:found) 
						System.out.println(winner.getNumbers());
					System.out.println("END WINNER");
					*/
				}
			}
			return null;
		}
		else 
		{
			// All combinations are covered, return the decisions list
			System.out.println("Found a working one");
			System.out.println(root.getNumbers());
			for (Ticket d:decisions) 
				System.out.println(d.getNumbers());
			return decisions;
		}
	}
	
	
	
	
	
	static void generateRecursive(int arr[], int data[], int start, int end, int index, int r, boolean ticket)
	{
		if (index == r)
		{
			//ArrayList<Integer> entry = new ArrayList<Integer> ();
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
	
static boolean[]  runningSumTrial (Ticket root, Ticket target, boolean[] decisions, HashSet<Integer> runningSum, int[] data, int smallest)
{
	if (target.getID() != root.getID()&& decisions[target.getID()] == false)
	{
		decisions[target.getID()] = true;
		runningSum.addAll(target.getNumbers());
		System.out.print("Checking: ");
		for (int i = 0; i < decisions.length; i++)
		{
			if (decisions[i])
				System.out.print(i + " ");
		}
		System.out.println();
		
		boolean containsAll = true;
		for (int checkNext: data)
		{
		
			if (!runningSum.contains(checkNext))
			{
				containsAll = false;
				break;
			}
		}
		if (!containsAll) // CHANGE THIS LATER
		{
			for (Ticket a:everything)
			{
				if (a.getID() > target.getID())
				{
					//System.out.println("going to call on ROOT = " + root.getID() +  ", target = " + a.getID());
					boolean[] newResults = runningSumTrial(target, a, decisions, runningSum, data, smallest);
					int smallestSum = newResults.length;
					for (int bb = 0; bb < decisions.length; bb++)
					{
						if (newResults[bb] == true) smallestSum++;
					}
					if (smallestSum < smallest) 
					{
						smallest = smallestSum;
						System.out.println("found a new smallest INSIDE, at " + smallest + ": ");
						for (int zz = 0; zz < newResults.length; zz++)
							if (newResults[zz]) System.out.print(zz + " ");
						System.out.println();
					}
				}
			}
		}
	}
	//System.out.println("root and target not working: " + root.getID() + ", " + target.getID());
	return decisions; 

}
}

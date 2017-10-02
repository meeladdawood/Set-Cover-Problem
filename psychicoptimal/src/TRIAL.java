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
}

public class TRIAL 
{
	static ArrayList<ArrayList<Integer>> results = new ArrayList<ArrayList<Integer>>();
	static ArrayList<Ticket> everything = new ArrayList<Ticket>();
	public static void main (String [] args)
	{
		int n = 5, k = 3, j = 3, i = 2;
		//Set <Integer> something = new TreeSet <Integer>();
		int[] elements = new int[n];
		for (int index = 0; index < n; index++)
			elements[index] = index + 1; 
		int [] data = new int [n];
		generateRecursive(elements, data, 0, n-1, 0, k);
		
		for (Ticket e:everything)
		{
			System.out.println(e.getID() + ":  " + e.getNumbers());
		}
		for (int checker:elements)
			System.out.print(checker + " ");
		System.out.println();

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
		// CHECKING OUT A BFS FOR SHORTEST, CHECKCOMBINATION IS CURRENTLY A DFS
		//checkCombinationBFS();
	}
/*	static ArrayList<Integer> runningSumCalculator (Ticket root, ArrayList<Integer> decisions, HashSet<Integer> runningSum, int[] data, int smallest)
	{
		for (Ticket others:everything)
		{
			if (others.getID() != root.getID() && !decisions.contains(others.getID()))
			{
				decisions.add(others.getID());
				runningSum.addAll(others.getNumbers());
				
				System.out.print("Checking: ");
				for (int i = 0; i < decisions.size(); i++)
				{
					System.out.print((decisions.get(i)) + " ");
				}
				System.out.println();
				boolean containsAll = true;
				for (int checkNext: data)
				{
				
					if (!runningSum.contains(checkNext))
					{
						// Not quite
						//System.out.println("does not contain " + checkNext);
						containsAll = false;
						break;
					}
				}
				if (!containsAll || containsAll) // CHANGE THIS LATER
				{
					for (Ticket a:everything)
					{
						ArrayList<Integer> results = runningSumCalculator(others, decisions, runningSum, data, smallest);
						if (results.size() < smallest) 
						{
							smallest = results.size();

						}
					}
				}
			}
			//System.out.println("one repeated or root: " + others.getID())
		}
		return decisions; 

	}*/
	static void generateRecursive(int arr[], int data[], int start, int end, int index, int r)
	{
		if (index == r)
		{
			//ArrayList<Integer> entry = new ArrayList<Integer> ();
			Ticket toAdd = new Ticket();
			for (int j=0; j<r; j++)
			{
				//System.out.print(data[j]+" ");
				toAdd.addNumber(data[j]);
			}
			everything.add(toAdd);
			return;
		}
		for (int i=start; i<=end && end-i+1 >= r-index; i++)
		{
			data[index] = arr[i];
			generateRecursive(arr, data, i+1, end, index+1, r);
		}
	}

	//static boolean checkCombinationHelper (int factor, int index, int[] data)
	//{
		/*
		for (int sumOf = 0; sumOf <= factor; sumOf++)
		{
			System.out.println("FACTOR " + sumOf);
			for (int start = 0; start < results.size(); start++)
			{
				for (int add = start + 1; add < results.size(); add++)
				{
				System.out.println("start = " + start );
				for (Integer s: checkCombination(sumOf, start, add, data))
					System.out.print(s + " ");					
				System.out.println();
				}
			}
		}
		return false;
		
		
	}
	static HashSet<Integer> checkCombination (int factor, int startIndex, int add, int[] data)
	{
		if (factor == 0)
			return everything.get(startIndex).getNumbers();
		else 
		{
			HashSet<Integer> toReturn = new HashSet<Integer>();
			toReturn.addAll(everything.get(startIndex).getNumbers());
			toReturn.addAll(checkCombination(factor - 1, startIndex + add, add--, data));
			return toReturn;
		}
	}
	*/
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
/*	static ArrayList<Ticket> checkCombinationBFS (int [] data, int layersDown)
	{
		Queue<Ticket> toCheck = new PriorityQueue<Ticket>();
		for (Ticket e:everything)
			toCheck.add(e);
		while (!toCheck.isEmpty())
		{
			Ticket check = toCheck.poll();
			for (Ticket allOthers:everything)
			{
				if (check.getID() != allOthers.getID())
				{
					ArrayList<Ticket> combo = new ArrayList<Ticket> ();
					combo.add(check);
					
					
					// RECURSIVELY ADD ALL OTHERS TO CHECK
					//combo.addAll(allOthers.getNumbers());
					for (Integer another: (checkSingle(check, layersDown)))
					{
						combo.add(another);
					}
					
					

					// Check combo set for all entries
					boolean works = true;
					for (int b: data)
					{
						if (!combo.contains(b))
						{
							works = false;
							break;
						}
					}
					if (works) 
					{
						return combo;
					}
					else return null;
				}
			}
		}
	}
	static ArrayList<Integer> checkSingle(Ticket target, int layer)
	{
		Queue<Ticket> next = new PriorityQueue<Ticket>();
		
	}
}
*/
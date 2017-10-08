import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;


class TicketComparator<E> implements Comparator<E>
{
	public int compare(Object a, Object b)
	{
		String aa = (String)a;
		String bb = (String)b;
		String[] aarr = aa.split(" ");
		String[] barr = bb.split(" ");
		for (int i = 0; i < aarr.length; i++)
		{
			int check1 = Integer.parseInt(aarr[i]);
			int check2 = Integer.parseInt(barr[i]);
			//System.out.println("");
			int toReturn = check1 - check2;
			if (toReturn < 0)
				return -1; 
			else if (toReturn > 0)
				return 1;
		}
		return 0;
	}
}
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
		int n = 0, k = 0, j = 0, l = 0; 
		String inputName = args[0], outputName = args[1];
		try 
		{
			File inFile = new File(inputName);
			Scanner in = new Scanner(inFile);
			n = in.nextInt(); 
			j = in.nextInt();
			k = in.nextInt(); 
			l = in.nextInt();
			in.close();
		}
		catch (FileNotFoundException d)
		{
			System.out.println("File not found!");
		}
		
		// n >= k >= j >= l
		
		if (n < k || k < j || j < l || n == 0 || k == 0 || j == 0 || l == 0) 
		{
			// BAD ARGUMENTS
			System.out.println("Bad values for n, j, k, l!");
			System.exit(1); 
		}
		
		// Setup ticket range
		int[] elements = new int[n];
		for (int index = 0; index < n; index++)
			elements[index] = index + 1; 
		int [] data = new int [n];
		
		// Generate Possible Tickets of size k
		generateRecursive(elements, data, 0, n-1, 0, k, true);
		
		// Setup promised number range
		int[] givenData = new int[n];

		// Generate combinations of promised numbers j
		generateRecursive(elements, givenData, 0, n-1, 0, j, false);
		
		
		// DFS Search all combinations for possible smallest values
		// THIS IS IT, TAKES ALL THE TIME. 
		runningSumBruteForce(new Ticket(-1), null, elements, l);
		
		ArrayList<String> resultStrings = new ArrayList<String>(); 
		Collections.sort(resultStrings);
		for (Ticket d: bestPath)
		{
			String dd = d.getNumbers().toString();
			dd = dd.replaceAll("([\\[,\\]])", "");
			resultStrings.add(dd);
		}
		resultStrings.sort(new TicketComparator<String>());
		// YO DOG
		try
		{
			File out = new File (outputName);
			out.createNewFile();
			PrintWriter print = new PrintWriter (out);
			print.println(smallest);
			//System.out.println(smallest);
			for (String str : resultStrings)
			{
				//System.out.println(str);
				print.println(str);
			}
			print.close();
		}
		catch (Exception exception) 
		{
			System.out.println("Unable to print to file");
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
						//System.out.println("FOUND NEW SMALLEST: " + smallest);
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

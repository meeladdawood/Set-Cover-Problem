import java.io.*;
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
}

public class TRIAL2
{
	static int smallest = Integer.MAX_VALUE;
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

		// Setup promised number range
		int[] givenData = new int[n];

		// Generate combinations of promised numbers j
		generateRecursive(elements, givenData, 0, n-1, 0, j);
	

		// DO THE ENTIRE THING
		LinkedList<TreeSet<Integer>> results = generateTickets(l, k, n);
		ArrayList<String> resultStrings = new ArrayList<String>(); 
	//	Collections.sort(resultStrings);
		for (TreeSet<Integer> d: results)
		{
			LinkedList<Integer> dd = new LinkedList<Integer>(); 
			for (Integer ddd: d)
				dd.add(ddd);
			Collections.sort(dd);
			String entry = dd.toString();
			entry = entry.replaceAll("([\\[,\\]])", "");
			resultStrings.add(entry);
		}	
		
		resultStrings.sort(new TicketComparator<String>());
		// SEND RESULTS TO OUTFILE
		try
		{
			File out = new File (outputName);
			PrintWriter print = new PrintWriter (out);
			print.println(results.size());
			for (String a: resultStrings)
			{
				//System.out.println(a);
				print.println(a);
			}
			print.close();
		}
		catch (Exception exception) {}
		

	}
	
	static LinkedList<TreeSet<Integer>> generateTickets (int requiredToContain, int ticketSize, int n)
	{
		LinkedList<TreeSet<Integer>> toReturn = new LinkedList<TreeSet<Integer>>();
		boolean cont = true;
		while (cont)
		{
			TreeSet<Integer> ticket = new TreeSet<Integer>();
			boolean denyEntry = false;
			for (int index = 0; index < possibilities.size(); index++)
			{
				Combination toCheck = possibilities.get(index);
				if (!toCheck.isChecked()) // We are only operating on unchecked combinations
				{
					if (ticket.isEmpty()) 
					{
						// If the ticket is empty, get numbers of the first unchecked ticket
						TreeSet<Integer> numb = toCheck.getNumbers();
						Iterator<Integer> it = numb.iterator();
						while (ticket.size() < ticketSize && it.hasNext())
						{
							ticket.add(it.next());
						}
						toCheck.setChecked();
						if (ticket.size() == ticketSize)
							denyEntry = true;
					}
					else if (requiredToContain - (ticketSize - ticket.size()) <= toCheck.contains(ticket))//((ticket.size() - requiredToContain) == toCheck.contains(ticket)) 
					{
						if (!denyEntry) 
						{
							TreeSet<Integer> numb = toCheck.getNumbers();
							Iterator<Integer> it = numb.iterator();
							while (ticket.size() < ticketSize && it.hasNext())
							{
								ticket.add(it.next());
							}
							if (ticket.size() == ticketSize)
								denyEntry = true;
						}
						toCheck.setChecked();
					}
					
				}
				
			}
			// Comment 
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

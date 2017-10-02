import java.util.*;

public class TRIAL 
{
	static ArrayList<ArrayList<Integer>> results = new ArrayList<ArrayList<Integer>>();
	public static void main (String [] args)
	{
		int n = 5, k = 3, j = 3, i = 2;
		//Set <Integer> something = new TreeSet <Integer>();
		int[] elements = new int[n];
		for (int index = 0; index < n; index++)
			elements[index] = index + 1; 
		int [] data = new int [n];
		generateRecursive(elements, data, 0, n-1, 0, k);
		/*for (ArrayList<Integer> a : results)
		{
			for (Integer d: a)
				System.out.print(d + " ");
			System.out.println();
		}
		*/
		
		// Check entries with brute-force
		int factor = 0; 
		checkCombinationHelper(2, data);
	}
	static void generateRecursive(int arr[], int data[], int start, int end, int index, int r)
	{
		if (index == r)
		{
			ArrayList<Integer> entry = new ArrayList<Integer> ();

			for (int j=0; j<r; j++)
			{
				//System.out.print(data[j]+" ");
				entry.add(data[j]);
			}
			results.add(entry);
			return;
		}
		for (int i=start; i<=end && end-i+1 >= r-index; i++)
		{
			data[index] = arr[i];
			generateRecursive(arr, data, i+1, end, index+1, r);
		}
	}
	static boolean checkCombinationHelper (int factor, int[] data)
	{
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
	static ArrayList<Integer> checkCombination (int factor, int startIndex, int add, int[] data)
	{
		if (factor == 0)
			return results.get(startIndex);
		else 
		{
			ArrayList<Integer> toReturn = new ArrayList<Integer>();
			toReturn.addAll(results.get(startIndex));
			toReturn.addAll(checkCombination(factor - 1, startIndex + add, add--, data));
			return toReturn;
		}
	}
}

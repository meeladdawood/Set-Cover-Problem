import java.util.*;

public class TRIAL 
{
	public static void main (String [] args)
	{
		int n = 14, k = 3, j = 3, i = 2;
		//Set <Integer> something = new TreeSet <Integer>();
		int[] elements = new int[n];
		for (int index = 0; index < n; index++)
			elements[index] = index; 
		int [] data = new int [n];
		combinationUtil(elements, data, 0, n-1, 0, k);
	}
	static void combinationUtil(int arr[], int data[], int start, int end, int index, int r)
	{
		// Current combination is ready to be printed, print it
		if (index == r)
		{
			for (int j=0; j<r; j++)
				System.out.print(data[j]+" ");
			System.out.println("");
			return;
		}
		for (int i=start; i<=end && end-i+1 >= r-index; i++)
		{
			data[index] = arr[i];
			combinationUtil(arr, data, i+1, end, index+1, r);
		}
	}
}

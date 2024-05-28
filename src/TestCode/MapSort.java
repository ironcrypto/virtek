package TestCode;

import java.io.PrintStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MapSort
{
  public static void main(String[] args)
  {
    Map<Long, MyObject> myMap = new LinkedHashMap();
    
    myMap.put(Long.valueOf(1L), new MyObject(1L, 26.0D, "Mat"));
    myMap.put(Long.valueOf(4L), new MyObject(4L, 25.0D, "Tommy"));
    myMap.put(Long.valueOf(16L), new MyObject(16L, 24.0D, "Kate"));
    myMap.put(Long.valueOf(63L), new MyObject(63L, 26.0D, "Mary"));
    myMap.put(Long.valueOf(99L), new MyObject(99L, 24.0D, "Ronny"));
    
    System.out.println("Before Sorting");
    System.out.println(myMap);
    System.out.println("\nAfter Sorting");
    System.out.println(sortMap(myMap));
    
    System.out.println("Starting map");
    System.out.println(myMap);
  }
  
  private static Map<Long, MyObject> sortMap(Map<Long, MyObject> unsortedMap)
  {
    List<Map.Entry<Long, MyObject>> list = new LinkedList(unsortedMap.entrySet());
    
    Collections.sort(list, new Comparator()
    {
      public int compare(Map.Entry<Long, MyObject> o1, Map.Entry<Long, MyObject> o2)
      {
        int priceResult = Double.valueOf(((MyObject)o2.getValue()).getPrice()).compareTo(Double.valueOf(((MyObject)o1.getValue()).getPrice()));
        if (priceResult != 0) {
          return priceResult;
        }
        return Long.valueOf(((MyObject)o1.getValue()).getDate()).compareTo(Long.valueOf(((MyObject)o2.getValue()).getDate()));
      }

	@Override
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		return 0;
	}
    });
    Map<Long, MyObject> sortedMap = new LinkedHashMap();
    for (Map.Entry<Long, MyObject> item : list) {
      sortedMap.put(item.getKey(), item.getValue());
    }
    return sortedMap;
  }
}

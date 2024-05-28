package TestCode;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

final class MapSort$1
  implements Comparator<Map.Entry<Long, MyObject>>
{
  public int compare(Map.Entry<Long, MyObject> o1, Map.Entry<Long, MyObject> o2)
  {
    int priceResult = Double.valueOf(((MyObject)o2.getValue()).getPrice()).compareTo(Double.valueOf(((MyObject)o1.getValue()).getPrice()));
    if (priceResult != 0) {
      return priceResult;
    }
    return Long.valueOf(((MyObject)o1.getValue()).getDate()).compareTo(Long.valueOf(((MyObject)o2.getValue()).getDate()));
  }
}

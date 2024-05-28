package TestCode;

import java.util.Comparator;

class PriorityQueueComparator
  implements Comparator<PQExample>
{
  public int compare(PQExample o1, PQExample o2)
  {
    if (o1.price < o2.price) {
      return 1;
    }
    if (o1.price > o2.price) {
      return -1;
    }
    if (o1.date < o2.date) {
      return -1;
    }
    return 1;
  }
}

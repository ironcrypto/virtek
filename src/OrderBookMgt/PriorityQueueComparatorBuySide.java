package OrderBookMgt;

import java.util.Comparator;

class PriorityQueueComparatorBuySide
  implements Comparator<OrderBuySide>
{
  public int compare(OrderBuySide o1, OrderBuySide o2)
  {
    switch (o1.typeOfOrder)
    {
    case 1: 
      if (o1.priceOfOrder < o2.priceOfOrder) {
        return 1;
      }
      if (o1.priceOfOrder > o2.priceOfOrder) {
        return -1;
      }
      if (o1.dateOfOrder < o2.dateOfOrder) {
        return -1;
      }
      return 1;
    }
    return -1;
  }
}

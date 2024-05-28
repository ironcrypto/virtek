package OrderBookMgt;

import java.util.Comparator;

class PriorityQueueComparatorSellSide
  implements Comparator<OrderSellSide>
{
  public int compare(OrderSellSide o1, OrderSellSide o2)
  {
    switch (o1.typeOfOrder)
    {
    case 1: 
      if (o1.priceOfOrder < o2.priceOfOrder) {
        return -1;
      }
      if (o1.priceOfOrder > o2.priceOfOrder) {
        return 1;
      }
      if (o1.dateOfOrder < o2.dateOfOrder) {
        return -1;
      }
      return 1;
    }
    return -1;
  }
}

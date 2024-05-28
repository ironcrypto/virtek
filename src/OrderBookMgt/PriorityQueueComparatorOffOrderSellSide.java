package OrderBookMgt;

import java.util.Comparator;

class PriorityQueueComparatorOffOrderSellSide
  implements Comparator<OffOrderSellSide>
{
  public int compare(OffOrderSellSide o1, OffOrderSellSide o2)
  {
    if (o1.priceLimit < o2.priceLimit) {
      return 1;
    }
    if (o1.priceLimit > o2.priceLimit) {
      return -1;
    }
    if (o1.dateOfOrder < o2.dateOfOrder) {
      return -1;
    }
    return 1;
  }
}

package OrderBookMgt;

import java.util.Comparator;

class PriorityQueueComparatorOffOrderBuySide
  implements Comparator<OffOrderBuySide>
{
  public int compare(OffOrderBuySide o1, OffOrderBuySide o2)
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

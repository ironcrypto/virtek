package OrderBookMgt;

import MarketPlaceMgt.MarketPlace;
import java.util.HashMap;
import java.util.PriorityQueue;

public class OffOrderBuySide
{
  long idOfOrder;
  long idOfTicker;
  long idOfUser;
  long dateOfOrder;
  int typeOfOrder = 2;
  double priceLimit;
  long sizeOfOrder;
  int marketType = 1;
  
  public OffOrderBuySide(long idOfOrder, long idOfTicker, long idOfUser, long dateOfOrder, double priceOfOrder, long sizeOfOrder)
  {
    this.idOfOrder = idOfOrder;
    this.idOfTicker = idOfTicker;
    this.idOfUser = idOfUser;
    this.dateOfOrder = dateOfOrder;
    this.priceLimit = priceOfOrder;
    this.sizeOfOrder = sizeOfOrder;
  }
  
  public static void AddToStopOrderBookBuySide(OffOrderBuySide newOrder)
  {
    OrderBook thisOrderBook = (OrderBook)MarketPlace.orderBookMap.get(Long.valueOf(newOrder.idOfTicker));
    
    thisOrderBook.offOrderBuySidePriorityQueue.add(newOrder);
  }
  
  public boolean equals(Object o)
  {
    if ((o instanceof OffOrderBuySide))
    {
      OffOrderBuySide offOrderBuySide = (OffOrderBuySide)o;
      return this.idOfOrder == offOrderBuySide.idOfOrder;
    }
    return false;
  }
}

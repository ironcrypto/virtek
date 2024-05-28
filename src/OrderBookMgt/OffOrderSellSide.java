package OrderBookMgt;

import MarketPlaceMgt.MarketPlace;
import java.util.HashMap;
import java.util.PriorityQueue;

public class OffOrderSellSide
{
  long idOfOrder;
  long idOfTicker;
  long idOfUser;
  long dateOfOrder;
  int typeOfOrder = 2;
  double priceLimit;
  long sizeOfOrder;
  int marketType = 1;
  
  public OffOrderSellSide(long idOfOrder, long idOfTicker, long idOfUser, long dateOfOrder, double priceOfOrder, long sizeOfOrder)
  {
    this.idOfOrder = idOfOrder;
    this.idOfTicker = idOfTicker;
    this.idOfUser = idOfUser;
    this.dateOfOrder = dateOfOrder;
    this.priceLimit = priceOfOrder;
    this.sizeOfOrder = sizeOfOrder;
  }
  
  public static void AddToStopOrderBookSellSide(OffOrderSellSide newOrder)
  {
    OrderBook thisOrderBook = (OrderBook)MarketPlace.orderBookMap.get(Long.valueOf(newOrder.idOfTicker));
    
    thisOrderBook.offOrderSellSidePriorityQueue.add(newOrder);
  }
  
  public boolean equals(Object o)
  {
    if ((o instanceof OffOrderSellSide))
    {
      OffOrderSellSide offOrderSellSide = (OffOrderSellSide)o;
      return this.idOfOrder == offOrderSellSide.idOfOrder;
    }
    return false;
  }
}

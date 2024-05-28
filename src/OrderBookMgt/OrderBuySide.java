package OrderBookMgt;

import MarketPlaceMgt.MarketPlace;
import java.util.HashMap;
import java.util.PriorityQueue;

public class OrderBuySide
{
  long idOfOrder;
  long idOfTicker;
  long idOfUser;
  long dateOfOrder;
  int typeOfOrder;
  double priceOfOrder;
  long sizeOfOrder;
  long hiddenSize;
  long sizePerSent;
  int marketType;
  
  public OrderBuySide(long idOfOrder, long idOfTicker, long idOfUser, long dateOfOrder, int typeOfOrder, double priceOfOrder, long sizeOfOrder, long hiddenSize, long sizePerSent, int marketType)
  {
    this.idOfOrder = idOfOrder;
    this.idOfTicker = idOfTicker;
    this.idOfUser = idOfUser;
    this.dateOfOrder = dateOfOrder;
    this.typeOfOrder = typeOfOrder;
    this.priceOfOrder = priceOfOrder;
    this.sizeOfOrder = sizeOfOrder;
    this.hiddenSize = hiddenSize;
    this.sizePerSent = sizePerSent;
    this.marketType = marketType;
  }
  
  public static OrderBook GetOrderBook(OrderBuySide newOrder)
  {
    OrderBook thisOrderBook = (OrderBook)MarketPlace.orderBookMap.get(Long.valueOf(newOrder.idOfTicker));
    
    thisOrderBook.orderBuySidePriorityQueue.add(newOrder);
    if (!thisOrderBook.orderSellSidePriorityQueue.isEmpty())
    {
      thisOrderBook.emptyLimitOrderSide = false;
      thisOrderBook = Execution.CheckExec(thisOrderBook, newOrder.typeOfOrder);
    }
    else if ((thisOrderBook.emptyLimitOrderSide) && (newOrder.typeOfOrder > 1))
    {
      ((OrderFlow)MarketPlace.orderFlowMap.get(Long.valueOf(newOrder.idOfOrder))).statusOfOrder = -3;
      thisOrderBook.orderBuySidePriorityQueue.poll();
    }
    return thisOrderBook;
  }
  
  public boolean equals(Object o)
  {
    if ((o instanceof OrderBuySide))
    {
      OrderBuySide orderBuySide = (OrderBuySide)o;
      return this.idOfOrder == orderBuySide.idOfOrder;
    }
    return false;
  }
}

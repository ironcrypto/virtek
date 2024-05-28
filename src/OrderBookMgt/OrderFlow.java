package OrderBookMgt;

import MarketPlaceMgt.MarketPlace;
import MarketPlaceMgt.Securities;
import UniqueId.UniqueLongId;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class OrderFlow
{
  long idOfTicker;
  int dirOfOrder;
  double priceOfOrder;
  long sizeOfOrder;
  long sizePerSent;
  long hiddenSize = 0L;
  int typeOfOrder;
  long sizeInitOfOrder;
  boolean validOrder = false;
  int marketType;
  long idOfOrder;
  int statusOfOrder = -1;
  long idOfTrader;
  long dateOfOrder;
  long executedQty = 0L;
  long orderParent;
  long orderChild;
  long dateOfTransfer;
  
  public OrderFlow(long idOfTicker, int dirOfOrder, double priceOfOrder, long sizeOfOrder, int typeOfOrder, long idOfTrader)
  {
    this.idOfOrder = UniqueLongId.get();
    this.idOfTicker = idOfTicker;
    this.dirOfOrder = dirOfOrder;
    this.sizeOfOrder = sizeOfOrder;
    this.sizeInitOfOrder = sizeOfOrder;
    this.sizePerSent = sizeOfOrder;
    
    this.typeOfOrder = typeOfOrder;
    this.dateOfOrder = new Date().getTime();
    this.idOfTrader = idOfTrader;
    if (MarketPlace.securitiesMap.containsKey(Long.valueOf(idOfTicker)))
    {
      this.marketType = ((Securities)MarketPlace.securitiesMap.get(Long.valueOf(idOfTicker))).marketType;
      this.validOrder = true;
      this.priceOfOrder = Securities.GetTickPrice(priceOfOrder);
    }
  }
  
  public static OrderFlow SendOrder(long idOfTicker, int dirOfOrder, double priceOfOrder, long sizeOfOrder, String typeOfOrder, long idOfTrader)
  {
    OrderFlow newOrder = new OrderFlow(idOfTicker, dirOfOrder, priceOfOrder, sizeOfOrder, ((Integer)MarketPlace.orderType.get(typeOfOrder)).intValue(), idOfTrader);
    AddOrder(newOrder);
    return newOrder;
  }
  
  public static void AddOrder(OrderFlow newOrder)
  {
    if (newOrder.validOrder) {
      switch (newOrder.dirOfOrder)
      {
      case 1: 
        OrderBuySide orderToBuy = new OrderBuySide(newOrder.idOfOrder, newOrder.idOfTicker, newOrder.idOfTrader, newOrder.dateOfOrder, newOrder.typeOfOrder, newOrder.priceOfOrder, newOrder.sizeOfOrder, newOrder.hiddenSize, newOrder.sizePerSent, newOrder.marketType);
        
        newOrder.statusOfOrder = 1;
        MarketPlace.orderFlowMap.put(Long.valueOf(newOrder.idOfOrder), newOrder);
        
        OrderBuySide.GetOrderBook(orderToBuy);
        break;
      case -1: 
        OrderSellSide orderToSell = new OrderSellSide(newOrder.idOfOrder, newOrder.idOfTicker, newOrder.idOfTrader, newOrder.dateOfOrder, newOrder.typeOfOrder, newOrder.priceOfOrder, newOrder.sizeOfOrder, newOrder.hiddenSize, newOrder.sizePerSent, newOrder.marketType);
        
        newOrder.statusOfOrder = 1;
        MarketPlace.orderFlowMap.put(Long.valueOf(newOrder.idOfOrder), newOrder);
        
        OrderSellSide.GetOrderBook(orderToSell);
      }
    } else {
      MarketPlace.orderFlowMap.put(Long.valueOf(newOrder.idOfOrder), newOrder);
    }
  }
  
  public static OrderFlow IcebergOrder(long idOfTicker, int dirOfOrder, double priceOfOrder, long sizeOfOrder, long sizePerSent, String typeOfOrder, long idOfTrader)
  {
    OrderFlow newOrder = new OrderFlow(idOfTicker, dirOfOrder, priceOfOrder, sizeOfOrder, ((Integer)MarketPlace.orderType.get(typeOfOrder)).intValue(), idOfTrader);
    newOrder.hiddenSize = (sizeOfOrder - sizePerSent);
    newOrder.sizeOfOrder = sizePerSent;
    newOrder.sizePerSent = sizePerSent;
    if (newOrder.validOrder) {
      switch (newOrder.dirOfOrder)
      {
      case 1: 
        newOrder.statusOfOrder = 1;
        MarketPlace.orderFlowMap.put(Long.valueOf(newOrder.idOfOrder), newOrder);
        OrderBuySide orderBuySide = new OrderBuySide(newOrder.idOfOrder, newOrder.idOfTicker, newOrder.idOfTrader, newOrder.dateOfOrder, 1, newOrder.priceOfOrder, newOrder.sizeOfOrder, newOrder.hiddenSize, newOrder.sizePerSent, newOrder.marketType);
        
        OrderBuySide.GetOrderBook(orderBuySide);
        break;
      case -1: 
        newOrder.statusOfOrder = 1;
        MarketPlace.orderFlowMap.put(Long.valueOf(newOrder.idOfOrder), newOrder);
        OrderSellSide orderSellSide = new OrderSellSide(newOrder.idOfOrder, newOrder.idOfTicker, newOrder.idOfTrader, newOrder.dateOfOrder, 1, newOrder.priceOfOrder, newOrder.sizeOfOrder, newOrder.hiddenSize, newOrder.sizePerSent, newOrder.marketType);
        
        OrderSellSide.GetOrderBook(orderSellSide);
      }
    } else {
      MarketPlace.orderFlowMap.put(Long.valueOf(newOrder.idOfOrder), newOrder);
    }
    return newOrder;
  }
  
  public static OrderFlow StopOrder(long idOfTicker, int dirOfOrder, double priceOfOrder, long hiddenSize, String typeOfOrder, long idOfTrader)
  {
    OrderFlow newOrder = new OrderFlow(idOfTicker, dirOfOrder, priceOfOrder, 0L, ((Integer)MarketPlace.orderType.get(typeOfOrder)).intValue(), idOfTrader);
    newOrder.hiddenSize = hiddenSize;
    newOrder.sizeInitOfOrder = hiddenSize;
    if (newOrder.validOrder) {
      switch (newOrder.dirOfOrder)
      {
      case 1: 
        newOrder.statusOfOrder = 1;
        MarketPlace.orderFlowMap.put(Long.valueOf(newOrder.idOfOrder), newOrder);
        
        OffOrderBuySide offOrderBuySide = new OffOrderBuySide(newOrder.idOfOrder, newOrder.idOfTicker, newOrder.idOfTrader, newOrder.dateOfOrder, newOrder.priceOfOrder, newOrder.hiddenSize);
        OffOrderBuySide.AddToStopOrderBookBuySide(offOrderBuySide);
        break;
      case -1: 
        newOrder.statusOfOrder = 1;
        MarketPlace.orderFlowMap.put(Long.valueOf(newOrder.idOfOrder), newOrder);
        
        OffOrderSellSide offOrderSellSide = new OffOrderSellSide(newOrder.idOfOrder, newOrder.idOfTicker, newOrder.idOfTrader, newOrder.dateOfOrder, newOrder.priceOfOrder, newOrder.hiddenSize);
        OffOrderSellSide.AddToStopOrderBookSellSide(offOrderSellSide);
      }
    } else {
      MarketPlace.orderFlowMap.put(Long.valueOf(newOrder.idOfOrder), newOrder);
    }
    return newOrder;
  }
  
  public static OrderFlow CancelOrder(long idOfOrder)
  {
    OrderFlow orderToCancel = (OrderFlow)MarketPlace.orderFlowMap.get(Long.valueOf(idOfOrder));
    
    OrderFlow newOrder = new OrderFlow(orderToCancel.idOfTicker, orderToCancel.dirOfOrder, orderToCancel.priceOfOrder, orderToCancel.sizeOfOrder, ((Integer)MarketPlace.orderType.get("Cancel Order")).intValue(), orderToCancel.idOfTrader);
    
    newOrder.sizeInitOfOrder += orderToCancel.executedQty;
    newOrder.executedQty = orderToCancel.executedQty;
    newOrder.orderParent = orderToCancel.idOfOrder;
    newOrder.dateOfTransfer = new Date().getTime();
    if (orderToCancel.statusOfOrder > 0) {
      switch (orderToCancel.dirOfOrder)
      {
      case 1: 
        OrderBuySide thisBuyOrder = new OrderBuySide(orderToCancel.idOfOrder, orderToCancel.idOfTicker, orderToCancel.idOfTrader, orderToCancel.dateOfOrder, orderToCancel.typeOfOrder, orderToCancel.priceOfOrder, orderToCancel.sizeOfOrder, orderToCancel.hiddenSize, orderToCancel.sizePerSent, orderToCancel.marketType);
        if (((OrderBook)MarketPlace.orderBookMap.get(Long.valueOf(orderToCancel.idOfTicker))).orderBuySidePriorityQueue.remove(thisBuyOrder))
        {
          orderToCancel.statusOfOrder = -3;
          orderToCancel.orderChild = newOrder.idOfOrder;
          orderToCancel.dateOfTransfer = newOrder.dateOfOrder;
          newOrder.statusOfOrder = 0;
          ((OrderBook)MarketPlace.orderBookMap.get(Long.valueOf(orderToCancel.idOfTicker))).cancellationList.add(newOrder);
        }
        break;
      case -1: 
        OrderSellSide thisSellOrder = new OrderSellSide(orderToCancel.idOfOrder, orderToCancel.idOfTicker, orderToCancel.idOfTrader, orderToCancel.dateOfOrder, orderToCancel.typeOfOrder, orderToCancel.priceOfOrder, orderToCancel.sizeOfOrder, orderToCancel.hiddenSize, orderToCancel.sizePerSent, orderToCancel.marketType);
        if (((OrderBook)MarketPlace.orderBookMap.get(Long.valueOf(orderToCancel.idOfTicker))).orderSellSidePriorityQueue.remove(thisSellOrder))
        {
          orderToCancel.statusOfOrder = -3;
          orderToCancel.orderChild = newOrder.idOfOrder;
          orderToCancel.dateOfTransfer = newOrder.dateOfOrder;
          newOrder.statusOfOrder = 0;
          ((OrderBook)MarketPlace.orderBookMap.get(Long.valueOf(orderToCancel.idOfTicker))).cancellationList.add(newOrder);
        }
        break;
      }
    }
    MarketPlace.orderFlowMap.put(Long.valueOf(newOrder.idOfOrder), newOrder);
    
    return newOrder;
  }
  
  public static OrderFlow ModifyOrder(long idOfOrder, int newDir, double newPrice, long newSize, String newTypeOfOrder)
  {
    OrderFlow finalOrder = null;
    OrderFlow orderToModify = (OrderFlow)MarketPlace.orderFlowMap.get(Long.valueOf(idOfOrder));
    
    OrderFlow newOrder = new OrderFlow(orderToModify.idOfTicker, newDir, newPrice, newSize, ((Integer)MarketPlace.orderType.get(newTypeOfOrder)).intValue(), orderToModify.idOfTrader);
    newOrder.sizeInitOfOrder = (newSize + orderToModify.executedQty);
    newOrder.executedQty = orderToModify.executedQty;
    newOrder.orderParent = orderToModify.idOfOrder;
    if (orderToModify.statusOfOrder > 0) {
      switch (orderToModify.dirOfOrder)
      {
      case 1: 
        OrderBuySide thisBuyOrder = new OrderBuySide(orderToModify.idOfOrder, orderToModify.idOfTicker, orderToModify.idOfTrader, orderToModify.dateOfOrder, orderToModify.typeOfOrder, orderToModify.priceOfOrder, orderToModify.sizeOfOrder, orderToModify.hiddenSize, orderToModify.sizePerSent, orderToModify.marketType);
        if (((OrderBook)MarketPlace.orderBookMap.get(Long.valueOf(orderToModify.idOfTicker))).orderBuySidePriorityQueue.remove(thisBuyOrder))
        {
          orderToModify.orderChild = newOrder.idOfOrder;
          orderToModify.dateOfTransfer = newOrder.dateOfOrder;
          orderToModify.statusOfOrder = -2;
          
          finalOrder = SendOrder(newOrder.idOfTicker, newDir, newPrice, newSize, newTypeOfOrder, newOrder.idOfTrader);
          finalOrder.orderParent = newOrder.idOfOrder;
          
          newOrder.statusOfOrder = 0;
          newOrder.orderChild = finalOrder.orderChild;
          newOrder.dateOfTransfer = finalOrder.dateOfOrder;
        }
        break;
      case -1: 
        OrderSellSide thisSellOrder = new OrderSellSide(orderToModify.idOfOrder, orderToModify.idOfTicker, orderToModify.idOfTrader, orderToModify.dateOfOrder, orderToModify.typeOfOrder, orderToModify.priceOfOrder, orderToModify.sizeOfOrder, orderToModify.hiddenSize, orderToModify.sizePerSent, orderToModify.marketType);
        if (((OrderBook)MarketPlace.orderBookMap.get(Long.valueOf(orderToModify.idOfTicker))).orderSellSidePriorityQueue.remove(thisSellOrder))
        {
          orderToModify.orderChild = newOrder.idOfOrder;
          orderToModify.dateOfTransfer = newOrder.dateOfOrder;
          orderToModify.statusOfOrder = -2;
          
          finalOrder = SendOrder(newOrder.idOfTicker, newDir, newPrice, newSize, newTypeOfOrder, newOrder.idOfTrader);
          finalOrder.orderParent = newOrder.idOfOrder;
          
          newOrder.statusOfOrder = 0;
          newOrder.orderChild = finalOrder.orderChild;
          newOrder.dateOfTransfer = finalOrder.dateOfOrder;
        }
        break;
      }
    }
    MarketPlace.orderFlowMap.put(Long.valueOf(newOrder.idOfOrder), newOrder);
    if (newOrder.statusOfOrder == 0) {
      return finalOrder;
    }
    return newOrder;
  }
}

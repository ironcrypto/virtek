package OrderBookMgt;

import MarketPlaceMgt.MarketPlace;
import UniqueId.UniqueLongId;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Execution
{
  long idOfTrade;
  public long idOfTicker;
  public long dateOfTrade;
  public double priceOfTrade;
  public long sizeOfTrade;
  long idOrder1;
  long idOrder2;
  long idOfTrader1;
  long idOfTrader2;
  
  public Execution(long idOfTrade, long idOfTicker, long dateOfTrade, double priceOfTrade, long sizeOfTrade, long idOrder1, long idOrder2, long idOfTrader1, long idOfTrader2)
  {
    this.idOfTrade = idOfTrade;
    this.idOfTicker = idOfTicker;
    this.dateOfTrade = dateOfTrade;
    this.priceOfTrade = priceOfTrade;
    this.sizeOfTrade = sizeOfTrade;
    this.idOrder1 = idOrder1;
    this.idOrder2 = idOrder2;
    this.idOfTrader1 = idOfTrader1;
    this.idOfTrader2 = idOfTrader2;
    
    ((OrderBook)MarketPlace.orderBookMap.get(Long.valueOf(idOfTicker))).lastPrice = priceOfTrade;
    ((OrderBook)MarketPlace.orderBookMap.get(Long.valueOf(idOfTicker))).lastSize = sizeOfTrade;
    ((OrderBook)MarketPlace.orderBookMap.get(Long.valueOf(idOfTicker))).lastDate = dateOfTrade;
  }
  
  public static OrderBook CheckExec(OrderBook ob, int orderType)
  {
	  Execution execution = null;
      switch(orderType)
      {
          case 1:
              //Check if any exec is possible and if the OrderBook is not empty
              while (!ob.emptyLimitOrderSide && ob.orderBuySidePriorityQueue.peek().priceOfOrder >= ob.orderSellSidePriorityQueue.peek().priceOfOrder)
              {
                  execution = RecordExec(ob, orderType);
                  ob = ContExecLimitOrder(execution);
              }
              break;
          case 2:
              while (!ob.emptyLimitOrderSide && (ob.orderBuySidePriorityQueue.peek().typeOfOrder == 2 || ob.orderSellSidePriorityQueue.peek().typeOfOrder == 2))
              {
                  execution = RecordExec(ob,orderType);
                  ob = ContExecMarketOrder(execution);
              }
              break;
          case 3:
              while (!ob.emptyLimitOrderSide && (ob.orderBuySidePriorityQueue.peek().typeOfOrder == 3 || ob.orderSellSidePriorityQueue.peek().typeOfOrder == 3))
              {
                  execution = RecordExec(ob,orderType);
                  ob = ContExecHitOrder(execution);
              }
              break;
      }
      //Check existing stop orders
      //todo: Check if we update the hiddenSize in the order flow map when we send the stop order...
      if (execution != null)
      {
          ob = CheckOffOrderExec(ob);
      }
      return ob;
	/*Execution execution = null;
    switch (orderType)
    {
    case 1: 
    case 2: 
    case 3: 
      while ((!ob.emptyLimitOrderSide) && (((OrderBuySide)ob.orderBuySidePriorityQueue.peek()).priceOfOrder >= ((OrderSellSide)ob.orderSellSidePriorityQueue.peek()).priceOfOrder))
      {
        execution = RecordExec(ob, orderType);
        ob = ContExecLimitOrder(execution); continue;
        while ((!ob.emptyLimitOrderSide) && ((((OrderBuySide)ob.orderBuySidePriorityQueue.peek()).typeOfOrder == 2) || (((OrderSellSide)ob.orderSellSidePriorityQueue.peek()).typeOfOrder == 2)))
        {
          execution = RecordExec(ob, orderType);
          ob = ContExecMarketOrder(execution); continue;
          while ((!ob.emptyLimitOrderSide) && ((((OrderBuySide)ob.orderBuySidePriorityQueue.peek()).typeOfOrder == 3) || (((OrderSellSide)ob.orderSellSidePriorityQueue.peek()).typeOfOrder == 3)))
          {
            execution = RecordExec(ob, orderType);
            ob = ContExecHitOrder(execution);
          }
        }
      }
    }
    if (execution != null) {
      ob = CheckOffOrderExec(ob);
    }
    return ob;
    */
  }
  
  public static Execution RecordExec(OrderBook orderBook, int orderType)
  {
	//Record of the exec
      double priceOfTrade;
      switch(orderType)
      {
          case 1:
              //Selection of price with Time Priority
              if (orderBook.orderBuySidePriorityQueue.peek().dateOfOrder <orderBook.orderSellSidePriorityQueue.peek().dateOfOrder)
              {
                  priceOfTrade = orderBook.orderBuySidePriorityQueue.peek().priceOfOrder;
              }
              else{priceOfTrade =  orderBook.orderSellSidePriorityQueue.peek().priceOfOrder;}
              break;
          default:
              if (orderBook.orderSellSidePriorityQueue.peek().typeOfOrder >1){priceOfTrade = orderBook.orderBuySidePriorityQueue.peek().priceOfOrder;}
              else{priceOfTrade = orderBook.orderSellSidePriorityQueue.peek().priceOfOrder;}
              break;
      }

      Execution newExec = new Execution(UniqueLongId.get(),orderBook.idOfOrderBook,new Date().getTime(),priceOfTrade,
              Math.min(orderBook.orderBuySidePriorityQueue.peek().sizeOfOrder,orderBook.orderSellSidePriorityQueue.peek().sizeOfOrder),
                      orderBook.orderBuySidePriorityQueue.peek().idOfOrder,orderBook.orderSellSidePriorityQueue.peek().idOfOrder,
                      orderBook.orderBuySidePriorityQueue.peek().idOfUser, orderBook.orderSellSidePriorityQueue.peek().idOfUser);
      orderBook.executionList.add(newExec);

      //Print of execution
      try {
          orderBook.buffer.write(newExec.dateOfTrade + "\t" + newExec.priceOfTrade + "\t" + newExec.sizeOfTrade + "\n");
      } catch (IOException e) {
          e.printStackTrace();
      }

      //Change in the OrderFlow collection, 1 for the buy list - 2 for the sell
      OrderFlow execO1 = MarketPlace.orderFlowMap.get(newExec.idOrder1);
          execO1.executedQty += newExec.sizeOfTrade;
          execO1.sizeOfOrder -= newExec.sizeOfTrade;
          if (execO1.sizeOfOrder==0 && execO1.hiddenSize==0){execO1.statusOfOrder = 0;}
          else if (execO1.sizeOfOrder==0 && execO1.hiddenSize != 0)
          {execO1.sizeOfOrder = Math.min(execO1.hiddenSize,execO1.sizePerSent);
          execO1.hiddenSize -= execO1.sizeOfOrder;}

      OrderFlow execO2 = MarketPlace.orderFlowMap.get(newExec.idOrder2);
          execO2.executedQty += newExec.sizeOfTrade;
          execO2.sizeOfOrder -= newExec.sizeOfTrade;
          if (execO2.sizeOfOrder==0 && execO2.hiddenSize ==0){execO2.statusOfOrder = 0;}
          else if (execO2.sizeOfOrder ==0 && execO2.hiddenSize !=0)
          {execO2.sizeOfOrder = Math.min(execO2.hiddenSize, execO2.sizePerSent);
          execO2.hiddenSize -= execO2.sizeOfOrder;}

      return newExec;
      
    /*double priceOfTrade;
    double priceOfTrade;
    switch (orderType)
    {
    case 1: 
      double priceOfTrade;
      if (((OrderBuySide)orderBook.orderBuySidePriorityQueue.peek()).dateOfOrder < ((OrderSellSide)orderBook.orderSellSidePriorityQueue.peek()).dateOfOrder) {
        priceOfTrade = ((OrderBuySide)orderBook.orderBuySidePriorityQueue.peek()).priceOfOrder;
      } else {
        priceOfTrade = ((OrderSellSide)orderBook.orderSellSidePriorityQueue.peek()).priceOfOrder;
      }
      break;
    default: 
      double priceOfTrade;
      if (((OrderSellSide)orderBook.orderSellSidePriorityQueue.peek()).typeOfOrder > 1) {
        priceOfTrade = ((OrderBuySide)orderBook.orderBuySidePriorityQueue.peek()).priceOfOrder;
      } else {
        priceOfTrade = ((OrderSellSide)orderBook.orderSellSidePriorityQueue.peek()).priceOfOrder;
      }
      break;
    }
    Execution newExec = new Execution(UniqueLongId.get(), orderBook.idOfOrderBook, new Date().getTime(), priceOfTrade, Math.min(((OrderBuySide)orderBook.orderBuySidePriorityQueue.peek()).sizeOfOrder, ((OrderSellSide)orderBook.orderSellSidePriorityQueue.peek()).sizeOfOrder), ((OrderBuySide)orderBook.orderBuySidePriorityQueue.peek()).idOfOrder, ((OrderSellSide)orderBook.orderSellSidePriorityQueue.peek()).idOfOrder, ((OrderBuySide)orderBook.orderBuySidePriorityQueue.peek()).idOfUser, ((OrderSellSide)orderBook.orderSellSidePriorityQueue.peek()).idOfUser);
    orderBook.executionList.add(newExec);
    try
    {
      orderBook.buffer.write(newExec.dateOfTrade + "\t" + newExec.priceOfTrade + "\t" + newExec.sizeOfTrade + "\n");
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    OrderFlow execO1 = (OrderFlow)MarketPlace.orderFlowMap.get(Long.valueOf(newExec.idOrder1));
    execO1.executedQty += newExec.sizeOfTrade;
    execO1.sizeOfOrder -= newExec.sizeOfTrade;
    if ((execO1.sizeOfOrder == 0L) && (execO1.hiddenSize == 0L))
    {
      execO1.statusOfOrder = 0;
    }
    else if ((execO1.sizeOfOrder == 0L) && (execO1.hiddenSize != 0L))
    {
      execO1.sizeOfOrder = Math.min(execO1.hiddenSize, execO1.sizePerSent);
      execO1.hiddenSize -= execO1.sizeOfOrder;
    }
    OrderFlow execO2 = (OrderFlow)MarketPlace.orderFlowMap.get(Long.valueOf(newExec.idOrder2));
    execO2.executedQty += newExec.sizeOfTrade;
    execO2.sizeOfOrder -= newExec.sizeOfTrade;
    if ((execO2.sizeOfOrder == 0L) && (execO2.hiddenSize == 0L))
    {
      execO2.statusOfOrder = 0;
    }
    else if ((execO2.sizeOfOrder == 0L) && (execO2.hiddenSize != 0L))
    {
      execO2.sizeOfOrder = Math.min(execO2.hiddenSize, execO2.sizePerSent);
      execO2.hiddenSize -= execO2.sizeOfOrder;
    }
    return newExec;
    */
  }
  
  public static OrderBook ContExecLimitOrder(Execution exec)
  {
    OrderBook orderBook = (OrderBook)MarketPlace.orderBookMap.get(Long.valueOf(exec.idOfTicker));
    if (((OrderBuySide)orderBook.orderBuySidePriorityQueue.peek()).sizeOfOrder == exec.sizeOfTrade)
    {
      OrderBuySide obs = (OrderBuySide)orderBook.orderBuySidePriorityQueue.poll();
      if (obs.hiddenSize != 0L)
      {
        obs.sizeOfOrder = Math.min(obs.hiddenSize, obs.sizePerSent);
        obs.hiddenSize -= obs.sizeOfOrder;
        obs.dateOfOrder = exec.dateOfTrade;
        orderBook.orderBuySidePriorityQueue.add(obs);
      }
      else if (orderBook.orderBuySidePriorityQueue.isEmpty())
      {
        orderBook.emptyLimitOrderSide = true;
      }
    }
    else
    {
      ((OrderBuySide)orderBook.orderBuySidePriorityQueue.peek()).sizeOfOrder -= exec.sizeOfTrade;
    }
    if (((OrderSellSide)orderBook.orderSellSidePriorityQueue.peek()).sizeOfOrder == exec.sizeOfTrade)
    {
      OrderSellSide oss = (OrderSellSide)orderBook.orderSellSidePriorityQueue.poll();
      if (oss.hiddenSize != 0L)
      {
        oss.sizeOfOrder = Math.min(oss.hiddenSize, oss.sizePerSent);
        oss.hiddenSize -= oss.sizeOfOrder;
        oss.dateOfOrder = exec.dateOfTrade;
        orderBook.orderSellSidePriorityQueue.add(oss);
      }
      else if (orderBook.orderSellSidePriorityQueue.isEmpty())
      {
        orderBook.emptyLimitOrderSide = true;
      }
    }
    else
    {
      ((OrderSellSide)orderBook.orderSellSidePriorityQueue.peek()).sizeOfOrder -= exec.sizeOfTrade;
    }
    return orderBook;
  }
  
  public static OrderBook ContExecMarketOrder(Execution exec)
  {
    OrderBook orderBook = (OrderBook)MarketPlace.orderBookMap.get(Long.valueOf(exec.idOfTicker));
    if (((OrderBuySide)orderBook.orderBuySidePriorityQueue.peek()).sizeOfOrder == exec.sizeOfTrade)
    {
      OrderBuySide obs = (OrderBuySide)orderBook.orderBuySidePriorityQueue.poll();
      if (obs.hiddenSize != 0L)
      {
        obs.sizeOfOrder = Math.min(obs.hiddenSize, obs.sizePerSent);
        obs.hiddenSize -= obs.sizeOfOrder;
        obs.dateOfOrder = exec.dateOfTrade;
        orderBook.orderBuySidePriorityQueue.add(obs);
      }
      else if (orderBook.orderBuySidePriorityQueue.isEmpty())
      {
        orderBook.emptyLimitOrderSide = true;
        if (((OrderSellSide)orderBook.orderSellSidePriorityQueue.peek()).typeOfOrder == 2)
        {
          ((OrderFlow)MarketPlace.orderFlowMap.get(Long.valueOf(((OrderSellSide)orderBook.orderSellSidePriorityQueue.peek()).idOfOrder))).statusOfOrder = -3;
          orderBook.orderSellSidePriorityQueue.poll();
        }
      }
    }
    else
    {
      ((OrderBuySide)orderBook.orderBuySidePriorityQueue.peek()).sizeOfOrder -= exec.sizeOfTrade;
    }
    if (((OrderSellSide)orderBook.orderSellSidePriorityQueue.peek()).sizeOfOrder == exec.sizeOfTrade)
    {
      OrderSellSide oss = (OrderSellSide)orderBook.orderSellSidePriorityQueue.poll();
      if (oss.hiddenSize != 0L)
      {
        oss.sizeOfOrder = Math.min(oss.hiddenSize, oss.sizePerSent);
        oss.hiddenSize -= oss.sizeOfOrder;
        oss.dateOfOrder = exec.dateOfTrade;
        orderBook.orderSellSidePriorityQueue.add(oss);
      }
      else if (orderBook.orderSellSidePriorityQueue.isEmpty())
      {
        orderBook.emptyLimitOrderSide = true;
        if (((OrderBuySide)orderBook.orderBuySidePriorityQueue.peek()).typeOfOrder == 2)
        {
          ((OrderFlow)MarketPlace.orderFlowMap.get(Long.valueOf(((OrderBuySide)orderBook.orderBuySidePriorityQueue.peek()).idOfOrder))).statusOfOrder = -3;
          orderBook.orderBuySidePriorityQueue.poll();
        }
      }
    }
    else
    {
      ((OrderSellSide)orderBook.orderSellSidePriorityQueue.peek()).sizeOfOrder -= exec.sizeOfTrade;
    }
    return orderBook;
  }
  
  public static OrderBook ContExecHitOrder(Execution exec)
  {
    OrderBook orderBook = (OrderBook)MarketPlace.orderBookMap.get(Long.valueOf(exec.idOfTicker));
    
    boolean checkAskSamePrice = false;
    if (((OrderBuySide)orderBook.orderBuySidePriorityQueue.peek()).sizeOfOrder == exec.sizeOfTrade)
    {
      OrderBuySide obs = (OrderBuySide)orderBook.orderBuySidePriorityQueue.poll();
      if (obs.hiddenSize != 0L)
      {
        obs.sizeOfOrder = Math.min(obs.hiddenSize, obs.sizePerSent);
        obs.hiddenSize -= obs.sizeOfOrder;
        obs.dateOfOrder = exec.dateOfTrade;
        orderBook.orderBuySidePriorityQueue.add(obs);
      }
      else if (orderBook.orderBuySidePriorityQueue.isEmpty())
      {
        orderBook.emptyLimitOrderSide = true;
      }
    }
    else
    {
      ((OrderBuySide)orderBook.orderBuySidePriorityQueue.peek()).sizeOfOrder -= exec.sizeOfTrade;
      if (((OrderBuySide)orderBook.orderBuySidePriorityQueue.peek()).typeOfOrder == 3) {
        checkAskSamePrice = true;
      }
    }
    if (((OrderSellSide)orderBook.orderSellSidePriorityQueue.peek()).sizeOfOrder == exec.sizeOfTrade)
    {
      OrderSellSide oss = (OrderSellSide)orderBook.orderSellSidePriorityQueue.poll();
      if (oss.hiddenSize != 0L)
      {
        oss.sizeOfOrder = Math.min(oss.hiddenSize, oss.sizePerSent);
        oss.hiddenSize -= oss.sizeOfOrder;
        oss.dateOfOrder = exec.dateOfTrade;
        orderBook.orderSellSidePriorityQueue.add(oss);
      }
      else if (orderBook.orderSellSidePriorityQueue.isEmpty())
      {
        orderBook.emptyLimitOrderSide = true;
      }
    }
    else
    {
      ((OrderSellSide)orderBook.orderSellSidePriorityQueue.peek()).sizeOfOrder -= exec.sizeOfTrade;
      if ((((OrderSellSide)orderBook.orderSellSidePriorityQueue.peek()).typeOfOrder == 3) && ((orderBook.orderBuySidePriorityQueue.isEmpty()) || (((OrderBuySide)orderBook.orderBuySidePriorityQueue.peek()).priceOfOrder != exec.priceOfTrade)))
      {
        ((OrderSellSide)orderBook.orderSellSidePriorityQueue.peek()).typeOfOrder = 1;
        ((OrderSellSide)orderBook.orderSellSidePriorityQueue.peek()).priceOfOrder = exec.priceOfTrade;
      }
    }
    if ((checkAskSamePrice) && ((orderBook.orderSellSidePriorityQueue.isEmpty()) || (((OrderSellSide)orderBook.orderSellSidePriorityQueue.peek()).priceOfOrder != exec.priceOfTrade)))
    {
      ((OrderBuySide)orderBook.orderBuySidePriorityQueue.peek()).typeOfOrder = 1;
      ((OrderBuySide)orderBook.orderBuySidePriorityQueue.peek()).priceOfOrder = exec.priceOfTrade;
    }
    return orderBook;
  }
  
  public static OrderBook CheckOffOrderExec(OrderBook ob)
  {
    if ((!ob.offOrderBuySidePriorityQueue.isEmpty()) && (((OffOrderBuySide)ob.offOrderBuySidePriorityQueue.peek()).priceLimit >= ob.lastPrice))
    {
      OffOrderBuySide sobs = (OffOrderBuySide)ob.offOrderBuySidePriorityQueue.poll();
      OrderBuySide obs = new OrderBuySide(sobs.idOfOrder, sobs.idOfTicker, sobs.idOfUser, new Date().getTime(), sobs.typeOfOrder, sobs.priceLimit, sobs.sizeOfOrder, 0L, 0L, sobs.marketType);
      
      ((OrderFlow)MarketPlace.orderFlowMap.get(Long.valueOf(sobs.idOfOrder))).sizeOfOrder = obs.sizeOfOrder;
      ((OrderFlow)MarketPlace.orderFlowMap.get(Long.valueOf(sobs.idOfOrder))).hiddenSize -= obs.sizeOfOrder;
      OrderBuySide.GetOrderBook(obs);
    }
    else if ((!ob.offOrderSellSidePriorityQueue.isEmpty()) && (((OffOrderSellSide)ob.offOrderSellSidePriorityQueue.peek()).priceLimit <= ob.lastPrice))
    {
      OffOrderSellSide soss = (OffOrderSellSide)ob.offOrderSellSidePriorityQueue.poll();
      OrderSellSide oss = new OrderSellSide(soss.idOfOrder, soss.idOfTicker, soss.idOfUser, new Date().getTime(), soss.typeOfOrder, soss.priceLimit, soss.sizeOfOrder, 0L, 0L, soss.marketType);
      
      ((OrderFlow)MarketPlace.orderFlowMap.get(Long.valueOf(soss.idOfOrder))).sizeOfOrder = oss.sizeOfOrder;
      ((OrderFlow)MarketPlace.orderFlowMap.get(Long.valueOf(soss.idOfOrder))).hiddenSize -= oss.sizeOfOrder;
      OrderSellSide.GetOrderBook(oss);
    }
    return ob;
  }
}

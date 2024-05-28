package OrderBookMgt;

import MarketPlaceMgt.MarketPlace;
import MarketPlaceMgt.Securities;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class OrderBook
{
	long idOfOrderBook;
	String nameOfBook;
    int marketType;
    boolean emptyLimitOrderSide;

    double lastPrice;
    long lastSize;
    long lastDate;


    PriorityQueueComparatorBuySide priorityQueueComparatorBuySide = new PriorityQueueComparatorBuySide();
    PriorityQueue<OrderBuySide> orderBuySidePriorityQueue = new PriorityQueue<OrderBuySide>(1,priorityQueueComparatorBuySide);
    PriorityQueueComparatorSellSide priorityQueueComparatorSellSide = new PriorityQueueComparatorSellSide();
    PriorityQueue<OrderSellSide> orderSellSidePriorityQueue = new PriorityQueue<OrderSellSide>(1,priorityQueueComparatorSellSide);

    PriorityQueueComparatorOffOrderBuySide priorityQueueComparatorOffOrderBuySide = new PriorityQueueComparatorOffOrderBuySide();
    PriorityQueue<OffOrderBuySide> offOrderBuySidePriorityQueue = new PriorityQueue<OffOrderBuySide>(1,priorityQueueComparatorOffOrderBuySide);
    PriorityQueueComparatorOffOrderSellSide priorityQueueComparatorOffOrderSellSide = new PriorityQueueComparatorOffOrderSellSide();
    PriorityQueue<OffOrderSellSide> offOrderSellSidePriorityQueue = new PriorityQueue<OffOrderSellSide>(1,priorityQueueComparatorOffOrderSellSide);

    //todo: 2- Take off all the unnecessary List for an execution such as executionList, cancellationList
    public LinkedList<Execution> executionList = new LinkedList<Execution>();
    LinkedList<OrderFlow> cancellationList = new LinkedList<OrderFlow>();

    FileWriter executionFile;
    BufferedWriter buffer;
  
/*
	long idOfOrderBook;
  String nameOfBook;
  int marketType;
  boolean emptyLimitOrderSide;
  double lastPrice;
  long lastSize;
  long lastDate;
  PriorityQueueComparatorBuySide priorityQueueComparatorBuySide = new PriorityQueueComparatorBuySide();
  PriorityQueue<OrderBuySide> orderBuySidePriorityQueue = new PriorityQueue(this.priorityQueueComparatorBuySide);
  PriorityQueueComparatorSellSide priorityQueueComparatorSellSide = new PriorityQueueComparatorSellSide();
  PriorityQueue<OrderSellSide> orderSellSidePriorityQueue = new PriorityQueue(this.priorityQueueComparatorSellSide);
  PriorityQueueComparatorOffOrderBuySide priorityQueueComparatorOffOrderBuySide = new PriorityQueueComparatorOffOrderBuySide();
  PriorityQueue<OffOrderBuySide> offOrderBuySidePriorityQueue = new PriorityQueue(this.priorityQueueComparatorOffOrderBuySide);
  PriorityQueueComparatorOffOrderSellSide priorityQueueComparatorOffOrderSellSide = new PriorityQueueComparatorOffOrderSellSide();
  PriorityQueue<OffOrderSellSide> offOrderSellSidePriorityQueue = new PriorityQueue(this.priorityQueueComparatorOffOrderSellSide);
 
  public LinkedList<Execution> executionList = new LinkedList();
  LinkedList<OrderFlow> cancellationList = new LinkedList();
  FileWriter executionFile;
  BufferedWriter buffer;
  */
  public OrderBook(long idOfOrderBook, String nameOfBook, int marketType)
  {
    this.idOfOrderBook = idOfOrderBook;
    this.nameOfBook = nameOfBook;
    this.marketType = marketType;
    this.emptyLimitOrderSide = true;
    try
    {
      this.executionFile = new FileWriter(idOfOrderBook + " Execution");
      this.buffer = new BufferedWriter(this.executionFile);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  public static void InstanceOrderBook(Securities sec)
  {
    OrderBook ob = new OrderBook(sec.idOfTicker, sec.nameOfTicker, sec.marketType);
    MarketPlace.orderBookMap.put(Long.valueOf(ob.idOfOrderBook), ob);
  }
}

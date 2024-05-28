package OrderBookMgt;

import MarketPlaceMgt.MarketPlace;
import MarketPlaceMgt.OrderType;
import MarketPlaceMgt.Securities;
import MarketPlaceMgt.Traders;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

public class TestEnvironment
{
  public static void main(String[] args)
    throws Exception
  {
    Securities.InstanceSecurities();
    Traders.InstanceTraders();
    OrderType.InstanceOrderType();
    
    SendOrder();
  }
  
  public static void SendOrder()
  {
    long startTime = System.nanoTime();
    Random r = new Random();
    double lowPrice = 99.0D;
    double highPrice = 101.0D;
    
    int lowQty = 5;
    int highQty = 30;
    
    int orderCount = 0;
    
    LinkedList<Long> orderIdList = new LinkedList();
    for (int i = 0; i < 1001; i++)
    {
      OrderFlow order;
      if (r.nextBoolean()) {
        order = OrderFlow.SendOrder(1001L, 1, r.nextDouble() * (highPrice - 0.05D - lowPrice) + lowPrice, r.nextInt(highQty - lowQty) + lowQty, "Limit Order", 789654L);
      } else {
        order = OrderFlow.SendOrder(1001L, -1, r.nextDouble() * (highPrice - lowPrice + 0.05D) + lowPrice + 0.05D, r.nextInt(highQty - lowQty) + lowQty, "Limit Order", 789654L);
      }
      orderIdList.add(Long.valueOf(order.idOfOrder));
      orderCount++;
    }
    
    long endTime = System.nanoTime();
    long duration = endTime - startTime;
    System.out.println(duration);
    System.out.println(orderCount);
    
    long tickerToClose = 1001L;
    try
    {
      ((OrderBook)MarketPlace.orderBookMap.get(Long.valueOf(tickerToClose))).buffer.close();
      ((OrderBook)MarketPlace.orderBookMap.get(Long.valueOf(tickerToClose + 1L))).buffer.close();
      ((OrderBook)MarketPlace.orderBookMap.get(Long.valueOf(tickerToClose + 1L))).buffer.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}

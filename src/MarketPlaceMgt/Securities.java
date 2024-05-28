package MarketPlaceMgt;

import OrderBookMgt.OrderBook;
import java.util.HashMap;

public class Securities
{
  public long idOfTicker;
  public String nameOfTicker;
  public int marketType;
  public double tickSize;
  
  public Securities(long idOfTicker, String nameOfTicker, int marketType)
  {
    this.idOfTicker = idOfTicker;
    this.nameOfTicker = nameOfTicker;
    this.marketType = marketType;
    this.tickSize = 0.005D;
  }
  
  public static void InstanceSecurities()
  {
    Securities sec1 = new Securities(1001L, "Asset 1", 1);
    OrderBook.InstanceOrderBook(sec1);
    
    Securities sec2 = new Securities(1002L, "Asset 2", 1);
    OrderBook.InstanceOrderBook(sec2);
    
    Securities sec3 = new Securities(1003L, "Asset 3", 1);
    OrderBook.InstanceOrderBook(sec3);
    
    MarketPlace.securitiesMap.put(Long.valueOf(sec1.idOfTicker), sec1);
    MarketPlace.securitiesMap.put(Long.valueOf(sec2.idOfTicker), sec2);
    MarketPlace.securitiesMap.put(Long.valueOf(sec3.idOfTicker), sec3);
  }
  
  public static double GetTickPrice(double price)
  {
    return Math.round(price * 200.0D) / 200.0D;
  }
}

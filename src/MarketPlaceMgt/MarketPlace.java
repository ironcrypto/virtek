package MarketPlaceMgt;

import OrderBookMgt.OrderBook;
import OrderBookMgt.OrderFlow;
import java.util.HashMap;

public class MarketPlace
{
  public static HashMap<Long, OrderBook> orderBookMap = new HashMap();
  public static HashMap<Long, Securities> securitiesMap = new HashMap();
  public static HashMap<Long, OrderFlow> orderFlowMap = new HashMap();
  public static HashMap<Long, Traders> tradersMap = new HashMap();
  public static HashMap<String, Integer> orderType = new HashMap();
}

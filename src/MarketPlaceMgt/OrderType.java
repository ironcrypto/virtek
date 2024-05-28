package MarketPlaceMgt;

import java.util.HashMap;

public class OrderType
{
  public static void InstanceOrderType()
  {
    MarketPlace.orderType.put("Limit Order", Integer.valueOf(1));
    MarketPlace.orderType.put("Market Order", Integer.valueOf(2));
    MarketPlace.orderType.put("Hit Order", Integer.valueOf(3));
    MarketPlace.orderType.put("Iceberg Order", Integer.valueOf(4));
    MarketPlace.orderType.put("Stop Order", Integer.valueOf(5));
    MarketPlace.orderType.put("Modify Order", Integer.valueOf(-1));
    MarketPlace.orderType.put("Cancel Order", Integer.valueOf(0));
  }
}

package MarketPlaceMgt;

import java.util.HashMap;

public class Traders
{
  public long idOfTrader;
  public String nameOfTrader;
  public double cashPosition;
  public double investedPosition;
  public boolean authorizationToTrade;
  public double maxPerTrade;
  public double maxVolume;
  
  public Traders(long idOfTrader, String nameOfTrader, double cashPosition, double investedPosition, boolean authorizationToTrade, double maxPerTrade, double maxVolume)
  {
    this.idOfTrader = idOfTrader;
    this.nameOfTrader = nameOfTrader;
    this.cashPosition = cashPosition;
    this.investedPosition = investedPosition;
    this.authorizationToTrade = authorizationToTrade;
    this.maxPerTrade = maxPerTrade;
    this.maxVolume = maxVolume;
  }
  
  public static void InstanceTraders()
  {
    Traders hft = new Traders(789654L, "HFT", 9.99999999E8D, 0.0D, true, 9.99999999E8D, 9.99999999E8D);
    Traders mft = new Traders(789321L, "MFT", 9.99999999E8D, 0.0D, true, 9.99999999E8D, 9.99999999E8D);
    Traders sft = new Traders(789987L, "SFT", 9.99999999E8D, 0.0D, true, 9.99999999E8D, 9.99999999E8D);
    
    MarketPlace.tradersMap.put(Long.valueOf(hft.idOfTrader), hft);
    MarketPlace.tradersMap.put(Long.valueOf(mft.idOfTrader), mft);
    MarketPlace.tradersMap.put(Long.valueOf(sft.idOfTrader), sft);
  }
}

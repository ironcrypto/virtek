package UniqueId;

public class UniqueLongId
{
  static long current = System.currentTimeMillis();
  
  public static synchronized long get()
  {
    return current++;
  }
}

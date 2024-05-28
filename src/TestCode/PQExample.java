package TestCode;

import java.io.PrintStream;
import java.util.PriorityQueue;

public class PQExample
{
  public int id;
  public int price;
  public String name;
  public long date;
  
  public PQExample(int id, int price, String name, long time)
  {
    this.id = id;
    this.price = price;
    this.name = name;
    this.date = time;
  }
  
  public static void main(String[] args)
  {
    PriorityQueueComparator pqc = new PriorityQueueComparator();
    PriorityQueue<PQExample> PQ = new PriorityQueue(1,pqc);
    int setID = 1000;
    int setDate = 0;
    PQ.add(new PQExample(setID++, 24, "Mat", setDate++));
    PQ.add(new PQExample(setID++, 25, "Tommy", setDate++));
    PQ.add(new PQExample(setID++, 22, "Kate", setDate++));
    PQ.add(new PQExample(setID++, 26, "Mary", setDate++));
    PQ.add(new PQExample(setID++, 24, "Ronny", setDate++));
    
    PQExample valueToDel = new PQExample(1000, 22, "Mat", 0L);
    PQ.remove(valueToDel);
    for (int i = 0; i < 5; i++)
    {
      System.out.println("Queue in: " + i + " is " + ((PQExample)PQ.peek()).name + " with the price of " + ((PQExample)PQ.peek()).price);
      PQ.poll();
    }
  }
  
  public boolean equals(Object o)
  {
    if ((o instanceof PQExample))
    {
      PQExample pqExample = (PQExample)o;
      return this.id == pqExample.id;
    }
    return false;
  }
}

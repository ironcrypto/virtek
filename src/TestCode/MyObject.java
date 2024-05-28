package TestCode;

import java.util.Date;

class MyObject
{
  private long id;
  private double price;
  private String name;
  private long date;
  
  public MyObject(long id, double price, String name)
  {
    this.id = id;
    this.price = price;
    this.name = name;
    this.date = new Date().getTime();
  }
  
  public String toString()
  {
    return "MyObject [id=" + this.id + ", price=" + this.price + ", name=" + this.name + "]";
  }
  
  public double getPrice()
  {
    return this.price;
  }
  
  public long getDate()
  {
    return this.date;
  }
}

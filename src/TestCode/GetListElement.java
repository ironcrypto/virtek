package TestCode;

import java.util.Date;
import java.util.LinkedList;

public class GetListElement
{
  long id;
  double price;
  String name;
  long date;
  
  public static void GetMyObject(long id, double price, String name)
  {
    GetListElement newObject = new GetListElement();
    newObject.id = id;
    newObject.price = price;
    newObject.name = name;
    newObject.date = new Date().getTime();
    PlaceOfMyList.myList.add(newObject);
  }
}

package TestCode;

class MainClass
{
  public static void main(String[] args)
    throws Exception
  {
    int setID = 1000;
    GetListElement.GetMyObject(setID++, 26.0D, "Mat");
    GetListElement.GetMyObject(setID++, 25.0D, "Tommy");
    GetListElement.GetMyObject(setID++, 24.0D, "Kate");
    GetListElement.GetMyObject(setID++, 26.0D, "Mary");
    GetListElement.GetMyObject(setID++, 24.0D, "Ronny");
  }
}

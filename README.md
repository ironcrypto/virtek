Description	v1.0
S. THOMAS

Virtek is developed in JAVA, using a solid architecture that can be optimised for prototyping in the near future. It is organised around five packages. 

| Package |	Objective |
|:-------|:------------------------------------------------------------------------|
| BatchAuction	| Test universe to simulate a market at the fix. This package will then be integrated into the OrderBookMgt package|
| DataMgt	| VirTeK data-to-data transformer |
| MarketPlaceMgt	| VirTeK's market settings, players, securities and orders |
| OrderBookMgt	| Order book management universe |
| TestCode	| Trading Test Universe |
| Tools	| All code tools that cannot be categorised |


To date, VirTek is a matching engine, allowing to read a flow of orders (different types of orders taken into account) and to integrate them into an order book according to certain priority rules, to then calculate the execution prices and the Best Bid Offer (BBO). Virtek manages two order books: visible and hidden, according to a dichotomy of Buy Order / Put Order. 

# Package OrderBookMgt

A user (virtual trader or researcher) only has access to a few methods, and can only communicate with one package: ```OrderBookMgt```. In addition, they can only access two classes: ```DisplayInformation``` and ```OrderFlow```. 

| Class | Objective |
|:-------|:------------------------------------------------------------------------|
| DisplayInformation| All available market information |
| Execution | The market's execution platform |
| OffOrderBuySide | The hidden order book quoted Bid |
| OffOrderSellSide | The hidden Ask order book |
| OrderBook | The class generating the 4 parts of the order book :Bid-Ask-cached-visible |
| OrderBuySide | The visible order book on the Bid side |
| OrderFlow | The only communication element for transferring electronic messages |
| OrderSellSide	| The visible Ask order book |
| TestEnvironment	| Test trading platform |

DisplayInformation is a class that does not allow direct interaction with the market. It discloses the market information available to the trader via three methods: ```GetBBO```, ```GetLastInformation```, ```GetVisibleOB```. 

| Method | Objective |
|:-------|:------------------------------------------------------------------------|
| GetBBO (long idOfOrderBook)| Obtain the best Bid and Ask for a given title | GetLastInformation (long idOfOrderBook)
| GetLastInformation (long idOfOrderBook) | Get price, date, size, volume, highest, lowest |
| GetVisibleOB (long idOfOrderBook, int nbOfTick) | Gets the order book for a specified security and the number of ticks required.

 
# Package OrderFlow
OrderFlow is the central class of Virtek's operation, it is the place where the order flow is transferred. The trader can only use three methods, the tool then organizes the correct routing of the order. It is the only place where he will be able to communicate with the financial market. 

Methods to be used by the trader are : ```SendOrder```, ```CancelOrder```, ```ModifyOrder```. 

| Method | Objective |
|:-------|:------------------------------------------------------------------------|
| SendOrder (long idOfTicker, int dirOfOrder, double priceOfOrder, long sizeOfOrder, String typeOfOrder, long idOfTrader) |	Envoi d'un ordre vers une des méthodes suivantes : Classic, Iceberg, Stop, FoK |
| ClassicOrder (OrderFlow newOrder) |	Sending a Limit, Hit or Market order |
| IcebergOrder (long idOfTicker, int dirOfOrder, double priceOfOrder, long sizeOfOrder, long sizePerSent, String typeOfOrder, long idOfTrader) | Sending an Iceberg order |
| StopOrder (long idOfTicker, int dirOfOrder, double priceOfOrder, long hiddenSize, String typeOfOrder, long idOfTrader) |	Sending a stop order |
| FoKOrder (long idOfTicker, int dirOfOrder, double priceOfOrder, long sizeOfOrder, String typeOfOrder, long idOfTrader) |	Sending a FoK order |
| CancelOrder (long idOfOrder, long idOfTrader) |	Sending a Cancel order |
| ModifyOrder (long idOfOrder, int newDir, double newPrice, long newSize, String newTypeOfOrder, long idOfTrader)	| Sending a Modify order |



Once the order is sent, the trader receives the ```OrderFlow``` object, which returns all the characteristics of his order: 
- long idOfTicker 
- int dirOfOrder
- double priceOfOrder
- long sizeOfOrder
- int typeOfOrder
- long idOfTrader 
- long idOfOrder 

It also receives all the private features such as:  
- long sizeInitOfOrder
- boolean validOrder    
- int marketType    
- long idOfOrder    
- int statusOfOrder   
- long idOfTrader    
- long dateOfOrder    
- long executedQty = 0    
- long orderParent    
- long orderChild    
- long dateOfTransfer

Note: On target, it will be necessary to select what information to disclose in order not to allow an overlay of information for the user. 

_Cancellation/modification/creation rules:_
- If the order is cancelled then it is excluded from the order book.
- If the order is changed then it is canceled and recreated as a new order. 
- When a new order is sent, it is redirected to the correct order book: visible or hidden, on the Bid side or on the Ask side. 

_Order Book Priority Rules_
- The interpretation layer is always the same, it follows two logics: A first ordering according to prices, a second according to time with a FIFO (First In First Out) logic.

# Package Execution
Once it has entered the right side of the right book, VirTek tests the possibility of execution.

Check Exec for Hidden Order Book

| Method | Objective |
|:-------|:------------------------------------------------------------------------|
| CheckExec (OrderBook ob, int orderType) | Check for a possible match: and send to an execution depending on the order type | 
| RecordExec (OrderBook orderBook, int orderType) | Record execution 
| ContExecLimitOrder (Execution exec) | Execution for Limit | 
| ContExecMarketOrder (Execution exec) | Execution for Market | 
| ContExecHitOrder (Execution exec) | Execution for Hit | 
| CheckOffOrderExec (OrderBook ob) | Check Exec for hidden order book |


# Execution Logic

The logic of execution is simple:

```
1. CheckExec checks whether an execution is possible
   
-> 1.a If so, it proceeds to record the execution and then transforms the order book after execution with the ContExecMarketOrder, ContExecHitOrder and ContExecLimitOrder (Cont for Continuous) methods

--> 1.a.i After execution, the BBO price has been able to change, CheckExec therefore checks whether the hidden order book can be executed: (CheckOffOrderExec method)

---> 1.a.i.1 In the event that there is a possible execution, the hidden order is removed from the dark order book to be integrated into the visible order book and take over the same rights as a visible order => resume step 1.a

----> 1.a.i.1.a The execution is recorded by the RecordExec method

---> 1.a.ii In the event that an execution is not possible, nothing happens and the hidden order book is not changed.

-> 1.b If not, nothing happens and the order book is not changed.
```
  
**&rarr;** Step 1.a is repeated until 1.b is reached.

**Note**: When an order status is changed or after any partial or complete execution, the order status of the ```OrderFlow``` is also changed. You need to create a notification for users through a ```dataFeedListener```. 


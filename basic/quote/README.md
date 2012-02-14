Quote Sample
============

This example demonstrates the following aspects of the CORE EIP support available with Spring Integration:

1. Channel Adapter (Inbound and Stdout)
2. Poller with Interval Trigers
3. Service Activator

It is a very simple example that introduces you to the Channel adapters and Pollers.

Messages are simply being emitted by the Poller (interval based) triggering **nextTicker()** method of *TickerStream* class and sent to a **tickers** channel from which they are retrieved by the *TickerStream* service.

*TickerStream* service generates random ticker symbols sending them to the **quotes** channel from which they are retrieved by the *QuoteService* (annotation based Service Activator). *QuoteService* generates random quotes sending them to the *Stdout Channel Adapter* where they are printed to a console.

To execute sample simply run **QuoteDemoTest**. You should see the output similar to this:

	XNY: 90.03
	XMR: 17.11
	IWR: 35.85
	KHR: 54.43
	WUW: 95.29
	YYC: 7.44
	DYW: 84.76
	TIW: 28.31
	HGE: 28.90



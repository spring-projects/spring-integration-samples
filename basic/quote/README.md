Quote Sample
============

This example demonstrates the following aspects of the CORE EIP support available with Spring Integration:

1. Channel Adapter (Inbound and Stdout)
2. Poller with Interval Triggers
3. Service Activator

It is a very simple example that introduces you to Channel adapters and Pollers.

Messages are simply being emitted by the *Poller* (interval based) triggering the **nextTicker()** method of the *TickerStream* class and are then sent to a **tickers** channel, from which they are retrieved by the *TickerStream* service.

The *TickerStream* service generates random ticker symbols, sending them to the **quotes** channel, from which they are retrieved by the *QuoteService* (annotation based Service Activator). The *QuoteService* generates random quotes, sending them to the *Stdout Channel Adapter*, where they are printed to the console.

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



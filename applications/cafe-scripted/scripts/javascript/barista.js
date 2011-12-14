importClass(org.springframework.integration.samples.cafe.Drink);
importClass(java.lang.Thread);
importClass(java.lang.System);
 
function prepareDrink(orderItem){ 
	Thread.sleep(timeToPrepare);
	System.out.println("javascript: preparing "+ orderItem + " for order "+ orderItem.getOrder().getNumber())
	return new Drink(orderItem.getOrder().getNumber(), orderItem.getDrinkType(), orderItem.isIced(),
            orderItem.getShots())
}

prepareDrink(payload);


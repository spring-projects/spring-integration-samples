import time
import java

Drink = java.type("org.springframework.integration.samples.cafe.Drink")

def prepareDrink(orderItem):
	print("python: preparing %s for order %s" % (orderItem,orderItem.getOrder().getNumber()))
	time.sleep(eval(timeToPrepare))
	return Drink(orderItem.getOrder().getNumber(), orderItem.getDrinkType(), orderItem.isIced(),
              orderItem.getShots())
              
drink = prepareDrink(payload)          
              
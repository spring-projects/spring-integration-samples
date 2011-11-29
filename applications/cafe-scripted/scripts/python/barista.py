from org.springframework.integration.samples.cafe import Drink
import time

def prepareDrink(orderItem):
	print("python: preparing %s for order %s" % (orderItem,orderItem.order.number))
	time.sleep(eval(timeToPrepare))
	return Drink(orderItem.getOrder().getNumber(), orderItem.getDrinkType(), orderItem.isIced(),
              orderItem.getShots())
              
drink = prepareDrink(payload)          
              
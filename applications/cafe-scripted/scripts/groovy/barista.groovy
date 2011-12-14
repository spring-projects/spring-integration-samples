package groovy
import org.springframework.integration.samples.cafe.Drink
  
def prepareDrink(orderItem){
	println "groovy: preparing $orderItem for order ${orderItem.order.number}"
	try {
		Thread.sleep(timeToPrepare as long)
	} catch (e) {
		println "sleep interrupted"
	}
	new Drink(orderItem.getOrder().getNumber(), orderItem.getDrinkType(), orderItem.isIced(),
			orderItem.getShots())
}

prepareDrink(payload)



 
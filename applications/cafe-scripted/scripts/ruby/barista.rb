  require 'java'
  
  import org.springframework.integration.samples.cafe.Drink
  
  orderItem = payload
  
  puts "ruby: preparing #{orderItem} for order #{orderItem.order.number}"

  sleep(timeToPrepare.to_f) 
    
  Drink.new(orderItem.getOrder().getNumber(), orderItem.getDrinkType(), orderItem.isIced(),
              orderItem.getShots())
  

 
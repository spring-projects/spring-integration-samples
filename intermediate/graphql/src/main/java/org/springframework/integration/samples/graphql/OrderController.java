package org.springframework.integration.samples.graphql;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class OrderController {

    static final Map<String, Order> orders = new ConcurrentHashMap<>();
    static final Map<String, Customer> customers = new ConcurrentHashMap<>();

    static {

        customers.put("0", new Customer("0", "Dan"));
        customers.put("1", new Customer("1", "Artem"));

        for(var orderId = 1; orderId <= 10; orderId++) {
            var customerId = String.valueOf(orderId % 2);
            var order = new Order(String.valueOf(orderId), (orderId * 10), customerId);
            orders.put(String.valueOf(orderId),order);
            System.out.println(order);
        }
    }

    @QueryMapping
    Flux<Order> orders() {

        return Mono.just(orders.values())
                .flatMapIterable(values -> values);
    }

    @QueryMapping
    Mono<Order> orderById(@Argument String orderId) {

        return Mono.just(orders.get(orderId));
    }

    @SchemaMapping(typeName = "Customer")
    Flux<Order> ordersByCustomer(Customer customer) {

        return Flux.fromIterable(orders.values())
                .filter(order -> order.customerId().equals(customer.customerId()));
    }

    @QueryMapping
    Flux<Customer> customers() {

        return Mono.just(customers.values())
                .flatMapIterable(values -> values);
    }

}

record Order(String orderId, double amount, String customerId) { }
record Customer(String customerId, String name) { }

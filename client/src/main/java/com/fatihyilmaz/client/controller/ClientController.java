package com.fatihyilmaz.client.controller;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.vavr.CheckedFunction0;
import io.vavr.concurrent.Future;
import io.vavr.control.Try;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.atomic.AtomicInteger;

@Controller
@RequestMapping("/client")
public class ClientController {

    private final CheckedFunction0<ResponseEntity<String>> checkedSupplier;

    public ClientController(RestTemplate restTemplate, Bulkhead bulkhead) {
        checkedSupplier = Bulkhead.decorateCheckedSupplier(bulkhead, () -> restTemplate.getForEntity("http://localhost:8090/server/get-resource", String.class));
        bulkhead.getEventPublisher()
                .onEvent(event -> System.out.println("Bulkhead event occurred: " + event));
    }

    @GetMapping("/get-resource")
    public ResponseEntity<String> getResource() {
        AtomicInteger counter = new AtomicInteger(0);
        Future<ResponseEntity<String>> responseEntities = null;
        for (int i = 0; i < 100; i++) {
            responseEntities = Future.of(checkedSupplier).onSuccess(responseEntity -> counter.incrementAndGet())
                    .onFailure(ex -> System.out.println("Exception occurred: " + ex));//fallback
        }

        responseEntities.await();
        return ResponseEntity.ok("Number of successfull calls: " + counter.get());
    }
}

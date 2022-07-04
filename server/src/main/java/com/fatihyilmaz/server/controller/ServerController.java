package com.fatihyilmaz.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Random;

@Controller
@RequestMapping("/server")
public class ServerController {

    private final Random randomGenerator = new Random();

    @GetMapping("/get-resource")
    public ResponseEntity<String> getResource() throws InterruptedException {
        Thread.sleep(randomGenerator.nextInt(1000));
        return ResponseEntity.ok("You got the resource");
    }
}

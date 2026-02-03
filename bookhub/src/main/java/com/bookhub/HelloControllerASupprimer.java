package com.bookhub;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloControllerASupprimer {

    @GetMapping("/")
    public String helloWorld() {
        return "Hello World!";
    }

}
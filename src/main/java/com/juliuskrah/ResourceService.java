package com.juliuskrah;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1.0/resources")
public class ResourceService {

    @GetMapping(produces = { "text/plain" })
    String hello() {
        return "Hello World!";
    }

    @GetMapping("{first}/{last}")
    String hello(@PathVariable Map<String, String> pathMap) {
        return String.format("Hello %s %s!", pathMap.get("first"), pathMap.get("last"));
    }
}
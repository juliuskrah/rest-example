package com.juliuskrah;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1.0/resources",  produces = { "application/json", "application/xml" })
public class ResourceService {
    private static List<Resource> resources = null;

    static {
        resources = new ArrayList<>();
        resources.add(new Resource(1L, "Resource One", LocalDateTime.now(), null));
        resources.add(new Resource(2L, "Resource Two", LocalDateTime.now(), null));
        resources.add(new Resource(3L, "Resource Three", LocalDateTime.now(), null));
        resources.add(new Resource(4L, "Resource Four", LocalDateTime.now(), null));
        resources.add(new Resource(5L, "Resource Five", LocalDateTime.now(), null));
        resources.add(new Resource(6L, "Resource Six", LocalDateTime.now(), null));
        resources.add(new Resource(7L, "Resource Seven", LocalDateTime.now(), null));
        resources.add(new Resource(8L, "Resource Eight", LocalDateTime.now(), null));
        resources.add(new Resource(9L, "Resource Nine", LocalDateTime.now(), null));
        resources.add(new Resource(10L, "Resource Ten", LocalDateTime.now(), null));
    }

    @GetMapping
    public List<Resource> getResources() {
        return resources;
    }
}
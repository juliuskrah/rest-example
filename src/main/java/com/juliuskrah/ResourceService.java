/*
* Copyright 2017, Julius Krah
* by the @authors tag. See the LICENCE in the distribution for a
* full listing of individual contributors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* http://www.apache.org/licenses/LICENSE-2.0
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.juliuskrah;

import com.sun.org.apache.regexp.internal.RE;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1.0/resources", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
class ResourceService {

    private final PersonRepository personRepository;

    @PostConstruct
    private void initialize() {
        List<Resource> resources = new ArrayList<>();
        resources.add(new Resource(null, "Resource One", LocalDateTime.now(), null));
        resources.add(new Resource(null, "Resource Two", LocalDateTime.now(), null));
        resources.add(new Resource(null, "Resource Three", LocalDateTime.now(), null));
        resources.add(new Resource(null, "Resource Four", LocalDateTime.now(), null));
        resources.add(new Resource(null, "Resource Five", LocalDateTime.now(), null));
        resources.add(new Resource(null, "Resource Six", LocalDateTime.now(), null));
        resources.add(new Resource(null, "Resource Seven", LocalDateTime.now(), null));
        resources.add(new Resource(null, "Resource Eight", LocalDateTime.now(), null));
        resources.add(new Resource(null, "Resource Nine", LocalDateTime.now(), null));
        resources.add(new Resource(null, "Resource Ten", LocalDateTime.now(), null));

        personRepository.save(resources);
    }

    @GetMapping
    List<Resource> getResources() {
        return personRepository.findAll();
    }

    /**
     * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#core.web.basic
     * @param resource
     * @return
     */
    @GetMapping("{id:[0-9]+}")
    ResponseEntity<Resource> getResource(@PathVariable("id") Resource resource) {
        return Optional.ofNullable(resource)
                .map(r -> ResponseEntity.ok(resource))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    ResponseEntity<Void> createResource(@Valid @RequestBody Resource resource, UriComponentsBuilder b) {
        if (Objects.nonNull(resource.getId()))
            return ResponseEntity
                    .badRequest()
                    .build();

            resource.setCreatedTime(LocalDateTime.now());
            personRepository.save(resource);
            UriComponents uriComponents = b.path("/api/v1.0/resources/{id}")
                    .buildAndExpand(resource.getId());
            return ResponseEntity
                    .created(uriComponents.encode().toUri())
                    .build();
    }

    @PutMapping("{id:[0-9]+}")
    ResponseEntity<Void> updateResource(@PathVariable("id") Resource managedResource, @Valid @RequestBody Resource resource) {
        return Optional.ofNullable(managedResource)
                .map(r -> {
                    resource.setId(managedResource.getId());
                    personRepository.save(resource);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id:[0-9]+}")
    ResponseEntity<Void> deleteResource(@PathVariable("id") Resource resource) {
        return Optional.ofNullable(resource)
                .map(r -> {
                    personRepository.delete(resource);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
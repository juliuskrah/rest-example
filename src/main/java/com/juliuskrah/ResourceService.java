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

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping(path = "/api/v1.0/resources", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
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

    @GetMapping("{id:[0-9]+}")
    public ResponseEntity<Resource> getResource(@PathVariable Long id) {
        Resource resource = new Resource(id, null, null, null);
        int index = Collections.binarySearch(resources, resource, Comparator.comparing(Resource::getId));

        if (index >= 0)
            return ResponseEntity
                    .ok(resources.get(index));
        else
            return ResponseEntity
                    .notFound()
                    .build();
    }

    @PostMapping
    public ResponseEntity<Void> createResource(@RequestBody Resource resource, UriComponentsBuilder b) {
        if (Objects.isNull(resource.getId()))
            return ResponseEntity
                    .badRequest()
                    .build();
        int index = Collections.binarySearch(resources, resource, Comparator.comparing(Resource::getId));

        if (index < 0) {
            resource.setCreatedTime(LocalDateTime.now());
            resources.add(resource);
            UriComponents uriComponents = b.path("/api/v1.0/resources/{id}")
                    .buildAndExpand(resource.getId());
            return ResponseEntity
                    .created(uriComponents.toUri())
                    .build();
        } else
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build();
    }

    @PutMapping("{id:[0-9]+}")
    public ResponseEntity<Void> updateResource(@PathVariable Long id, @RequestBody Resource resource) {
        resource.setId(id);
        int index = Collections.binarySearch(resources, resource, Comparator.comparing(Resource::getId));

        if (index >= 0) {
            Resource updatedResource = resources.get(index);
            updatedResource.setModifiedTime(LocalDateTime.now());
            updatedResource.setDescription(resource.getDescription());
            resources.set(index, updatedResource);
            return ResponseEntity
                    .noContent()
                    .build();
        } else
            return ResponseEntity
                    .notFound()
                    .build();
    }

    @DeleteMapping("{id:[0-9]+}")
    public ResponseEntity<Void> deleteResource(@PathVariable Long id) {
        Resource resource = new Resource(id, null, null, null);
        int index = Collections.binarySearch(resources, resource, Comparator.comparing(Resource::getId));

        if (index >= 0) {
            resources.remove(index);
            return ResponseEntity
                    .noContent()
                    .build();
        } else
            return ResponseEntity
                    .notFound()
                    .build();
    }
}
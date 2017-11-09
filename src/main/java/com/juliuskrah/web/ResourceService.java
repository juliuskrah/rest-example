/*
* Copyright 2017, Julius Krah
* by the @authors tag. See the LICENCE in the distribution for a
* full listing of individual contributors.
*
* Licensed under the GNU General Public License, Version 3 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* http://www.gnu.org/licenses/
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.juliuskrah.web;

import com.juliuskrah.model.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;
import javax.ws.rs.PathParam;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;

@Slf4j
@Component
@Path("/api/v1.0/resources")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class ResourceService {

    private static List<Resource> resources = null;

    static {
        // Initialize list in a static block
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

    /**
     * GET  /api/v1.0/resources : get all resources.
     * 
     * @return the {@code List<Resource>} of resources with status code 200 (OK)
     */
    @GET
    public List<Resource> getResources() {
        return resources;
    }

    /**
     * GET /api/v1.0/resources/:id : get the resource specified by the identifier.
     * 
     * @param id the id to the resource being looked up
     * @return the {@code Resource} with status 200 (OK) and body or status 404 (NOT FOUND)
     */
    @GET
    @Path("{id: [0-9]+}")
    public Resource getResource(@PathParam("id") Long id) {
        Resource resource = new Resource(id, null, null, null);

        // Search for resource with given identifier in the Resources list
        int index = Collections.binarySearch(resources, resource, Comparator.comparing(Resource::getId));

        // If the index is not a negative value then it is in the list
        if (index >= 0)
            return resources.get(index);
        // A negative index suggest the resource cannot be found in the list
        else
            throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

    /**
     * POST /api/v1.0/resources : creates a new resource.
     * 
     * @param resource the resource being sent by the client as payload
     * @return the {@code Resource} with status 201 (CREATED) and no -content or status 
     *         400 (BAD REQUEST) if the resource does not contain an Id or status
     *         409 (CONFLICT) if the resource being created already exists in the list
     */
    @POST
    public Response createResource(Resource resource) {
        if (Objects.isNull(resource.getId()))
            throw new WebApplicationException(Response.Status.BAD_REQUEST);

        // Search for resource with the given identifier in the Resources list
        int index = Collections.binarySearch(resources, resource, Comparator.comparing(Resource::getId));

        // If the resource is a negative value then it does not exist so add to the list
        if (index < 0) {
            resource.setCreatedTime(LocalDateTime.now());
            resources.add(resource);
            return Response
                    .status(Response.Status.CREATED)
                    // Build a URI pointing to the location of the newly created resource
                    .location(URI.create(String.format("/api/v1.0/resources/%s", resource.getId())))
                    .build();
        // If the resource is found in the list, then return a 409 (CONFLICT)
        } else
            throw new WebApplicationException(Response.Status.CONFLICT);
    }

    /**
     * PUT /api/v1.0/resources/:id : update a resource identified by the given id
     * 
     * @param id the identifier of the resource to be updated
     * @param resource the resource that contains the update
     * @return the {@code Resource} with a status code 204 (NO CONTENT) or status code
     *         404 (NOT FOUND) when the resource being updated cannot be found
     */
    @PUT
    @Path("{id: [0-9]+}")
    public Response updateResource(@PathParam("id") Long id, Resource resource) {
        resource.setId(id);
        // search for the resource with the given identifier in the resources list
        int index = Collections.binarySearch(resources, resource, Comparator.comparing(Resource::getId));

        // If the index is a non-negative value, then the resource is contained in the list. Update it
        if (index >= 0) {
            Resource updatedResource = resources.get(index);
            updatedResource.setModifiedTime(LocalDateTime.now());
            updatedResource.setDescription(resource.getDescription());
            // Replace the resource in the list at the specified index with an updated resource
            resources.set(index, updatedResource);
            return Response
                    .status(Response.Status.NO_CONTENT)
                    .build();
        // The resource is not found in the list. Return a 404 (NOT FOUND)
        } else
            throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

    /**
     * DELETE /api/v1.0/resources/:id : delete a resource identified by the given id
     * 
     * @param id the identifier of the resource to be deleted
     * @return the {@code Response} with a status code of 204 (NO CONTENT) or status code
     *         404 (NOT FOUND) when there is no resource with the given identifier
     */
    @DELETE
    @Path("{id: [0-9]+}")
    public Response deleteResource(@PathParam("id") Long id) {
        Resource resource = new Resource(id, null, null, null);
        // search for the resource with the given identifier in the resources list
        int index = Collections.binarySearch(resources, resource, Comparator.comparing(Resource::getId));

        // When the index is non-negative, the resource is found in the list
        if (index >= 0) {
            resources.remove(index);
            return Response
                    .status(Response.Status.NO_CONTENT)
                    .build();
        // The resource is not found in the static list
        } else
            throw new WebApplicationException(Response.Status.NOT_FOUND);
    }
}
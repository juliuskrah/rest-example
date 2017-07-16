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
package com.juliuskrah;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;

@Path("/api/v1.0/resources")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
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

    @GET
    public List<Resource> getResources() {
        return resources;
    }

    @GET
    @Path("{id: [0-9]+}")
    public Resource getResource(@PathParam("id") Long id) {
        Resource resource = new Resource(id, null, null, null);

        int index = Collections.binarySearch(resources, resource, Comparator.comparing(Resource::getId));

        if (index >= 0)
            return resources.get(index);
        else
            throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createResource(Resource resource) {
        if (Objects.isNull(resource.getId()))
            throw new WebApplicationException(Response.Status.BAD_REQUEST);

        int index = Collections.binarySearch(resources, resource, Comparator.comparing(Resource::getId));

        if (index < 0) {
            resource.setCreatedTime(LocalDateTime.now());
            resources.add(resource);
            return Response
                    .status(Response.Status.CREATED)
                    .location(URI.create(String.format("/api/v1.0/resources/%s", resource.getId())))
                    .build();
        } else
            throw new WebApplicationException(Response.Status.CONFLICT);
    }


    @PUT
    @Path("{id: [0-9]+}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateResource(@PathParam("id") Long id, Resource resource) {
        resource.setId(id);
        int index = Collections.binarySearch(resources, resource, Comparator.comparing(Resource::getId));

        if (index >= 0) {
            Resource updatedResource = resources.get(index);
            updatedResource.setModifiedTime(LocalDateTime.now());
            updatedResource.setDescription(resource.getDescription());
            resources.set(index, updatedResource);
            return Response
                    .status(Response.Status.NO_CONTENT)
                    .build();
        } else
            throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

    @DELETE
    @Path("{id: [0-9]+}")
    public Response deleteResource(@PathParam("id") Long id) {
        Resource resource = new Resource(id, null, null, null);
        int index = Collections.binarySearch(resources, resource, Comparator.comparing(Resource::getId));

        if (index >= 0) {
            resources.remove(index);
            return Response
                    .status(Response.Status.NO_CONTENT)
                    .build();

        } else
            throw new WebApplicationException(Response.Status.NOT_FOUND);
    }
}
package com.juliuskrah;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
    public Response createResource(Resource resource) {
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
}
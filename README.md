[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![CircleCI](https://circleci.com/gh/juliuskrah/rest-example/tree/jersey-spring-jwt.svg?style=svg)](https://circleci.com/gh/juliuskrah/rest-example/tree/jersey-spring-jwt)
# REST Example for Java using Spring and Jersey (JWT)
Simple REST repository to accompany my [REST series](http://juliuskrah.com/tutorial/2017/07/26/developing-restful-services-with-spring-and-jersey/).  
The code in this repository has been augmented with Spring Security and JWT


# Quickstart
```bash
> git clone https://github.com/juliuskrah/rest-example.git -b jersey-spring-jwt
> cd rest-example
> mvnw clean spring-boot:run
```

Highlights

```java
@Component
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
```

Create a bean to register `Jersey`:

```java
@Bean
public ResourceConfig jerseyConfig() {
    ResourceConfig config = new ResourceConfig();
    config.register(ResourceService.class);
    return config;
}
```
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

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

public class ResourceServiceTest extends JerseyTest {
	@Override
    protected Application configure() {
		ResourceConfig rc = new ResourceConfig();
		ConfigurableApplicationContext ctx = SpringApplication.run(com.juliuskrah.Application.class);
  		rc.property("contextConfig", ctx);
        return rc;
    }

    @Test
    public void testGetResources() {
        List<Resource> resources = target("/api/v1.0/resources/")
                .request().get(new GenericType<List<Resource>>() {
                });
        assertThat(resources, is(not(empty())));
        assertThat(resources.stream().findFirst().get().getDescription(), is("Resource One"));
        assertThat(resources, hasSize(10));
    }

    @Test
    public void testGetResource() {
        Response response = target("/api/v1.0/resources/")
                .path("{id}")
                .resolveTemplate("id", 4L)
                .request(MediaType.APPLICATION_XML_TYPE)
                .get();

        assertThat(response, is(notNullValue()));
        assertThat(response.getMediaType().toString(), is(equalTo(MediaType.APPLICATION_XML)));
        assertThat(response.getStatus(), is(equalTo(200)));
    }

    @Test
    public void testCreateResource() {
        Resource resource = new Resource(87L, "Resource Eighty-Seven", LocalDateTime.now(), null);
        Response response = target("/api/v1.0/resources/")
                .request(MediaType.APPLICATION_XML_TYPE)
                .post(Entity.xml(resource));
        assertThat(response, is(notNullValue()));
        assertThat(response.getStatus(), is(equalTo(201)));
        assertThat(response.getLocation(), is(equalTo(URI.create(String
                .format("http://localhost:9998/api/v1.0/resources/%s", resource.getId())))));
    }

    @Test
    public void testUpdateResource() {
        Resource resource = new Resource(7L, "Resource Seven modified", LocalDateTime.now(), null);
        Response response = target("/api/v1.0/resources/")
                .path("{id}")
                .resolveTemplate("id", resource.getId())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.json(resource));
        assertThat(response, is(notNullValue()));
        assertThat(response.getStatus(), is(equalTo(204)));
    }

    @Test
    public void testDeleteResource() {
        Response response = target("/api/v1.0/resources/")
                .path("{id}")
                .resolveTemplate("id", 8L)
                .request()
                .delete();
        assertThat(response, is(notNullValue()));
        assertThat(response.getStatus(), is(equalTo(204)));
    }
}

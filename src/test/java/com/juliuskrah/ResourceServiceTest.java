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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_XML;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
public class ResourceServiceTest {
	@Autowired
	private TestRestTemplate restTemplate;
	@Autowired
	private MockMvc restMvc;

	@Test
	public void testGetResources() {
		ResponseEntity<List<Resource>> response = this.restTemplate.exchange("/api/v1.0/resources", GET, null,
				new ParameterizedTypeReference<List<Resource>>() {
				});

		assertThat(response.getBody()).isNotNull();
		List<Resource> resources = response.getBody();

		assertThat(resources).isNotEmpty();
		assertThat(response.getStatusCode()).isSameAs(OK);
		assertThat(resources).hasOnlyElementsOfType(Resource.class);
		// assertThat(resources).asList().isSorted();
	}

	@Test
	public void testGetResourcesMock() throws Exception {
		// @formatter:off
		this.restMvc.perform(get("/api/v1.0/resources/")
				.accept(APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[*].id", hasItems(1, 3, 8)))
				.andExpect(jsonPath("$[*].description", hasItems("Resource Three", "Resource Four")));
		// @formatter:on
	}

	@Test
	public void testGetResourceIsFound() {
		Resource resource = this.restTemplate.getForObject("/api/v1.0/resources/{id}", Resource.class, 2L);

		assertThat(resource).isNotNull();
		assertThat(resource.getDescription()).isEqualTo("Resource Two");
	}

	@Test
	public void testGetResourceIsNotFound() throws Exception {
		// @formatter:off
		this.restMvc.perform(get("/api/v1.0/resources/{id}", 11L)
				.accept(APPLICATION_XML))
				.andExpect(status().isNotFound());
		// @formatter:on
	}

	@Test
	public void testCreateResourceIsBadRequest() {
		Resource resource = new Resource(48L, "Reource Two Conflict", LocalDateTime.now(), null);
		HttpEntity<Resource> entity = new HttpEntity<>(resource);
		ResponseEntity<Void> response = this.restTemplate.exchange("/api/v1.0/resources", POST, entity, Void.class);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isSameAs(BAD_REQUEST);
	}

	@Test
	public void testCreateResourceIsCreated() throws IOException, Exception {
		// @formatter:off
		Resource resource = new Resource(null, "Reource Eighty-Seven", LocalDateTime.now(), null);

		this.restMvc.perform(post("/api/v1.0/resources")
				.contentType(APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(resource)))
				.andExpect(status().isCreated())
				.andExpect(header().string("location", containsString("/api/v1.0/resources/")));
		// @formatter:on
	}

	@Test
	public void testUpdateResourceIsNoContent() throws IOException, Exception {
		// @formatter:off
		Resource resource = new Resource(null, "Reource Three Updated", LocalDateTime.now(), null);
		
		this.restMvc.perform(put("/api/v1.0/resources/{id}", 3L)
				.accept(APPLICATION_JSON)
				.contentType(APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(resource)))
				.andExpect(status().isNoContent());	
		// @formatter:off
	}

	@Test
	public void testUpdateResourceIsNotFound() {
		Resource resource = new Resource(null, "Reource Twenty Updated", LocalDateTime.now(), null);
		HttpEntity<Resource> entity = new HttpEntity<>(resource);
		ResponseEntity<Void> response = this.restTemplate.exchange("/api/v1.0/resources{id}", PUT, entity, Void.class, 20L);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isSameAs(NOT_FOUND);
	}
	
	@Test 
	public void testDeleteResourceIsNotFound() throws Exception {
		// @formatter:off				
		this.restMvc.perform(delete("/api/v1.0/resources/{id}", 34L)
				.accept(APPLICATION_JSON))
				.andExpect(status().isNotFound());
		// @formatter:off	
	}
	
	@Test 
	public void testDeleteResourceIsNoContent() {
		ResponseEntity<Void> response = this.restTemplate.exchange("/api/v1.0/resources/{id}", DELETE, null, Void.class, 9L);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isSameAs(NO_CONTENT);
	}
}

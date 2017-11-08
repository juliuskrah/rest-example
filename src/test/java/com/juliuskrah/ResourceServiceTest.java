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

import static io.restassured.RestAssured.*;
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
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import com.juliuskrah.model.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ResourceServiceTest {
	@Autowired
	private TestRestTemplate restTemplate;
	@LocalServerPort
	private int port; // Added for RestAssured tests

	@Test
	public void testGetResources() {
		ResponseEntity<List<Resource>> response = this.restTemplate.exchange("/api/v1.0/resources", GET, null,
				new ParameterizedTypeReference<List<Resource>>() {
				});

		Assertions.assertThat(response.getBody()).isNotNull();
		List<Resource> resources = response.getBody();

		Assertions.assertThat(resources).isNotEmpty();
		assertThat(response.getStatusCode()).isSameAs(OK);
		Assertions.assertThat(resources).hasOnlyElementsOfType(Resource.class);
	}

	@Test
	public void testGetResourcesRestAssured() throws Exception {
		// @formatter:off
		given()
			.port(this.port)
		.when()
			.get("/api/v1.0/resources")
		.then()
			.body("findAll { it.id < 10 }.description", 
				hasItems("Resource Two", "Resource Five", "Resource Six", "Resource Seven"));
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
		given()
			.port(this.port)
		.when()
			.get("/api/v1.0/resources/{id}", 45L)
		.then()
			.assertThat().statusCode(NOT_FOUND.value());
		// @formatter:on
	}

	@Test
	public void testCreateResourceIsConflict() {
		Resource resource = new Resource(2L, "Reource Two Conflict", LocalDateTime.now(), null);
		HttpEntity<Resource> entity = new HttpEntity<>(resource);
		ResponseEntity<Void> response = this.restTemplate.exchange("/api/v1.0/resources", POST, entity, Void.class);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isSameAs(CONFLICT);
	}

	@Test
	public void testCreateResourceIsBadRequest() {
		Resource resource = new Resource(null, "Reource Two Conflict", LocalDateTime.now(), null);
		HttpEntity<Resource> entity = new HttpEntity<>(resource);
		ResponseEntity<Void> response = this.restTemplate.exchange("/api/v1.0/resources", POST, entity, Void.class);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isSameAs(BAD_REQUEST);
	}

	@Test
	public void testCreateResourceIsCreated() throws IOException, Exception {
		// @formatter:off
		Resource resource = new Resource(87L, "Reource Eighty-Seven", LocalDateTime.now(), null);

		given()
			.port(this.port)
			.contentType(APPLICATION_XML_VALUE)
			.body(resource)
		.when()
			.post("/api/v1.0/resources")
		.then()
			.statusCode(CREATED.value())
			.header(HttpHeaders.LOCATION, containsString(String.format("/api/v1.0/resources/%s", resource.getId())));
		// @formatter:on
	}

	@Test
	public void testUpdateResourceIsNoContent() throws IOException, Exception {
		// @formatter:off
		Resource resource = new Resource(null, "Reource Three Updated", LocalDateTime.now(), null);
		
		given()
			.port(this.port)
			.contentType(APPLICATION_XML_VALUE)
			.body(resource)
		.when()
			.put("/api/v1.0/resources/{id}", 3L)
		.then()
			.statusCode(NO_CONTENT.value());
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
		given()
			.port(this.port)
			.accept(APPLICATION_XML_VALUE)
		.when()
			.delete("/api/v1.0/resources/{id}", 34L)
		.then()
			.statusCode(NOT_FOUND.value());
		// @formatter:off	
	}
	
	@Test 
	public void testDeleteResourceIsNoContent() {
		ResponseEntity<Void> response = this.restTemplate.exchange("/api/v1.0/resources/{id}", DELETE, null, Void.class, 9L);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isSameAs(NO_CONTENT);
	}
}

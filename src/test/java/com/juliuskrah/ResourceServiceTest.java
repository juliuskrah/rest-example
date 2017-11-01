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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_XML;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
public class ResourceServiceTest {
    @Autowired
    private MockMvc restMvc;

    @Test
    @WithMockUser
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
    @WithMockUser
    public void testGetResourceIsFound() throws Exception {
        this.restMvc.perform(get("/api/v1.0/resources/{id}", 2L)
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("Resource Two")));
    }

    @Test
    @WithMockUser
    public void testGetResourceIsNotFound() throws Exception {
        // @formatter:off
        this.restMvc.perform(get("/api/v1.0/resources/{id}", 11L)
                .accept(APPLICATION_XML))
                .andExpect(status().isNotFound());
        // @formatter:on
    }

    @Test
    @WithMockUser
    public void testCreateResourceIsConflict() throws Exception {
        Resource resource = new Resource(2L, "Reource Two Conflict", LocalDateTime.now(), null);
        this.restMvc.perform(post("/api/v1.0/resources")
                .contentType(APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(resource))
                .with(csrf()))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser
    public void testCreateResourceIsBadRequest() throws  Exception{
        Resource resource = new Resource(null, "Reource Two Bad Request", LocalDateTime.now(), null);
        this.restMvc.perform(post("/api/v1.0/resources")
                .contentType(APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(resource))
                .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void testCreateResourceIsCreated() throws Exception {
        // @formatter:off
        Resource resource = new Resource(87L, "Reource Eighty-Seven", LocalDateTime.now(), null);

        this.restMvc.perform(post("/api/v1.0/resources")
                .contentType(APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(resource)))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", containsString(String.format("/api/v1.0/resources/%s", resource.getId()))));
        // @formatter:on
    }

    @Test
    @WithMockUser
    public void testUpdateResourceIsNoContent() throws Exception {
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
    @WithMockUser
    public void testUpdateResourceIsNotFound() throws Exception {
        Resource resource = new Resource(null, "Reource Twenty Updated", LocalDateTime.now(), null);
      this.restMvc.perform(put("/api/v1.0/resources/{id}", 20L)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(resource)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testDeleteResourceIsNotFound() throws Exception {
        // @formatter:off
        this.restMvc.perform(delete("/api/v1.0/resources/{id}", 34L)
                .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound());
        // @formatter:off
    }

    @Test
    @WithMockUser
    public void testDeleteResourceIsNoContent() throws Exception {
        this.restMvc.perform(delete("/api/v1.0/resources/{id}", 9L)
                .accept(APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}

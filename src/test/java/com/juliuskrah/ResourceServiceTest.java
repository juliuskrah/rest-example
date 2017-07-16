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

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;


public class ResourceServiceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(ResourceService.class);
    }

    @Test
    public void testGetResources() {
        List<Resource> resources = target("/api/v1.0/resources/")
                .request().get(new GenericType<List<Resource>>() {
                });
        assertThat(resources, is(not(empty())));
        /*
        assertThat(resources, hasItems(
                new Resource(1L, "Resource One", LocalDateTime.now(), null),
                new Resource(10L, "Resource Ten", LocalDateTime.now(), null)
                )
        );*/
    }
}

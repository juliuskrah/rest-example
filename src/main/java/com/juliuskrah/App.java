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

import io.netty.channel.Channel;
import org.glassfish.jersey.netty.httpserver.NettyHttpContainerProvider;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;

public class App {
    public static void main(String... cmd) throws IOException, InterruptedException {
        int port = Objects.nonNull(System.getenv("PORT")) ? Integer.valueOf(System.getenv("PORT")) : 8080;
        URI baseUri = UriBuilder.fromUri("http://localhost/").port(port).build();
        ResourceConfig resourceConfig = new ResourceConfig().packages("com.juliuskrah");
        NettyHttpContainerProvider.createServer(baseUri, resourceConfig, false);
        System.out.printf("Application running on %s\n", baseUri.toURL().toExternalForm());
    }
}

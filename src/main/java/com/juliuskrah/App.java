package com.juliuskrah;

import org.glassfish.jersey.netty.httpserver.NettyHttpContainerProvider;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class App {
    public static void main(String... cmd) {
        URI baseUri = UriBuilder.fromUri("http://localhost/").port(8080).build();
        ResourceConfig resourceConfig = new ResourceConfig().packages("com.juliuskrah");
        NettyHttpContainerProvider.createServer(baseUri, resourceConfig, false);
    }
}

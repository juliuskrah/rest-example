package com.juliuskrah;

import io.netty.channel.Channel;
import org.glassfish.jersey.netty.httpserver.NettyHttpContainerProvider;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;

public class App {
    public static void main(String... cmd) throws IOException, InterruptedException {
        URI baseUri = UriBuilder.fromUri("http://localhost/").port(8080).build();
        ResourceConfig resourceConfig = new ResourceConfig().packages("com.juliuskrah");
        Channel server = NettyHttpContainerProvider.createServer(baseUri, resourceConfig, false);
        System.out.println("Press ENTER to terminate...");
        System.in.read();
        server.close().await();
    }
}

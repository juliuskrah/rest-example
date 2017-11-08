package com.juliuskrah.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.security.Principal;

@Component
public class BridgingSecurityFilter implements ContainerRequestFilter {
    @Context
    UriInfo uriInfo;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        containerRequestContext.setSecurityContext(
                new BridgingSecurityContext(SecurityContextHolder.getContext(), this.uriInfo));
    }

    private static class BridgingSecurityContext implements SecurityContext {
        private final org.springframework.security.core.context.SecurityContext spring;
        private final UriInfo uriInfo;

        private BridgingSecurityContext(org.springframework.security.core.context.SecurityContext spring, UriInfo uriInfo) {
            this.spring = spring;
            this.uriInfo = uriInfo;
        }

        @Override
        public Principal getUserPrincipal() {
            return spring.getAuthentication();
        }

        @Override
        public boolean isUserInRole(String s) {
            return spring.getAuthentication().getAuthorities()
                    .stream()
                    .anyMatch(ga -> ga.getAuthority().contains(s));
        }

        @Override
        public boolean isSecure() {
            return uriInfo.getAbsolutePath().toString().toLowerCase().startsWith("https");
        }

        @Override
        public String getAuthenticationScheme() {
            return "spring-security-authentication";
        }

    }
}

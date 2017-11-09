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
//package com.juliuskrah.security;
//
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//
//import javax.ws.rs.container.ContainerRequestContext;
//import javax.ws.rs.container.ContainerRequestFilter;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.SecurityContext;
//import javax.ws.rs.core.UriInfo;
//import java.io.IOException;
//import java.security.Principal;
//
//@Component
//public class BridgingSecurityFilter implements ContainerRequestFilter {
//    @Context
//    UriInfo uriInfo;
//
//    @Override
//    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
//        containerRequestContext.setSecurityContext(
//                new BridgingSecurityContext(SecurityContextHolder.getContext(), this.uriInfo));
//    }
//
//    private static class BridgingSecurityContext implements SecurityContext {
//        private final org.springframework.security.core.context.SecurityContext spring;
//        private final UriInfo uriInfo;
//
//        private BridgingSecurityContext(org.springframework.security.core.context.SecurityContext spring, UriInfo uriInfo) {
//            this.spring = spring;
//            this.uriInfo = uriInfo;
//        }
//
//        @Override
//        public Principal getUserPrincipal() {
//            return spring.getAuthentication();
//        }
//
//        @Override
//        public boolean isUserInRole(String s) {
//            return spring.getAuthentication().getAuthorities()
//                    .stream()
//                    .anyMatch(ga -> ga.getAuthority().contains(s));
//        }
//
//        @Override
//        public boolean isSecure() {
//            return uriInfo.getAbsolutePath().toString().toLowerCase().startsWith("https");
//        }
//
//        @Override
//        public String getAuthenticationScheme() {
//            return "Bearer";
//        }
//
//    }
//}

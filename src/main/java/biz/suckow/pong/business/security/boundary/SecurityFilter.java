package biz.suckow.pong.business.security.boundary;

import java.io.IOException;
import java.security.Principal;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import biz.suckow.pong.business.security.control.TokenAuthority;

@Provider
@PreMatching
@Priority(Priorities.AUTHENTICATION)
public class SecurityFilter implements ContainerRequestFilter {
    @Inject
    private TokenAuthority authority;

    // see http://howtodoinjava.com/2013/06/26/jax-rs-resteasy-basic-authentication-and-authorization-tutorial/
    @Override
    public void filter(final ContainerRequestContext context) throws IOException {
	final String token = context.getHeaderString(TokenAuthority.TOKEN_HEADER_NAME);
	context.setSecurityContext(new SecurityContext() {
	    public static final String TOKEN_AUTH = "TOKEN_AUTH";

	    @Override
	    public boolean isUserInRole(final String arg0) {
		// TODO Auto-generated method stub
		return false;
	    }

	    @Override
	    public boolean isSecure() {
		return context.getSecurityContext().isSecure();
	    }

	    @Override
	    public Principal getUserPrincipal() {
		// TODO Auto-generated method stub
		return null;
	    }

	    @Override
	    public String getAuthenticationScheme() {
		return TOKEN_AUTH;
	    }
	});
	// context.abortWith(Response.status(Status.UNAUTHORIZED).build());
    }
}

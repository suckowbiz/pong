package biz.suckow.pong.business.security.boundary;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import biz.suckow.pong.business.security.control.UserLocator;
import biz.suckow.pong.business.security.entity.User;

@Provider
@PreMatching
@Priority(Priorities.AUTHENTICATION)
public class SecurityFilter implements ContainerRequestFilter {
    public static final String TOKEN_HEADER_NAME ="X-PONG-TOKEN";

    @Inject
    private UserLocator userLocator;

    // see http://howtodoinjava.com/2013/06/26/jax-rs-resteasy-basic-authentication-and-authorization-tutorial/
    @Override
    public void filter(final ContainerRequestContext context) throws IOException {
	if (true) {
	    // disabled so far
	    return;
	}

	final String token = context.getHeaderString(TOKEN_HEADER_NAME);
	final Optional<User> possibleUser = this.userLocator.locate(token);
	context.setSecurityContext(new SecurityContext() {
	    public static final String TOKEN_AUTH = "TOKEN_AUTH";

	    @Override
	    public boolean isUserInRole(final String role) {
		boolean result = false;
		if (role == null || role.isEmpty()) {
		    return result;
		}
		if (role.equals(possibleUser.get().getRole())) {
		    result = true;
		}
		return result;
	    }

	    @Override
	    public boolean isSecure() {
		return context.getSecurityContext().isSecure();
	    }

	    @Override
	    public Principal getUserPrincipal() {
		Principal result = null;
		if (possibleUser.isPresent()) {
		    result = () -> possibleUser.get().getUsername();
		}
		return result;
	    }

	    @Override
	    public String getAuthenticationScheme() {
		String result = null;
		if (possibleUser.isPresent()) {
		    result = TOKEN_AUTH;
		}
		return result;
	    }
	});
    }
}

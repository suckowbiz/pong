package biz.suckow.pong.business.security.boundary;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import biz.suckow.pong.business.security.control.TokenAuthority;
import biz.suckow.pong.business.security.entity.Role;

public class SecurityRoleFilter implements ContainerRequestFilter {
    public static final String TOKEN_HEADER_NAME ="X-PONG-TOKEN";
    private final Role requiredRole;
    private final TokenAuthority authority;

    public SecurityRoleFilter(final Role requiredRole, final TokenAuthority authority) {
	this.requiredRole = requiredRole;
	this.authority = authority;
    }

    @Override
    public void filter(final ContainerRequestContext context) throws IOException {
	final String actualToken = context.getHeaderString(TOKEN_HEADER_NAME);
	final boolean inRequiredRole = this.isUserInRequiredRole(actualToken);
	if (inRequiredRole == false) {
	    context.abortWith(Response.status(Status.UNAUTHORIZED).entity("Request not in required role.").build());
	}

    }

    private boolean isUserInRequiredRole(final String token) {
	boolean result = false;
	final Role actualRole = this.authority.getRole(token);
	if (Role.SUPER.equals(actualRole) || this.requiredRole.equals(actualRole)) {
	    result = true;
	}
	return result;
    }

}

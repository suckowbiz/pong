package biz.suckow.pong.business.security.boundary;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import biz.suckow.pong.business.hosts.boundary.HostsResource;
import biz.suckow.pong.business.hosts.control.HostStore;
import biz.suckow.pong.business.hosts.entity.Host;
import biz.suckow.pong.business.security.control.TokenAuthority;
import biz.suckow.pong.business.security.entity.Role;

@Priority(Priorities.AUTHENTICATION)
@Provider
public class SecurityIdentityFilter implements ContainerRequestFilter {
    @Inject
    private TokenAuthority authority;

    @Inject
    private HostStore store;

    @Override
    public void filter(final ContainerRequestContext requestContext) throws IOException {
	final String actualToken = requestContext.getHeaderString(SecurityRoleFilter.TOKEN_HEADER_NAME);
	final Role actualRole = this.authority.getRole(actualToken);
	if (Role.SUPER.equals(actualRole)) {
	    return;
	}

	// in case the target user exists the actual user must be identical to
	// it to avoid changes other users data
	final Optional<String> possibleTargetUser = this.getTargetUser(requestContext, HostsResource.PATH_PARAM_HOSTNAME);
	if (possibleTargetUser.isPresent()) {
	    final Consumer<? super Host> consumer = host -> {
		if (host.getHostname().equals(possibleTargetUser) == false) {
		    requestContext.abortWith(Response.status(Status.UNAUTHORIZED).build());
		}
	    };
	    final Optional<Host> host = this.store.getByToken(actualToken);
	    host.ifPresent(consumer);
	}
    }

    private final Optional<String> getTargetUser(final ContainerRequestContext context, final String key) {
	Optional<String> result = Optional.empty();
	final List<String> values = context.getUriInfo().getPathParameters().get(key);
	if (values == null) {
	    return result;
	}
	result = Optional.of(values.get(0));
	return result;
    }

}

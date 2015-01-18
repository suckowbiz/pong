package biz.suckow.pong.business.security.control;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response.Status;

import biz.suckow.pong.business.hosts.boundary.HostsResource;
import biz.suckow.pong.business.hosts.control.HostStore;
import biz.suckow.pong.business.hosts.entity.Host;
import biz.suckow.pong.business.security.entity.Identity;

@Identity
public class IdentityStrategy implements SecurityStrategy {
    @Inject
    private HostStore store;

    @Override
    public Optional<Status> getAbortReason(final ContainerRequestContext context) {
	Optional<Status> result = Optional.empty();
	final String token = context.getHeaderString(SecurityFilter.TOKEN_HEADER_NAME);
	if (token == null || token.trim().isEmpty()) {
	    result = Optional.of(Status.UNAUTHORIZED);
	    return result;
	}

	final Optional<String> possibleTargetHostname = this.getTargetHostname(context,
		HostsResource.PATH_PARAM_HOSTNAME);
	if (possibleTargetHostname.isPresent()) {
	    result = this.getIdentityAbortReason(possibleTargetHostname.get(), token);
	} else {
	    result = Optional.of(Status.UNAUTHORIZED);
	}
	return result;
    }

    private final Optional<String> getTargetHostname(final ContainerRequestContext context, final String key) {
	Optional<String> result = Optional.empty();
	final List<String> values = context.getUriInfo().getPathParameters().get(key);
	if (values == null) {
	    return result;
	}
	result = Optional.of(values.get(0));
	return result;
    }

    private Optional<Status> getIdentityAbortReason(final String targetHostname, final String token) {
	Optional<Status> result = Optional.empty();
	final Optional<Host> existingHost = this.store.getByHostname(targetHostname);
	if (existingHost.isPresent() == false) {
	    return result;
	}

	final Optional<Host> callingHost = this.store.getByToken(token);
	if (callingHost.isPresent() == false
		|| callingHost.get().getHostname().equals(existingHost.get().getHostname()) == false) {
	    result = Optional.of(Status.FORBIDDEN);
	}
	return result;
    }
}

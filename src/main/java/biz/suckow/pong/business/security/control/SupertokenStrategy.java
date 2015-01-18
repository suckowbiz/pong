package biz.suckow.pong.business.security.control;

import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response.Status;

import biz.suckow.pong.business.security.entity.Supertoken;

@Supertoken
public class SupertokenStrategy implements SecurityStrategy {
    @Inject
    private String supertoken;

    @Override
    public Optional<Status> getAbortReason(final ContainerRequestContext context) {
	Optional<Status> result = Optional.empty();
	final String token = context.getHeaderString(SecurityFilter.TOKEN_HEADER_NAME);
	if (token == null || token.trim().isEmpty()) {
	    result = Optional.of(Status.UNAUTHORIZED);
	    return result;
	}

	final boolean isSupertoken = this.supertoken.equals(token);
	if (isSupertoken == false) {
	    result = Optional.of(Status.FORBIDDEN);
	}
	return result;
    }

}

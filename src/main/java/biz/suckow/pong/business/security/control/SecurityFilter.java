package biz.suckow.pong.business.security.control;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class SecurityFilter implements ContainerRequestFilter {
    public static final String TOKEN_HEADER_NAME = "X-PONG-TOKEN";
    private final SecurityStrategy strategy;

    public SecurityFilter(final SecurityStrategy strategy) {
	this.strategy = Objects.requireNonNull(strategy);
    }

    @Override
    public void filter(final ContainerRequestContext context) throws IOException {
	final Optional<Status> possibleAbortReason = this.strategy.getAbortReason(context);
	if (possibleAbortReason.isPresent()) {
	    context.abortWith(Response.status(possibleAbortReason.get()).build());
	    return;
	}
    }

}

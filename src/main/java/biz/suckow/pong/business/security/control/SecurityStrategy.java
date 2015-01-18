package biz.suckow.pong.business.security.control;

import java.util.Optional;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response.Status;

public interface SecurityStrategy {
    Optional<Status> getAbortReason(ContainerRequestContext context);
}

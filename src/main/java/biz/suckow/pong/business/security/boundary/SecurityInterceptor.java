package biz.suckow.pong.business.security.boundary;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;

import biz.suckow.pong.business.security.control.TokenAuthority;

@SuppressWarnings("deprecation")
@ServerInterceptor
public class SecurityInterceptor implements PreProcessInterceptor {
    @Inject
    private TokenAuthority authority;
    // see http://howtodoinjava.com/2013/06/26/jax-rs-resteasy-basic-authentication-and-authorization-tutorial/
    @Override
    public ServerResponse preProcess(final HttpRequest request, final ResourceMethodInvoker method) throws Failure,
    WebApplicationException {
	final String token = request.getHttpHeaders().getHeaderString(TokenAuthority.TOKEN_HEADER_NAME);
	final boolean isAnIssue = this.authority.isAnIssue(token);
	if (isAnIssue) {
	    return ServerResponse.status(Status.UNAUTHORIZED).build();
	};
	return null;
    }
}

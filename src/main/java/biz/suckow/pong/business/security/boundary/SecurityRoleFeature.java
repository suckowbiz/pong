package biz.suckow.pong.business.security.boundary;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

import biz.suckow.pong.business.security.control.TokenAuthority;
import biz.suckow.pong.business.security.entity.TokenRoleAllowed;

@Priority(Priorities.AUTHENTICATION)
@Provider
public class SecurityRoleFeature implements DynamicFeature {
    @Inject
    private TokenAuthority authority;

    @Override
    public void configure(final ResourceInfo resourceInfo, final FeatureContext context) {
	final TokenRoleAllowed annotation = resourceInfo.getResourceMethod().getAnnotation(TokenRoleAllowed.class);
	if (annotation == null) {
	    return;
	}
	context.register(new SecurityRoleFilter(annotation.value(), this.authority));
    }

}

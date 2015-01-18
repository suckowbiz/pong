package biz.suckow.pong.business.security.boundary;

import java.util.Objects;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

import biz.suckow.pong.business.security.control.SecurityFilter;
import biz.suckow.pong.business.security.control.SecurityStrategy;
import biz.suckow.pong.business.security.entity.Identity;
import biz.suckow.pong.business.security.entity.Secured;
import biz.suckow.pong.business.security.entity.Supertoken;

@Priority(Priorities.AUTHENTICATION)
@Provider
public class SecurityFeature implements DynamicFeature {
    @Inject
    @Supertoken
    private SecurityStrategy supertokenStrategy;

    @Inject
    @Identity
    private SecurityStrategy identityStrategy;

    @Override
    public void configure(final ResourceInfo resourceInfo, final FeatureContext context) {
	final Secured annotation = resourceInfo.getResourceMethod().getAnnotation(Secured.class);
	if (annotation == null) {
	    return;
	}

	SecurityStrategy strategy = null;
	switch (annotation.value()) {
	case IDENTITY:
	    strategy = this.identityStrategy;
	    break;
	case SUPERTOKEN:
	    strategy = this.supertokenStrategy;
	    break;
	default:
	    break;
	}
	context.register(new SecurityFilter(Objects.requireNonNull(strategy)));
    }

}

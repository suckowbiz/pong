package biz.suckow.pong.business.security.control;

import java.util.Optional;

import javax.inject.Inject;

import biz.suckow.pong.business.hosts.control.HostStore;
import biz.suckow.pong.business.hosts.entity.Host;
import biz.suckow.pong.business.security.entity.Role;

public class TokenAuthority {
    @Inject
    private HostStore hostStore;

    @Inject
    private String supertoken;

    public Role getRole(final String token) {
	if (this.supertoken.equals(token)) {
	    return Role.SUPER;
	}

	Role result = Role.UNDEFINED;
	final Optional<Host> host = this.hostStore.getByToken(token);
	if (host.isPresent()) {
	    if (this.supertoken.equals(host.get().getToken())) {
		result = Role.USER;
	    }
	}
	return result;
    }

}

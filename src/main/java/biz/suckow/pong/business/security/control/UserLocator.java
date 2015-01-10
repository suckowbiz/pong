package biz.suckow.pong.business.security.control;

import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import biz.suckow.pong.business.config.boundary.ConfigurationService;
import biz.suckow.pong.business.security.entity.User;

public class UserLocator {
    @Inject
    private ConfigurationService configurationService;

    public Optional<User> locate(final String token) {
	Optional<User> result = Optional.empty();
	if (token == null || token.isEmpty()) {
	    return result;
	}

	final Set<User> users = this.configurationService.getUsers();
	for (final User user : users) {
	    if (token.equals(user.getToken())) {
		result = Optional.of(user);
		break;
	    }
	}
	return result;
    }
}

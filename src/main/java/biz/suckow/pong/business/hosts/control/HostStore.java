package biz.suckow.pong.business.hosts.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import biz.suckow.pong.business.hosts.entity.Host;

@Singleton
public class HostStore {
    @Inject
    private Logger logger;

    private final Set<Host> cache = new ConcurrentSkipListSet<Host>();

    public void add(final Host remoteHost) {
	if (this.cache.contains(remoteHost)) {
	    this.cache.remove(remoteHost);
	}
	this.cache.add(remoteHost);
	this.logger.log(Level.INFO, "Considered: {0}. Actual store size: {1} item(s).", new Object[] { remoteHost,
		this.cache.size() });
    }

    public Optional<Host> getByToken(final String token) {
	Optional<Host> result = Optional.empty();
	if (token == null) {
	    return result;
	}
	for (final Host host : this.cache) {
	    if (token.equals(host.getToken())) {
		result = Optional.of(host);
		break;
	    }
	}
	return result;
    }

    public Optional<Host> getByHostname(final String hostname) {
	Optional<Host> result = Optional.empty();
	if (hostname == null) {
	    return result;
	}
	for (final Host host : this.cache) {
	    if (hostname.equalsIgnoreCase(host.getHostname())) {
		result = Optional.of(host);
		break;
	    }
	}
	return result;
    }

    public List<Host> getAll() {
	final List<Host> result = new ArrayList<Host>(this.cache);
	return result;
    }

}

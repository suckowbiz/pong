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

    public Optional<Host> get(final String hostname) {
	Optional<Host> result = Optional.empty();
	for (final Host remoteHost : this.cache) {
	    if (remoteHost.getHostname().equalsIgnoreCase(hostname)) {
		result = Optional.of(remoteHost);
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

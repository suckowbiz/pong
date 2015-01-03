package biz.suckow.pong.business.hosts.control;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import biz.suckow.pong.business.hosts.entity.RemoteHost;

public class IPStore {
    private final Set<RemoteHost> cache = new ConcurrentSkipListSet<RemoteHost>();

    public void save(final RemoteHost remoteHost) {
	if (this.cache.contains(remoteHost)) {
	    this.cache.remove(remoteHost);
	}
	if (this.cache.size() >= 1000) {
	    this.cache.clear();
	}
	this.cache.add(remoteHost);
    }

    public Optional<RemoteHost> get(final String hostname) {
	Optional<RemoteHost> result = Optional.empty();
	for (final RemoteHost remoteHost : this.cache) {
	    if (remoteHost.getHostname().equalsIgnoreCase(hostname)) {
		result = Optional.of(remoteHost);
		break;
	    }
	}
	return result;
    }

}

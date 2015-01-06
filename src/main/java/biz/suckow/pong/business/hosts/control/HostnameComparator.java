package biz.suckow.pong.business.hosts.control;

import java.util.Comparator;
import java.util.Objects;

import biz.suckow.pong.business.hosts.entity.Host;

public class HostnameComparator implements Comparator<Host> {
    @Override
    public int compare(final Host o1, final Host o2) {
	if (Objects.equals(o1, o2) || this.equalsFromBusinessPerspective(o1, o2)) {
	    return 0;
	}
	return -1;
    }

    private boolean equalsFromBusinessPerspective(final Host o1, final Host o2) {
	boolean result = false;
	if (o1 == null || o2 == null) {
	    return result;
	}
	if (o1.getHostname().equalsIgnoreCase(o2.getHostname())) {
	    result = true;
	}
	return result;
    }

}

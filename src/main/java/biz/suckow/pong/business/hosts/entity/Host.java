package biz.suckow.pong.business.hosts.entity;

import java.util.Objects;

import javax.xml.bind.annotation.XmlRootElement;

import biz.suckow.pong.business.hosts.control.HostnameComparator;

@XmlRootElement
public class Host implements Comparable<Host> {
    private String hostname;
    private String ip;
    private Integer port;
    private String service;
    private String token;

    public String getToken() {
	return this.token;
    }

    public Host setToken(final String token) {
	this.token = token;
	return this;
    }

    public String getHostname() {
	return this.hostname;
    }

    public Host setHostname(final String hostname) {
	this.hostname = hostname;
	return this;
    }

    public String getIp() {
	return this.ip;
    }

    public Host setIp(final String ip) {
	this.ip = ip;
	return this;
    }

    public Integer getPort() {
	return this.port;
    }

    public Host setPort(final Integer port) {
	this.port = port;
	return this;
    }

    public String getService() {
	return this.service;
    }

    public Host setService(final String service) {
	this.service = service;
	return this;
    }

    @Override
    public int hashCode() {
	return Objects.hashCode(this.hostname);
    }

    @Override
    public boolean equals(final Object other) {
	if (!(other instanceof Host)) {
	    return false;
	}
	return Objects.equals(this.getHostname(), ((Host) other).getHostname());
    }

    /**
     * Implemented to support <Set>.contains().
     */
    @Override
    public int compareTo(final Host other) {
	return Objects.compare(this, other, new HostnameComparator());
    }

    @Override
    public String toString() {
	return new StringBuilder(this.getClass().getSimpleName()).append("{").append("hostname=")
		.append(this.getHostname()).append(", ip=").append(this.getIp()).append(", service=")
		.append(this.getService()).append(", port=").append(this.getPort()).append("}").toString();
    }
}

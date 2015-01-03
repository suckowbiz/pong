package biz.suckow.pong.business.hosts.entity;

public class RemoteHost {
    private String hostname;
    private String ip;
    private Integer port;
    private String service;

    public String getHostname() {
	return this.hostname;
    }
    public RemoteHost setHostname(final String hostname) {
	this.hostname = hostname;
	return this;
    }
    public String getIp() {
	return this.ip;
    }
    public RemoteHost setIp(final String ip) {
	this.ip = ip;
	return this;
    }
    public Integer getPort() {
	return this.port;
    }
    public RemoteHost setPort(final Integer port) {
	this.port = port;
	return this;
    }
    public String getService() {
	return this.service;
    }
    public RemoteHost setService(final String service) {
	this.service = service;
	return this;
    }
}

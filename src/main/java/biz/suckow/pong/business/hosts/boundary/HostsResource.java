package biz.suckow.pong.business.hosts.boundary;

import java.util.Optional;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import biz.suckow.pong.business.hosts.control.IPStore;
import biz.suckow.pong.business.hosts.entity.RemoteHost;

@Singleton
@Path("hosts")
public class HostsResource {
    @Inject
    private IPStore store;

    @GET
    public Response index() {
	return Response.ok("add(/{hostname}/{ip}/{service}/{port}) or get (/get/{hostname}).")
		.entity(this.getClass().getSimpleName()).build();
    }

    @POST
    @Path("/add/{hostname}/{ip}/{service}/{port}")
    public Response add(@PathParam("hostname") final String hostname, @PathParam("ip") final String ip,
	    @PathParam("service") final String service, @PathParam("port") final int port) {
	final RemoteHost remoteHost = new RemoteHost().setHostname(hostname).setIp(ip).setPort(port)
		.setService(service);
	this.store.save(remoteHost);
	return Response.ok().build();
    }

    @GET
    @Path("/get/{hostname}")
    public Response get(@PathParam("hostname") final String hostname) {
	Response response = Response.status(Status.NOT_FOUND).build();
	final Optional<RemoteHost> possibleHost = this.store.get(hostname);
	if (possibleHost.isPresent()) {
	    response = Response.ok(possibleHost.get()).build();
	}
	return response;
    }
}

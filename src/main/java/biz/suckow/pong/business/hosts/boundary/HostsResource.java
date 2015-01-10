package biz.suckow.pong.business.hosts.boundary;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import biz.suckow.pong.business.hosts.control.HostStore;
import biz.suckow.pong.business.hosts.entity.Host;

@Singleton
@Path(HostsResource.PATH_BASE)
public class HostsResource {
    public static final String PATH_BASE = "hosts";
    public static final String RELPATH_LIST_ALL = "all";

    @Inject
    private HostStore store;

    @GET
    public Response index() {
	return Response.ok().entity(this.getClass().getSimpleName()).build();
    }

    @POST
    @Path("{hostname}/{ip}/{port}/{service}")
    public Response addHost(@NotNull @Size(min = 3) @PathParam("hostname") final String hostname,
	    @NotNull @Size(min = 7) @PathParam("ip") final String ip,
	    @NotNull @Size(min = 3) @PathParam("service") final String service,
	    @NotNull @DecimalMin(value = "1") @PathParam("port") final int port, @Context final UriInfo info) {
	final Host remoteHost = new Host().setHostname(hostname).setIp(ip).setPort(port).setService(service);
	this.store.add(remoteHost);

	final String path = new StringBuilder("/").append(PATH_BASE).append("/").append(remoteHost.getHostname())
		.toString();
	final URI uri = info.getBaseUriBuilder().path(path).build();
	return Response.created(uri).build();
    }

    @GET
    @Path("{hostname}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHost(@PathParam("hostname") final String hostname)
	    throws URISyntaxException {
	Response response = Response.status(Status.NOT_FOUND).build();
	final Optional<Host> possibleHost = this.store.get(hostname);
	if (possibleHost.isPresent()) {
	    response = Response.ok(possibleHost.get()).build();
	}
	return response;
    }

    @GET
    @Path(RELPATH_LIST_ALL)
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAllHosts() {
	final List<Host> hosts = this.store.getAll();
	return Response.ok(hosts).build();
    }
}

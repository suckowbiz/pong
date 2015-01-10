package biz.suckow.pong.business.config.boundary;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Stateless
@Path("config")
public class ConfigurationResource {
    @Inject
    private ConfigurationService configurationService;

    @Path("reload")
    @POST
    public Response reload() {
	final boolean result = this.configurationService.reload();
	Status status = Status.INTERNAL_SERVER_ERROR;
	if (result) {
	    status = Status.OK;
	}
	return Response.status(status).build();
    }
}

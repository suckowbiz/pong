package biz.suckow.pong.business.hosts.boundary;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.archive.importer.MavenImporter;
import org.testng.annotations.Test;

import biz.suckow.pong.business.hosts.entity.Host;
import biz.suckow.pong.business.security.control.SecurityFilter;

public class HostsResourceIT extends Arquillian {
    private final Client client = ClientBuilder.newClient();
    private static final String BASE = "http://localhost:8080/pong-0.0.1-SNAPSHOT/resources/hosts";

    @Deployment(testable = false)
    @OverProtocol("Servlet 3.0")
    public static WebArchive deploy() {
	return ShrinkWrap.create(MavenImporter.class).loadPomFromFile("pom.xml").importBuildOutput()
		.as(WebArchive.class);
    }

    @Test
    public void verifyEndpointIsAvailable() {
	final Response response = this.client.target(BASE).request().get();
	assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    }

    @Test
    public void verifyAddingAHostReturnsCreated() {
	final Response response = this.client.target(BASE + "/duke/127.0.0.1/22/ssh").request()
		.header(SecurityFilter.TOKEN_HEADER_NAME, "duke-token").post(null);
	assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
	assertThat(response.getLocation().toString()).isEqualTo(BASE + "/duke");
    }

    @Test
    public void verifyRoundtripOfAdditionAndRetrivalWorks() {
	final Response response = this.client.target(BASE + "/dutchess/127.0.0.1/22/ssh").request()
		.header(SecurityFilter.TOKEN_HEADER_NAME, "dutchess-token").post(null);
	assertThat(response.getStatus()).isEqualTo(Status.CREATED.getStatusCode());

	final Host actualHost = this.client.target(BASE + "/dutchess").request(MediaType.APPLICATION_JSON)
		.header(SecurityFilter.TOKEN_HEADER_NAME, "dutchess-token").get(Host.class);
	assertThat(actualHost).isNotNull();
	assertThat(actualHost.getHostname()).isEqualTo("dutchess");
	assertThat(actualHost.getIp()).isEqualTo("127.0.0.1");
	assertThat(actualHost.getService()).isEqualTo("ssh");
    }


    @Test
    public void verifyChangingOtherHostFails() {
	this.client.target(BASE +
		"/duke-one/127.0.0.1/22/ssh").request().header(SecurityFilter.TOKEN_HEADER_NAME,
			"duke-one-token")
			.post(null);
	final Response response = this.client.target(BASE +
		"/duke-one/127.0.0.1/22/ssh").request().header(SecurityFilter.TOKEN_HEADER_NAME,
			"47112")
			.post(null);

	assertThat(response.getStatus()).isEqualTo(Status.FORBIDDEN.getStatusCode());
    }

    @Test
    public void testListAllWorks() {
	this.client.target(BASE + "/duke1/127.0.0.1/22/ssh").request()
	.header(SecurityFilter.TOKEN_HEADER_NAME, "duke1-token").post(null);
	this.client.target(BASE + "/duke2/127.0.0.1/22/ssh").request()
	.header(SecurityFilter.TOKEN_HEADER_NAME, "duke2-token").post(null);

	final List<Host> hosts = this.client.target(BASE + "/" + HostsResource.RELPATH_LIST_ALL).request()
		.header(SecurityFilter.TOKEN_HEADER_NAME, "4711").get(new GenericType<List<Host>>() {
		});
	assertThat(hosts.size()).isGreaterThanOrEqualTo(2);
	assertThat(hosts).contains(new Host().setHostname("duke1"));
	assertThat(hosts).contains(new Host().setHostname("duke2"));
    }

    @Test
    public void testListAllUnauthorized() {
	final Response response = this.client.target(BASE + "/" + HostsResource.RELPATH_LIST_ALL).request().get();
	assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
    }

    @Test
    public void testListAllForbidden() {
	final Response response = this.client.target(BASE + "/" + HostsResource.RELPATH_LIST_ALL).request()
		.header(SecurityFilter.TOKEN_HEADER_NAME, "wrong").get();
	assertThat(response.getStatus()).isEqualTo(Response.Status.FORBIDDEN.getStatusCode());
    }
}

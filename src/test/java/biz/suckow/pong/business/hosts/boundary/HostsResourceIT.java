package biz.suckow.pong.business.hosts.boundary;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.archive.importer.MavenImporter;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HostsResourceIT extends Arquillian {

    @Deployment(testable=false)
    @OverProtocol("Servlet 3.0")
    public static WebArchive deploy() {
	return ShrinkWrap.create(MavenImporter.class).loadPomFromFile("pom.xml").importBuildOutput()
		.as(WebArchive.class);
    }

    @Test
    public void verifyRESTAPIAvailable() {
	final Client client = ClientBuilder.newClient();
	final WebTarget target = client.target("http://localhost:8080/pong-0.0.1-SNAPSHOT/resources/hosts/");

	final Response response = target.request().get();
	Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
    }

    @Test
    public void verifyAddedRemoteHostCanBeListed() {
	final Client client = ClientBuilder.newClient();
	WebTarget target = client.target("http://localhost:8080/pong-0.0.1-SNAPSHOT/resources/hosts/add/duke/127.0.0.1/ssh/22");
	Response response = target.request().post(null);
	Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());

	target = client.target("http://localhost:8080/pong-0.0.1-SNAPSHOT/resources/hosts/get/duke");
	response = target.request().get();
	Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
	System.out.println(".----------------------------------->"+response.getEntity());
    }
}

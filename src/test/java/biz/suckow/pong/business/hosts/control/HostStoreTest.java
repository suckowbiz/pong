package biz.suckow.pong.business.hosts.control;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.logging.Logger;

import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import biz.suckow.pong.business.hosts.entity.Host;

public class HostStoreTest extends EasyMockSupport {
    @Mock
    private Logger logger;

    @TestSubject
    HostStore cut = new HostStore();

    @BeforeClass
    private void injectMocks() {
	injectMocks(this);
    }

    @Test
    public void verifyAdditionStoresCorrectly() {
	final Host expectedHost = new Host().setHostname("duke").setIp("127.0.0.1").setPort(22).setService("ssh");
	this.cut.add(expectedHost);

	final Optional<Host> actualHost = this.cut.get(expectedHost.getHostname());
	assertThat(actualHost.isPresent());
	assertThat(actualHost.get().getHostname()).isEqualTo(expectedHost.getHostname());
	assertThat(actualHost.get().getIp()).isEqualTo(expectedHost.getIp());
	assertThat(actualHost.get().getPort()).isEqualTo(expectedHost.getPort());
	assertThat(actualHost.get().getService()).isEqualTo(expectedHost.getService());
    }

    @Test
    public void verifyAdditionRefreshesCorrectly() {
	final String refreshedHostname = "dutchess";

	final Host expectedHost = new Host().setHostname("duke");
	this.cut.add(expectedHost);

	expectedHost.setHostname(refreshedHostname);
	this.cut.add(expectedHost);

	final Optional<Host> actualHost = this.cut.get(expectedHost.getHostname());
	assertThat(actualHost.isPresent());
	assertThat(actualHost.get().getHostname()).isEqualTo(refreshedHostname);
    }

    @Test
    public void verifyCannotGetNotExistingItem() {
	final Optional<Host> actualHost = this.cut.get("noname");
	assertThat(actualHost.isPresent()).isFalse();
    }
}

package biz.suckow.pong;

import java.util.logging.Logger;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

public class LoggerFactory {
    @Produces
    public Logger produceLogger(final InjectionPoint ip) {
	return Logger.getLogger(ip.getMember().getDeclaringClass().getName());
    }
}

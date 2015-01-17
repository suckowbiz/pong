package biz.suckow.pong;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

@Startup
@Singleton
public class Configuration {
    @Inject
    private Logger logger;

    public static final String NAME_ENV_CONFIG_PATH = "PONG_CONFIG_PATH";
    private final Properties properties = new Properties();

    @PostConstruct
    public void fetchProperties() {
	final Path path = this.getConfigPath();
	if (Files.notExists(path)) {
	    throw new IllegalArgumentException("File at '" + path.toString() + "' must not be missing.");
	} else if (Files.isReadable(path) == false) {
	    throw new IllegalArgumentException("File at '" + path.toString() + "' must not be read protected.");
	}
	try {
	    this.properties.load(Files.newInputStream(path, StandardOpenOption.READ));
	} catch (final IOException e) {
	    throw new IllegalStateException("Error loading properties from "+path.toString()+". "+e.getMessage());
	}
    }

    @Produces
    public String getString(final InjectionPoint ip) {
	final String key = ip.getMember().getName();
	final String result = this.properties.getProperty(key);
	if (result == null) {
	    this.logger.log(Level.SEVERE, "No such property: {0}.", key);
	}
	return result;
    }

    private Path getConfigPath() {
	final String result = System.getProperty(NAME_ENV_CONFIG_PATH);
	if (result == null) {
	    throw new IllegalStateException("Please define property '" + NAME_ENV_CONFIG_PATH
		    + "' in the system environment.");
	}
	return Paths.get(result);
    }
}

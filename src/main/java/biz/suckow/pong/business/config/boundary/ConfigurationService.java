package biz.suckow.pong.business.config.boundary;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Singleton;
import javax.inject.Inject;

import org.yaml.snakeyaml.Yaml;

import biz.suckow.pong.business.config.entity.Configuration;

@Singleton
public class ConfigurationService {
    @Inject
    private Logger logger;
    private static final String NAME_ENV_CONFIG_PATH = "PONG_CONFIG_PATH";
    private AtomicReference<Configuration> config;

    public boolean reload() {
	boolean result = true;
	try {
	    this.load();
	} catch (final Exception e) {
	    this.logger.log(Level.SEVERE, e.getMessage());
	    result = false;
	}
	return result;
    }

    private void load() throws IOException {
	final Path configPath = this.getConfigPath();
	if (Files.notExists(configPath)) {
	    throw new IllegalArgumentException("File at '"+configPath.toString()+"' must not be missing.");
	} else if (Files.isReadable(configPath) == false) {
	    throw new IllegalArgumentException("File at '"+configPath.toString()+"' must not be read protected.");
	}

	final Yaml yaml = new Yaml();
	try (final InputStream inputStream = Files.newInputStream(configPath)) {
	    final Configuration config = yaml.loadAs(inputStream, Configuration.class);
	    this.config.set(config);
	};
    }

    private Path getConfigPath() {
	final String result = System.getenv(NAME_ENV_CONFIG_PATH);
	if (result == null) {
	    throw new IllegalStateException("Please define variable '"+ NAME_ENV_CONFIG_PATH + "' in the system environmen.");
	}
	return Paths.get(result);
    }

}

package edu.weeia.cynodesu;

import edu.weeia.cynodesu.configuration.AppProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

@SpringBootApplication
@Slf4j
@EnableCaching
@EnableConfigurationProperties(AppProperties.class)
public class CynoDesuApplication {
	public static void main(String[] args) throws UnknownHostException {
		ApplicationContext ctx = SpringApplication.run(CynoDesuApplication.class, args);
		Environment env = ctx.getEnvironment();

		String banner = String.format("""
				Access URLs:
				                ----------------------------------------------------------
				                \tLocal: \t\t\thttp://localhost:%s
				                \tExternal: \t\thttp://%s:%s
				                \tEnvironment: \t%s
				                ----------------------------------------------------------""",

				            env.getProperty("server.port"),
				            InetAddress.getLocalHost().getHostAddress(),
				            env.getProperty("server.port"),
				            Arrays.toString(env.getActiveProfiles()));
		log.info(banner);
	}

}

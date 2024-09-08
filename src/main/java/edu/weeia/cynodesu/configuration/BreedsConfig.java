package edu.weeia.cynodesu.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.HashSet;
import java.util.Set;


@Configuration
@ConfigurationProperties(prefix = "breeds")
public class BreedsConfig {
    private Set<String> breeds;

    public Set<String> getBreeds() {
        return breeds;
    }

    public void setBreeds(Set<String> breeds) {
        this.breeds = breeds;
    }
}

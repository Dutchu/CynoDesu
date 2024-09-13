package edu.weeia.cynodesu.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "cert")
public class CertConfig {

    String title;
    String subtitle;
    String footer;
    String registrar;
    String backgroundImagePath;
    String organizationIconPath;
}

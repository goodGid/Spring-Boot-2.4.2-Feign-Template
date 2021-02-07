package dev.be.goodgid.common.property;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Profiles;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class YamlEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final String[] propertyUris = { "classpath*:config/custom/*.yml" };
    private static final String[] acceptsProfiles = { "local", "beta", "real" };

    private final YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
    private final ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        try {
            // Set Default Profile
            boolean isSomeProfileActive = environment.acceptsProfiles(Profiles.of(acceptsProfiles));

            if (!isSomeProfileActive) {
                environment.setActiveProfiles("local");
                Resource path = new ClassPathResource("config/application.yml");
                if (path.exists()) {
                    try {
                        environment.getPropertySources().addLast(
                                new PropertiesPropertySourceLoader().load("application", path).get(0));
                    } catch (IOException e) {
                        throw new IllegalStateException(e);
                    }
                }
            } else {
                log.info("{} profile is active: " + environment.getActiveProfiles());
            }

            // Add Custom *.yml
            List<Resource> resourceList = new ArrayList<>();
            for (String propertyUri : propertyUris) {
                resourceList.addAll(List.of(resourcePatternResolver.getResources(propertyUri)));
            }

            resourceList.stream().map(this::loadYaml).forEach(them -> {
                if (them != null && them.size() > 0) {
                    for (PropertySource<?> it : them) {
                        environment.getPropertySources().addLast(it);
                    }
                }
            });
        } catch (Exception e) {
            throw new BeanCreationException(e.getMessage(), e);
        }
    }

    private List<PropertySource<?>> loadYaml(Resource resource) {
        if (!resource.exists()) {
            throw new IllegalArgumentException("Resource " + resource + " does not exist");
        }
        try {
            return loader.load(resource.getURL().toString(), resource);
        } catch (IOException ex) {
            throw new IllegalStateException(
                    "Failed to load yaml configuration from " + resource, ex);
        }
    }
}

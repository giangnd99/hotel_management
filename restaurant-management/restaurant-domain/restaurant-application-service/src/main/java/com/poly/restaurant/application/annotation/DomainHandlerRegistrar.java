package com.poly.restaurant.application.annotation;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Map;
import java.util.Set;

public class DomainHandlerRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                        BeanDefinitionRegistry registry) {
        Map<String, Object> attrs = importingClassMetadata
                .getAnnotationAttributes(EnableDomainHandlers.class.getName());

        String[] basePackages = (String[]) attrs.get("value");

        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false); // disable default filters

        scanner.addIncludeFilter(new AnnotationTypeFilter(DomainHandler.class)); // Đây chính là ComponentScan tuỳ biến

        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidates = scanner.findCandidateComponents(basePackage);
            for (BeanDefinition candidate : candidates) {
                registry.registerBeanDefinition(
                        ((ScannedGenericBeanDefinition) candidate).getBeanClassName(),
                        candidate
                );
            }
        }
    }
}


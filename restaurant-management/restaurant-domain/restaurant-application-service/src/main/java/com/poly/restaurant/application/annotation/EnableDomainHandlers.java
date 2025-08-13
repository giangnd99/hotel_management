package com.poly.restaurant.application.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(DomainHandlerRegistrar.class)
public @interface EnableDomainHandlers {
    String[] value(); // packages to scan
}


package com.poly.service.handler;

public abstract class BaseHandler<DOMAIN, REPOSITORY> {

    protected final DOMAIN domainService;

    protected final REPOSITORY repository;

    public BaseHandler(DOMAIN domainService, REPOSITORY repository) {
        this.domainService = domainService;
        this.repository = repository;
    }

}

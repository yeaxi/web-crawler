module client {
    requires domain_common;
    requires lombok;
    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires spring.context;
    requires spring.cloud.commons;
    requires spring.integration.core;
    requires spring.cloud.stream;
    requires spring.messaging;
    requires spring.web;
    requires reactor.core;
    requires spring.data.mongodb;
    requires spring.data.commons;
    requires jackson.annotations;
}
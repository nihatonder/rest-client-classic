package com.nonder.rest_app.config; // Or any appropriate package

import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;

import java.util.List;

/**
 * A Spring FactoryBean for creating a RestClient instance using a fluent builder,
 * which is difficult to configure directly in XML.
 */
public class RestClientFactoryBean implements FactoryBean<RestClient> {

    private List<ClientHttpRequestInterceptor> interceptors;

    // This is the setter Spring XML will use for the 'interceptors' property
    public void setInterceptors(List<ClientHttpRequestInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

    @Override
    public RestClient getObject() throws Exception {
        RestClient.Builder builder = RestClient.builder();

        // Programmatically add the interceptors to the builder
        if (this.interceptors != null && !this.interceptors.isEmpty()) {
            builder.requestInterceptors(list -> list.addAll(this.interceptors));
        }

        return builder.build();
    }

    @Override
    public Class<?> getObjectType() {
        return RestClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
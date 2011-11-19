package com.microsoft.windowsazure.utils.jersey;

import java.util.Map;
import java.util.Map.Entry;

import com.microsoft.windowsazure.common.Builder;
import com.microsoft.windowsazure.common.Builder.Registry;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class Exports implements Builder.Exports {

    public void register(Registry registry) {
        registry.add(new Builder.Factory<ClientConfig>() {
            public ClientConfig create(String profile, Builder builder, Map<String, Object> properties) {
                ClientConfig clientConfig = new DefaultClientConfig();
                for (Entry<String, Object> entry : properties.entrySet()) {
                    clientConfig.getProperties().put(entry.getKey(), entry.getValue());
                }
                return clientConfig;
            }
        });

        registry.add(new Builder.Factory<Client>() {
            public Client create(String profile, Builder builder, Map<String, Object> properties) {
                ClientConfig clientConfig = (ClientConfig) properties.get("ClientConfig");
                Client client = Client.create(clientConfig);
                return client;
            }
        });

        registry.add(new Builder.Factory<HttpURLConnectionClient>() {
            public HttpURLConnectionClient create(String profile, Builder builder, Map<String, Object> properties) {
                ClientConfig clientConfig = (ClientConfig) properties.get("ClientConfig");
                HttpURLConnectionClient client = HttpURLConnectionClient.create(clientConfig);
                //client.addFilter(new LoggingFilter());
                return client;
            }
        });
    }
}
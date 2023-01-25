package ru.programstore.prostore.eureka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import ru.programstore.prostore.core.impl.JsonQuery;
import ru.programstore.prostore.core.Query;
import ru.programstore.prostore.core.QueryBus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class EurekaQueryBus implements QueryBus {
    private final EurekaClient eurekaClient;
    private final HttpClient client;
    private final ObjectMapper objectMapper;
    private final AtomicInteger roundRobinCounter = new AtomicInteger();

    public EurekaQueryBus(EurekaClient eurekaClient, HttpClient client, ObjectMapper objectMapper) {
        this.eurekaClient = eurekaClient;
        this.client = client;
        this.objectMapper = objectMapper;
    }

    @Override
    public Object send(String serviceName, Query query) {
        Application application = eurekaClient.getApplication(serviceName);
        if (application == null) {
            throw new RuntimeException("No instances of " + serviceName);
        }
        List<InstanceInfo> instances = application.getInstances();
        if (instances == null || instances.isEmpty()) {
            throw new RuntimeException("No instances of " + serviceName);
        }
        if (roundRobinCounter.get() > instances.size() - 1) {
            roundRobinCounter.set(0);
        }
        InstanceInfo instanceInfo = instances.get(roundRobinCounter.getAndIncrement());
        String hostName = instanceInfo.getHostName();
        int port = instanceInfo.getPort();
        String url = String.format("http://%s:%s/prostore/query", hostName, port);
        HttpPost request = new HttpPost(String.format("http://%s:%s/prostore/query", hostName, port));
        request.setEntity(asEntity(query));
        request.setHeader("Content-Type", "application/json");
        final HttpResponse response;
        final String responseBody;
        try {
            response = client.execute(request);
            responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() % 200 != 0) {
            throw new RuntimeException(
                String.format("got unexpected status code %s:%s", statusLine.getStatusCode(), statusLine.getReasonPhrase())
            );
        }
        return responseBody;
    }

    private StringEntity asEntity(Query query) {
        JsonQuery jsonQuery = new JsonQuery(query.getClass().getName(), asJsonString(query));
        return new StringEntity(
            asJsonString(jsonQuery), StandardCharsets.UTF_8
        );
    }

    private String asJsonString(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

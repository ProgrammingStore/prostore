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
import ru.programstore.prostore.core.impl.JsonCommand;
import ru.programstore.prostore.core.Command;
import ru.programstore.prostore.core.CommandBus;
import ru.programstore.prostore.core.impl.CommandResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class EurekaCommandBus implements CommandBus {
    private final EurekaClient eurekaClient;
    private final HttpClient client;
    private final ObjectMapper objectMapper;
    private final AtomicInteger roundRobinCounter = new AtomicInteger();

    public EurekaCommandBus(EurekaClient eurekaClient, HttpClient client, ObjectMapper objectMapper) {
        this.eurekaClient = eurekaClient;
        this.client = client;
        this.objectMapper = objectMapper;
    }

    @Override
    public CommandResponse send(String serviceName, Command command) {
        Application application = eurekaClient.getApplication(serviceName);
        if (application == null) {
            return new CommandResponse(false, "No instances of " + serviceName);
        }
        List<InstanceInfo> instances = application.getInstances();
        if (instances == null || instances.isEmpty()) {
            return new CommandResponse(false, "No instances of " + serviceName);
        }
        if (roundRobinCounter.get() > instances.size() - 1) {
            roundRobinCounter.set(0);
        }
        InstanceInfo instanceInfo = instances.get(roundRobinCounter.getAndIncrement());
        String hostName = instanceInfo.getHostName();
        int port = instanceInfo.getPort();
        String url = String.format("http://%s:%s/prostore/command", hostName, port);
        HttpPost request = new HttpPost(String.format("http://%s:%s/prostore/command", hostName, port));
        request.setEntity(asEntity(command));
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
            return new CommandResponse(
                false, String.format("got unexpected status code %s:%s", statusLine.getStatusCode(), statusLine.getReasonPhrase())
            );
        }
        return new CommandResponse(true,responseBody);
    }

    private StringEntity asEntity(Command command) {
        JsonCommand jsonCommand = new JsonCommand(command.getClass().getName(), asJsonString(command));
        return new StringEntity(
            asJsonString(jsonCommand), StandardCharsets.UTF_8
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

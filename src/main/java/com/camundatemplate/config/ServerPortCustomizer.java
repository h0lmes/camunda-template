package com.camundatemplate.config;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.Socket;

@Component
public class ServerPortCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    private static final int MAX_PORT_NUMBER = 8085;

    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        int port = 8080;
        while (portInUse(port) && port < MAX_PORT_NUMBER) port++;
        factory.setPort(port);
    }

    private boolean portInUse(int port) {
        try {
            new Socket("localhost", port).close();
            return true;
        }
        catch(IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}

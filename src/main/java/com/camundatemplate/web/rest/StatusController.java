package com.camundatemplate.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;

@RestController
@RequestMapping("api/status")
public class StatusController {

    private final Logger log = LoggerFactory.getLogger(StatusController.class);

    private static final String SERVER_NAME = InetAddress.getLoopbackAddress().getCanonicalHostName();

    @GetMapping
    public Status getStatus() {
        return new Status("OK", SERVER_NAME, getUpTime());
    }

    private String getUpTime() {
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
        long millis = uptime % 1000;
        long seconds = (uptime / 1000) % 60;
        long minutes = (uptime / (1000 * 60)) % 60;
        long hours = (uptime / (1000 * 60 * 60)) % 24;
        long days = uptime / (1000 * 60 * 60 * 24);
        return String.format("%d.%02d:%02d:%02d:%03d", days, hours, minutes, seconds, millis);
    }

    public static class Status {
        String applicationState;
        String serverName;
        String uptime;

        Status(String applicationState, String serverName, String uptime) {
            this.applicationState = applicationState;
            this.serverName = serverName;
            this.uptime = uptime;
        }

        public String getApplicationState() {
            return applicationState;
        }

        public String getServerName() {
            return serverName;
        }

        public String getUptime() {
            return uptime;
        }
    }

}
package com.camundatemplate.config;

import com.camundatemplate.process.RegistrationProcess;
import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
public class CamundaConfig {

    private RegistrationProcess registrationProcess;

    public CamundaConfig(RegistrationProcess registrationProcess) {
        this.registrationProcess = registrationProcess;
    }

    @EventListener
    public void processPostDeploy(PostDeployEvent event) {
        //registrationProcess.startRandom();
    }
}

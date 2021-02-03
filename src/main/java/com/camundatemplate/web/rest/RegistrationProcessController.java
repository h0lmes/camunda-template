package com.camundatemplate.web.rest;

import com.camundatemplate.process.RegistrationProcess;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/registration")
public class RegistrationProcessController {

    private RegistrationProcess registrationProcess;

    public RegistrationProcessController(RegistrationProcess registrationProcess) {
        this.registrationProcess = registrationProcess;
    }

    @GetMapping("start")
    public String start() {
        return registrationProcess.startRandom();
    }

    @GetMapping("proceed")
    public void proceed() {
        registrationProcess.completeAwaitTasks();
    }
}

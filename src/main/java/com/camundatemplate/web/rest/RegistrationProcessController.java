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
    public void start() {
        registrationProcess.startRandom();
    }

    @GetMapping("confirm")
    public void confirm() {
        registrationProcess.confirmAccount();
    }

    @GetMapping("fail")
    public void fail() {
        registrationProcess.failAccount();
    }
}

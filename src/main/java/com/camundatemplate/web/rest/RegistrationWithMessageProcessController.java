package com.camundatemplate.web.rest;

import com.camundatemplate.process.RegistrationWithMessageProcess;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/registrationwithmessage")
public class RegistrationWithMessageProcessController {

    private RegistrationWithMessageProcess process;

    public RegistrationWithMessageProcessController(RegistrationWithMessageProcess process) {
        this.process = process;
    }

    @GetMapping("start/{id}")
    public void start(@PathVariable("id") String id) {
        process.start(id);
    }

    @GetMapping("confirm/{id}")
    public void confirm(@PathVariable("id") String id) {
        process.confirmAccount(id);
    }

    @GetMapping("fail/{id}")
    public void fail(@PathVariable("id") String id) {
        process.failAccount(id);
    }
}

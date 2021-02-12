package com.camundatemplate.web.rest;

import com.camundatemplate.process.RegistrationProcess;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/registration")
public class RegistrationProcessController {

    private RegistrationProcess process;

    public RegistrationProcessController(RegistrationProcess registrationProcess) {
        this.process = registrationProcess;
    }

    @GetMapping("start/{id}")
    public void start(@PathVariable("id") String id) {
        process.start(id);
    }

    @GetMapping("confirm/{id}")
    public void confirm(@PathVariable("id") String id) {
        process.confirmAccount(id);
    }

    @GetMapping("confirmmsg/{id}")
    public void confirmMessage(@PathVariable("id") String id) {
        process.confirmAccountMessage(id);
    }

    @GetMapping("fail/{id}")
    public void fail(@PathVariable("id") String id) {
        process.failAccount(id);
    }
}

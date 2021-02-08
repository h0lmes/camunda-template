package com.camundatemplate.process;

import com.camundatemplate.util.Util;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TaskSetValues implements JavaDelegate {

    private final Logger log = LoggerFactory.getLogger(TaskSetValues.class);

    public void execute(DelegateExecution delegate) throws Exception {
        delegate.setVariable(RegistrationProcess.CUSTOM_ATTRIBUTE, "value1");
        log.info("******* TaskSetValues invoked " + Util.variablesToString(delegate.getVariables()));
    }
}

package com.camundatemplate.process;

import com.camundatemplate.util.Util;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class WriteTask implements JavaDelegate {

    private final Logger log = LoggerFactory.getLogger(WriteTask.class);

    public void execute(DelegateExecution delegate) throws Exception {
        log.info("WriteTask invoked " + Util.variablesToString(delegate.getVariables()));

        delegate.setVariable(RegistrationProcess.CUSTOM_ATTRIBUTE1, "value1");
        delegate.setVariable(RegistrationProcess.CUSTOM_ATTRIBUTE2, "value2");
    }
}

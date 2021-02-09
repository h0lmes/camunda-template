package com.camundatemplate.process;

import com.camundatemplate.process.model.RegistrationProcessData;
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
        log.info("******* TaskSetValues invoked " + Util.variablesToString(delegate.getVariables()));

        // example of working with untyped process data
        delegate.setVariable(RegistrationProcess.CUSTOM_ATTRIBUTE, "value1");

        // example of working with typed process data
        RegistrationProcessData data = RegistrationProcess.getData(delegate);
        data.setAccountNumber("new account number");
        RegistrationProcess.setData(delegate, data);
    }
}

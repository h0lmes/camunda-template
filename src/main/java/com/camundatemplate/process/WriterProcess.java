package com.camundatemplate.process;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service
public class WriterProcess implements JavaDelegate {

    public static final String ATTRIBUTE1_NAME = "attribute1";
    public static final String ATTRIBUTE2_NAME = "attribute2";

    public void execute(DelegateExecution delegate) throws Exception {
        System.out.println("-----------------------------------------");
        System.out.println("WriterProcess invoked.");
        delegate.setVariable(ATTRIBUTE1_NAME, "value1");
        delegate.setVariable(ATTRIBUTE2_NAME, "value2");
        System.out.println("Attributes written.");
        System.out.println("-----------------------------------------");
    }
}

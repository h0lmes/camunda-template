package com.camundatemplate.process;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service
public class ReaderProcess implements JavaDelegate {

    public void execute(DelegateExecution delegate) throws Exception {
        System.out.println("-----------------------------------------");
        System.out.println("ReaderProcess invoked.");
        System.out.println("Process attributes are:");
        System.out.println("\t" + WriterProcess.ATTRIBUTE1_NAME + " = " + delegate.getVariable(WriterProcess.ATTRIBUTE1_NAME));
        System.out.println("\t" + WriterProcess.ATTRIBUTE2_NAME + " = " + delegate.getVariable(WriterProcess.ATTRIBUTE2_NAME));
        System.out.println("-----------------------------------------");
    }
}

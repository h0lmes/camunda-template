package com.camundatemplate.process;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.impl.history.event.HistoryEvent;
import org.camunda.bpm.spring.boot.starter.event.ExecutionEvent;
import org.camunda.bpm.spring.boot.starter.event.TaskEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CamundaEventListener {

    //
    // uncomment according to your needs
    //

//    @EventListener
//    public void onTaskEvent(DelegateTask taskDelegate) {
//        System.out.println("Mutable task event: " + taskDelegate.getId());
//    }

//    @EventListener
//    public void onTaskEvent(TaskEvent taskEvent) {
//        System.out.println("Immutable task event: " + taskEvent.getName());
//    }

//    @EventListener
//    public void onExecutionEvent(DelegateExecution executionDelegate) {
//        System.out.println("Mutable execution event: " + executionDelegate.toString());
//    }

    @EventListener
    public void onExecutionEvent(ExecutionEvent executionEvent) {
        if (executionEvent.getActivityInstanceId() != null
                && executionEvent.getProcessInstanceId() != null
                && executionEvent.getActivityInstanceId().equals(executionEvent.getProcessInstanceId())
                && "end".equals(executionEvent.getEventName())) {
            System.out.println("******* A process has finished: " + executionEvent.getProcessDefinitionId());
        }
    }

//    @EventListener
//    public void onHistoryEvent(HistoryEvent historyEvent) {
//        System.out.println("History event: [" + historyEvent.getEventType() + "] " + historyEvent.toString());
//    }
}

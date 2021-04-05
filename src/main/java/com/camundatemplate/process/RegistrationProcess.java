package com.camundatemplate.process;

import com.camundatemplate.process.model.RegistrationProcessData;
import com.camundatemplate.util.Util;
import org.camunda.bpm.engine.ExternalTaskService;
import org.camunda.bpm.engine.MismatchingMessageCorrelationException;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.VariableScope;
import org.camunda.bpm.engine.externaltask.LockedExternalTask;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * This component is intended to control a particular process with @Id = PROCESS_KEY.<p>
 * Process should be deployed beforehand (Camunda does this automatically for all *.bpmn in `src/main/resources`).
 */
@Component
public class RegistrationProcess {

    private static final String PROCESS_KEY = "processRegistration";
    private static final String MESSAGE_CONFIRM_ACCOUNT = "MESSAGE_CONFIRM_ACCOUNT";
    private static final String TASK_CONFIRM_ACCOUNT_TOPIC = "taskConfirmAccount";
    private static final String TASK_CONFIRM_ACCOUNT_WORKER = "taskConfirmAccountWorker";
    private static final long LOCK_INTERVAL = 10000L;
    private static final int FETCH_LOCK_SIZE = 10;
    private static final String PROCESS_DATA_ATTRIBUTE = "processData";
    static final String CUSTOM_ATTRIBUTE = "attr";
    // BPMN diagram should contain error definition with the following error code
    private static final String ERR_CONFIRM_ACCOUNT = "ERR_CONFIRM_ACCOUNT";

    private final Logger log = LoggerFactory.getLogger(RegistrationProcess.class);
    private RuntimeService runtimeService;
    private ExternalTaskService externalTaskService;

    public RegistrationProcess(RuntimeService runtimeService, ExternalTaskService externalTaskService) {
        this.runtimeService = runtimeService;
        this.externalTaskService = externalTaskService;
    }

    /**
     * A method to start an instance of the process.
     * @param externalId some startup variable for demo purposes.
     * @return process instance Id
     */
    public String start(String externalId) {
        log.info("\n\n\n");
        log.info("******* start process " + PROCESS_KEY);
        return runtimeService
                .startProcessInstanceByKey(PROCESS_KEY, externalId, makeStartupVariables())
                .getId();
    }

    private Map<String, Object> makeStartupVariables() {
        return Variables.createVariables()
                .putValue(PROCESS_DATA_ATTRIBUTE, new RegistrationProcessData("number", "holder"));
    }

    public void confirmAccountMessage(String externalId) {
        log.info("******* confirm account with message for BusinessKey = " + externalId);
        correlateMessage(MESSAGE_CONFIRM_ACCOUNT, externalId);
    }

    private void correlateMessage(String message, String businessKey) {
        try {
            runtimeService.createMessageCorrelation(message)
                    .processInstanceBusinessKey(businessKey)
                    .correlateExclusively();
        } catch (MismatchingMessageCorrelationException e) {
            log.error(e.getMessage());
        }
    }

    public void confirmAccount(String externalId) {
        confirmOrFailAccount(externalId, true);
    }

    public void failAccount(String externalId) {
        confirmOrFailAccount(externalId, false);
    }

    /**
     * This method handles External Service Tasks for a specific business key and topic
     */
    private void confirmOrFailAccount(String externalId, boolean success) {
        List<LockedExternalTask> tasks = externalTaskService
                .fetchAndLock(FETCH_LOCK_SIZE, TASK_CONFIRM_ACCOUNT_WORKER)
                .topic(TASK_CONFIRM_ACCOUNT_TOPIC, LOCK_INTERVAL)
                .businessKey(externalId)
                .enableCustomObjectDeserialization()
                .execute();

        for (LockedExternalTask task : tasks) {
            RegistrationProcessData data = getData(task);
            if (success) {
                log.info("******* confirm account for " + Util.variablesToString(task.getVariables()));
                externalTaskService.complete(task.getId(), TASK_CONFIRM_ACCOUNT_WORKER);
            } else {
                log.info("******* fail account for " + Util.variablesToString(task.getVariables()));
                externalTaskService.handleBpmnError(task.getId(), TASK_CONFIRM_ACCOUNT_WORKER, ERR_CONFIRM_ACCOUNT);
            }
        }
    }

    /**
     * Static method for type-safe process data retrieval.
     * @param lockedExternalTask
     * @return typed data
     */
    public static RegistrationProcessData getData(LockedExternalTask lockedExternalTask) {
        return (RegistrationProcessData) lockedExternalTask
                .getVariables()
                .<ObjectValue>getValueTyped(RegistrationProcess.PROCESS_DATA_ATTRIBUTE)
                .getValue();
    }

    /**
     * Static method for type-safe process data retrieval.
     * @param delegate any delegate execution
     * @return typed data
     */
    public static RegistrationProcessData getData(VariableScope delegate) {
        return (RegistrationProcessData) delegate
                .<ObjectValue>getVariableTyped(RegistrationProcess.PROCESS_DATA_ATTRIBUTE)
                .getValue();
    }

    /**
     * Static method for type-safe process data storing.
     * @param delegate any delegate execution
     * @param data typed data to store in the process scope
     */
    public static void setData(VariableScope delegate, RegistrationProcessData data) {
        delegate.setVariable(RegistrationProcess.PROCESS_DATA_ATTRIBUTE, data);
    }
}

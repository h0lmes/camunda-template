package com.camundatemplate.process;

import com.camundatemplate.process.model.RegistrationProcessData;
import org.camunda.bpm.engine.MismatchingMessageCorrelationException;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.VariableScope;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * This component is intended to control a particular process with @Id = PROCESS_KEY.<p>
 * Process should be deployed beforehand (Camunda does this automatically for all *.bpmn in `src/main/resources`).
 */
@Component
public class RegistrationWithMessageProcess {

    private final Logger log = LoggerFactory.getLogger(RegistrationWithMessageProcess.class);

    private static final String PROCESS_KEY = "processRegistrationWithMessage";

    private static final String MESSAGE_CONFIRM_ACCOUNT = "MESSAGE_CONFIRM_ACCOUNT";
    private static final String MESSAGE_FAIL_ACCOUNT = "MESSAGE_FAIL_ACCOUNT";

    private static final String PROCESS_DATA_ATTRIBUTE = "processData";
    static final String CUSTOM_ATTRIBUTE = "attr";

    private RuntimeService runtimeService;

    public RegistrationWithMessageProcess(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    /**
     * A method to start an instance of the process.
     *
     * @param externalId some startup variable for demo purposes.
     * @return process instance Id
     */
    public void start(String externalId) {
        log.info("\n\n\n");
        log.info("******* start process " + PROCESS_KEY);
        runtimeService.startProcessInstanceByKey(PROCESS_KEY, externalId, makeStartupVariables());
    }

    private Map<String, Object> makeStartupVariables() {
        return Variables.createVariables()
                .putValue(PROCESS_DATA_ATTRIBUTE, new RegistrationProcessData("number", "holder"));
    }

    public void confirmAccount(String externalId) {
        correlateMessage(MESSAGE_CONFIRM_ACCOUNT, externalId);
    }

    public void failAccount(String externalId) {
        correlateMessage(MESSAGE_FAIL_ACCOUNT, externalId);
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

    /**
     * Static method for type-safe process data retrieval.
     *
     * @param delegate any delegate execution
     * @return typed data
     */
    public static RegistrationProcessData getData(VariableScope delegate) {
        return (RegistrationProcessData) delegate
                .<ObjectValue>getVariableTyped(RegistrationWithMessageProcess.PROCESS_DATA_ATTRIBUTE)
                .getValue();
    }

    /**
     * Static method for type-safe process data storing.
     *
     * @param delegate any delegate execution
     * @param data     typed data to store in the process scope
     */
    public static void setData(VariableScope delegate, RegistrationProcessData data) {
        delegate.setVariable(RegistrationWithMessageProcess.PROCESS_DATA_ATTRIBUTE, data);
    }
}

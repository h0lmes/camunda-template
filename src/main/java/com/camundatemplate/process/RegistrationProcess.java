package com.camundatemplate.process;

import com.camundatemplate.util.Util;
import org.camunda.bpm.engine.ExternalTaskService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.externaltask.LockedExternalTask;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This component is intended to control a particular process with @Id = PROCESS_KEY.<p>
 * Process should be deployed beforehand (Camunda does this automatically for all *.bpmn in `src/main/resources`).
 */
@Component
public class RegistrationProcess {

    private final Logger log = LoggerFactory.getLogger(RegistrationProcess.class);

    private static final String PROCESS_KEY = "processRegistration";

    private static final String TASK_CONFIRM_ACCOUNT_TOPIC = "taskConfirmAccount";
    private static final String TASK_CONFIRM_ACCOUNT_WORKER = "taskConfirmAccountWorker";
    private static final long LOCK_INTERVAL = 10000L;
    private static final int FETCH_LOCK_SIZE = 10;

    static final String EXTERNAL_ID_ATTRIBUTE = "externalId";
    static final String CUSTOM_ATTRIBUTE = "attr";

    // BPMN diagram should contain error definition with the following error code
    private static final String ERR_CONFIRM_ACCOUNT = "ERR_CONFIRM_ACCOUNT";

    private RuntimeService runtimeService;
    private ExternalTaskService externalTaskService;

    public RegistrationProcess(RuntimeService runtimeService, ExternalTaskService externalTaskService) {
        this.runtimeService = runtimeService;
        this.externalTaskService = externalTaskService;
    }

    public String startRandom() {
        return start(UUID.randomUUID().toString());
    }

    public String start(String externalId) {
        log.info("******* start process");
        Map<String, Object> params = new HashMap<>();
        params.put(RegistrationProcess.EXTERNAL_ID_ATTRIBUTE, externalId);
        ProcessInstance process = runtimeService.startProcessInstanceByKey(PROCESS_KEY, params);
        return process.getId();
    }

    public void confirmAccount() {
        confirmOrFailAccount(true);
    }

    public void failAccount() {
        confirmOrFailAccount(false);
    }

    private void confirmOrFailAccount(boolean success) {
        List<LockedExternalTask> tasks = externalTaskService.fetchAndLock(FETCH_LOCK_SIZE, TASK_CONFIRM_ACCOUNT_WORKER)
                .topic(TASK_CONFIRM_ACCOUNT_TOPIC, LOCK_INTERVAL)
                .execute();

        for (LockedExternalTask task : tasks) {
            if (success) {
                log.info("******* confirm account for " + Util.variablesToString(task.getVariables()));
                externalTaskService.complete(task.getId(), TASK_CONFIRM_ACCOUNT_WORKER);
            } else {
                log.info("******* fail account for " + Util.variablesToString(task.getVariables()));
                externalTaskService.handleBpmnError(task.getId(), TASK_CONFIRM_ACCOUNT_WORKER, ERR_CONFIRM_ACCOUNT);
            }
        }
    }
}

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

    private static final String PROCESS_KEY = "registration";
    private static final String AWAIT_TASK_TOPIC = "awaitTask";

    private static final String WORKER_ID = "externalTaskWorker";
    private static final long LOCK_INTERVAL = 10000L;

    static final String EXTERNAL_ID_ATTRIBUTE = "externalId";
    static final String LINK_ID_ATTRIBUTE = "linkId";
    static final String CUSTOM_ATTRIBUTE1 = "attr1";
    static final String CUSTOM_ATTRIBUTE2 = "attr2";

    private RuntimeService runtimeService;
    private ExternalTaskService externalTaskService;

    public RegistrationProcess(RuntimeService runtimeService, ExternalTaskService externalTaskService) {
        this.runtimeService = runtimeService;
        this.externalTaskService = externalTaskService;
    }

    public String startRandom() {
        return start(UUID.randomUUID().toString(), UUID.randomUUID().toString());
    }

    public String start(String externalId, String linkId) {
        Map<String, Object> params = new HashMap<>();
        params.put(RegistrationProcess.EXTERNAL_ID_ATTRIBUTE, externalId);
        params.put(RegistrationProcess.LINK_ID_ATTRIBUTE, linkId);
        ProcessInstance process = runtimeService.startProcessInstanceByKey(PROCESS_KEY, params);
        return process.getId();
    }

    public void completeAwaitTasks() {
        log.info("Handle external tasks.");

        List<LockedExternalTask> tasks = externalTaskService.fetchAndLock(10, WORKER_ID)
                .topic(AWAIT_TASK_TOPIC, LOCK_INTERVAL)
                .execute();

        for (LockedExternalTask task : tasks) {
            log.info("Handle task " + Util.variablesToString(task.getVariables()));
            externalTaskService.complete(task.getId(), WORKER_ID);
        }
    }
}

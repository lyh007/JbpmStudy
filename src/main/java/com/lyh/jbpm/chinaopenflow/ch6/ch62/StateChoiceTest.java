package com.lyh.jbpm.chinaopenflow.ch6.ch62;

import org.jbpm.api.ProcessInstance;
import org.jbpm.test.JbpmTestCase;
import org.junit.Test;

/**
 * @author kevin
 * @version Revision: 1.00 Date: 11-8-5下午4:05
 * @Email liuyuhui007@gmail.com
 */
public class StateChoiceTest extends JbpmTestCase {
    String deploymentId;

    protected void setUp() throws Exception {
        super.setUp();

        deploymentId = repositoryService.createDeployment()
                .addResourceFromClasspath("com/lyh/jbpm/chinaopenflow/ch6/ch62/StateChoice.jpdl.xml").deploy();
    }

    protected void tearDown() throws Exception {
        // repositoryService.deleteDeploymentCascade(deploymentId);
    }

    @Test
    public void testStateChoice() {
        ProcessInstance processInstance = executionService.startProcessInstanceByKey("StateChoice");
    }
}

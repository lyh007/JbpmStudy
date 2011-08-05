package com.lyh.jbpm.chinaopenflow.ch6.ch61;

import org.jbpm.api.Execution;
import org.jbpm.api.ProcessInstance;
import org.jbpm.test.JbpmTestCase;
import org.junit.Test;

import java.util.Set;

/**
 * @author kevin
 * @version Revision: 1.00 Date: 11-8-5上午11:50
 * @Email liuyuhui007@gmail.com
 */
public class StateSequenceTest extends JbpmTestCase {
    String deploymentId;

    protected void setUp() throws Exception {
        super.setUp();

        deploymentId = repositoryService.createDeployment()
                .addResourceFromClasspath("com/lyh/jbpm/chinaopenflow/ch61/StateSequence.jpdl.xml").deploy();
    }

    protected void tearDown() throws Exception {
        // repositoryService.deleteDeploymentCascade(deploymentId);
    }

    @Test
    public void testStateSeq() {
        System.out.println("hello world!");
        ProcessInstance processInstance = executionService.startProcessInstanceByKey("StateSequence");
        System.out.println("******************start instance info**********************");
        System.out.println("getPriority=" + processInstance.getPriority());
        System.out.println("getState  " + processInstance.getState());
        System.out.println("getExecutions().size() " + processInstance.getExecutions().size());
        Set<String> activeNames = processInstance.findActiveActivityNames();
        for (String activeName : activeNames) {
            System.out.println("activeName:" + activeName);
        }
        System.out.println("findActiveActivityNames() " + processInstance.findActiveActivityNames().size());
        System.out.println("getId:" + processInstance.getId());
        System.out.println("getIsProcessInstance:" + processInstance.getIsProcessInstance());
        System.out.println("getKey() " + processInstance.getKey());
        System.out.println("getName: " + processInstance.getName());
        if (processInstance.getParent() != null)
            System.out.println("getParent().getName()" + processInstance.getParent().getName());
        System.out.println("getProcessDefinitionId " + processInstance.getProcessDefinitionId());
        System.out.println("isEnded " + processInstance.isEnded());
        System.out.println("isSuspended " + processInstance.isSuspended());
        System.out.println("******************end instance info**********************");

        Execution executionInA = processInstance.findActiveExecutionIn("a");
        assertNotNull(executionInA);
        System.out.println("******************start executionInA info**********************");
        System.out.println("getName: " + executionInA.getName());
        System.out.println("getID: " + executionInA.getId());
        System.out.println("findActiveActivityNames().size() " + executionInA.findActiveActivityNames().size());
        Set<String> executionInAActiveActivityNames = executionInA.findActiveActivityNames();
        for (String executionInAActiveActivityName : executionInAActiveActivityNames) {
            System.out.println("executionInAActiveActivityName " + executionInAActiveActivityName);
        }
        System.out.println("getIsProcessInstance " + executionInA.getIsProcessInstance());
        System.out.println("getKey " + executionInA.getKey());
        System.out.println("getProcessDefinitionId " + executionInA.getProcessDefinitionId());
        System.out.println("getState " + executionInA.getState());
        System.out.println("getPriority " + executionInA.getPriority());
        System.out.println(" hasExecution(\"b\")" + executionInA.hasExecution("b"));
        System.out.println("******************end executionInA info**********************");

        processInstance = executionService.signalExecutionById(executionInA.getId());
        System.out.println("******************start signalExecutionById instance info**********************");
        System.out.println("getPriority=" + processInstance.getPriority());
        System.out.println("getState  " + processInstance.getState());
        System.out.println("getExecutions().size() " + processInstance.getExecutions().size());
        activeNames = processInstance.findActiveActivityNames();
        for (String activeName : activeNames) {
            System.out.println("activeName:" + activeName);
        }
        System.out.println("findActiveActivityNames() " + processInstance.findActiveActivityNames().size());
        System.out.println("getId:" + processInstance.getId());
        System.out.println("getIsProcessInstance:" + processInstance.getIsProcessInstance());
        System.out.println("getKey() " + processInstance.getKey());
        System.out.println("getName: " + processInstance.getName());
        if (processInstance.getParent() != null)
            System.out.println("getParent().getName()" + processInstance.getParent().getName());
        System.out.println("getProcessDefinitionId " + processInstance.getProcessDefinitionId());
        System.out.println("isEnded " + processInstance.isEnded());
        System.out.println("isSuspended " + processInstance.isSuspended());
        System.out.println("******************end signalExecutionById instance info**********************");

        Execution executionInB = processInstance.findActiveExecutionIn("b");
        assertNotNull(executionInA);
        System.out.println("******************start executionInB info**********************");
        System.out.println("getName: " + executionInB.getName());
        System.out.println("getID: " + executionInB.getId());
        System.out.println("findActiveActivityNames().size() " + executionInB.findActiveActivityNames().size());
        executionInAActiveActivityNames = executionInB.findActiveActivityNames();
        for (String executionInAActiveActivityName : executionInAActiveActivityNames) {
            System.out.println("executionInAActiveActivityName " + executionInAActiveActivityName);
        }
        System.out.println("getIsProcessInstance " + executionInB.getIsProcessInstance());
        System.out.println("getKey " + executionInB.getKey());
        System.out.println("getProcessDefinitionId " + executionInB.getProcessDefinitionId());
        System.out.println("getState " + executionInB.getState());
        System.out.println("getPriority " + executionInB.getPriority());
        System.out.println("hasExecution(\"c\")" + executionInB.hasExecution("c"));
        System.out.println("******************end executionInB info**********************");

        assertNotNull(executionInB);

        processInstance = executionService.signalExecutionById(executionInB.getId());
        Execution executionInC = processInstance.findActiveExecutionIn("c");
        assertNotNull(executionInC);

        System.out.println("******************start instance c info**********************");
        System.out.println("getPriority=" + processInstance.getPriority());
        System.out.println("getState  " + processInstance.getState());
        System.out.println("getExecutions().size() " + processInstance.getExecutions().size());
        activeNames = processInstance.findActiveActivityNames();
        for (String activeName : activeNames) {
            System.out.println("activeName:" + activeName);
        }
        System.out.println("findActiveActivityNames() " + processInstance.findActiveActivityNames().size());
        System.out.println("getId:" + processInstance.getId());
        System.out.println("getIsProcessInstance:" + processInstance.getIsProcessInstance());
        System.out.println("getKey() " + processInstance.getKey());
        System.out.println("getName: " + processInstance.getName());
        if (processInstance.getParent() != null)
            System.out.println("getParent().getName()" + processInstance.getParent().getName());
        System.out.println("getProcessDefinitionId " + processInstance.getProcessDefinitionId());
        System.out.println("isEnded " + processInstance.isEnded());
        System.out.println("isSuspended " + processInstance.isSuspended());
        System.out.println("******************end instance c info**********************");


        System.out.println("******************start executionInC info**********************");
        System.out.println("getName: " + executionInC.getName());
        System.out.println("getID: " + executionInC.getId());
        System.out.println("findActiveActivityNames().size() " + executionInC.findActiveActivityNames().size());
        executionInAActiveActivityNames = executionInC.findActiveActivityNames();
        for (String executionInAActiveActivityName : executionInAActiveActivityNames) {
            System.out.println("executionInAActiveActivityName " + executionInAActiveActivityName);
        }
        System.out.println("getIsProcessInstance " + executionInC.getIsProcessInstance());
        System.out.println("getKey " + executionInC.getKey());
        System.out.println("getProcessDefinitionId " + executionInC.getProcessDefinitionId());
        System.out.println("getState " + executionInC.getState());
        System.out.println("getPriority " + executionInC.getPriority());
        System.out.println("hasExecution(\"c\")" + executionInC.hasExecution("c"));
        System.out.println("******************end executionInC info**********************");
    }
}

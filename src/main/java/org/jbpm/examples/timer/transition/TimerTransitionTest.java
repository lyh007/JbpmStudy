/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jbpm.examples.timer.transition;

import java.util.ArrayList;
import java.util.List;

import org.jbpm.api.ProcessInstance;
import org.jbpm.api.job.Job;
import org.jbpm.test.JbpmTestCase;

/**
 * @author Tom Baeyens
 */
public class TimerTransitionTest extends JbpmTestCase {
  
  String deploymentId;
  
  protected void setUp() throws Exception {
    super.setUp();
    
    deploymentId = repositoryService.createDeployment()
        .addResourceFromClasspath("org/jbpm/examples/timer/transition/process.jpdl.xml")
        .deploy();
  }

  protected void tearDown() throws Exception {
    repositoryService.deleteDeploymentCascade(deploymentId);
    
    super.tearDown();
  }

  public void testTimerTransitionTimerFires() {
    ProcessInstance processInstance = executionService.startProcessInstanceByKey("TimerTransition");
    
    Job job = managementService.createJobQuery()
      .timers()
      .processInstanceId(processInstance.getId())
      .uniqueResult();
    
    managementService.executeJob(job.getId());
    
    processInstance = executionService.findProcessInstanceById(processInstance.getId());
    
    assertTrue(processInstance.isActive("escalation"));
  }

  public void testTimerTransitionContinueBeforeTimerFires() {
    ProcessInstance processInstance = executionService.startProcessInstanceByKey("TimerTransition");
    
    String executionId = processInstance.findActiveExecutionIn("guardedWait").getId();

    executionService.signalExecutionById(executionId, "go on");
    
    processInstance = executionService.findProcessInstanceById(processInstance.getId());
    
    assertTrue(processInstance.isActive("next step"));

    List<Job> jobs = managementService.createJobQuery()
      .processInstanceId(processInstance.getId())
      .list();
    
    assertEquals(new ArrayList<Job>(), new ArrayList<Job>(jobs));
  }
}

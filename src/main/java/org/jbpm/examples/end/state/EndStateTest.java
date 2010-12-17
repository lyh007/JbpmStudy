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
package org.jbpm.examples.end.state;

import org.jbpm.api.ProcessInstance;
import org.jbpm.test.JbpmTestCase;


/**
 * @author Tom Baeyens
 */
public class EndStateTest extends JbpmTestCase {

  String deploymentId;
  
  protected void setUp() throws Exception {
    super.setUp();
    
    deploymentId = repositoryService.createDeployment()
        .addResourceFromClasspath("org/jbpm/examples/end/state/process.jpdl.xml")
        .deploy();
  }

  protected void tearDown() throws Exception {
    repositoryService.deleteDeploymentCascade(deploymentId);
    
    super.tearDown();
  }

  public void testEndStateCompleted() {
    ProcessInstance processInstance = executionService.startProcessInstanceByKey("EndState");
    String pid = processInstance.getId();
    processInstance = executionService.signalExecutionById(pid, "200");
    assertEquals("completed", processInstance.getState());
    assertTrue(processInstance.isEnded());
  }

  public void testEndStateErrorBadRequest() {
    ProcessInstance processInstance = executionService.startProcessInstanceByKey("EndState");
    String pid = processInstance.getId();
    processInstance = executionService.signalExecutionById(pid, "400");
    assertEquals("cancel", processInstance.getState());
    assertTrue(processInstance.isEnded());
  }

  public void testEndStateErrorInternalServerError() {
    ProcessInstance processInstance = executionService.startProcessInstanceByKey("EndState");
    String executionId = processInstance.getId();
    processInstance = executionService.signalExecutionById(executionId, "500");
    assertEquals("error", processInstance.getState());
    assertTrue(processInstance.isEnded());
  }
}

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
package org.jbpm.examples.concurrency.graphbased;

import java.util.HashSet;
import java.util.Set;

import org.jbpm.api.ProcessInstance;
import org.jbpm.test.JbpmTestCase;


/**
 * @author Tom Baeyens
 */
public class ConcurrencyGraphBasedTest extends JbpmTestCase {
  
  String deploymentId;
  
  protected void setUp() throws Exception {
    super.setUp();
    
    deploymentId = repositoryService.createDeployment()
        .addResourceFromClasspath("org/jbpm/examples/concurrency/graphbased/process.jpdl.xml")
        .deploy();
  }

  protected void tearDown() throws Exception {
    repositoryService.deleteDeploymentCascade(deploymentId);
    
    super.tearDown();
  }

  public void testConcurrencyGraphBased() {
    ProcessInstance processInstance = executionService.startProcessInstanceByKey("ConcurrencyGraphBased");
    String pid = processInstance.getId();
    
    Set<String> expectedActivityNames = new HashSet<String>();
    expectedActivityNames.add("send invoice");
    expectedActivityNames.add("load truck");
    expectedActivityNames.add("print shipping documents");
    
    assertEquals(expectedActivityNames, processInstance.findActiveActivityNames());
    
    assertNotNull(processInstance.findActiveExecutionIn("send invoice"));
    assertNotNull(processInstance.findActiveExecutionIn("load truck"));
    assertNotNull(processInstance.findActiveExecutionIn("print shipping documents"));
    
    String sendInvoiceExecutionId = processInstance.findActiveExecutionIn("send invoice").getId();
    processInstance = executionService.signalExecutionById(sendInvoiceExecutionId);

    expectedActivityNames.remove("send invoice");
    assertEquals(expectedActivityNames, processInstance.findActiveActivityNames());

    assertNotNull(processInstance.findActiveExecutionIn("load truck"));
    assertNotNull(processInstance.findActiveExecutionIn("print shipping documents"));

    String loadTruckExecutionId = processInstance.findActiveExecutionIn("load truck").getId();
    processInstance = executionService.signalExecutionById(loadTruckExecutionId);

    expectedActivityNames.remove("load truck");
    assertEquals(expectedActivityNames, processInstance.findActiveActivityNames());

    assertNotNull(processInstance.findActiveExecutionIn("print shipping documents"));
    
    String printShippingDocumentsId = processInstance.findActiveExecutionIn("print shipping documents").getId();
    processInstance = executionService.signalExecutionById(printShippingDocumentsId);

    expectedActivityNames.remove("print shipping documents");
    expectedActivityNames.add("drive truck to destination");
    assertEquals(expectedActivityNames, processInstance.findActiveActivityNames());

    assertNotNull(processInstance.findActiveExecutionIn("drive truck to destination"));

    String driveTruckExecutionId = processInstance.findActiveExecutionIn("drive truck to destination").getId();
    processInstance = executionService.signalExecutionById(driveTruckExecutionId);

    assertNull("execution "+pid+" should not exist", executionService.findExecutionById(pid));
  }
}

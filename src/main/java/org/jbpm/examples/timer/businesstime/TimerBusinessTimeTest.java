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
package org.jbpm.examples.timer.businesstime;

import org.jbpm.api.ProcessInstance;
import org.jbpm.api.job.Job;
import org.jbpm.test.JbpmTestCase;


/**
 * @author Tom Baeyens
 */
public class TimerBusinessTimeTest extends JbpmTestCase {
  
  static long HOUR_IN_MILLIS = 60*60*100;

  String deploymentId;
  
  protected void setUp() throws Exception {
    super.setUp();
    
    deploymentId = repositoryService.createDeployment()
        .addResourceFromClasspath("org/jbpm/examples/timer/businesstime/process.jpdl.xml")
        .deploy();
  }

  protected void tearDown() throws Exception {
    repositoryService.deleteDeploymentCascade(deploymentId);
    
    super.tearDown();
  }

  public void testBusinessTime() {
    long now = System.currentTimeMillis();

    ProcessInstance processInstance = executionService.startProcessInstanceByKey("TimerBusinessTime");

    Job job = managementService.createJobQuery()
      .processInstanceId(processInstance.getId())
      .uniqueResult();

    long difference = job.getDueDate().getTime() - now;

    // we do not know when this test will be run.  So the exact actual duedate of the timer
    // can not be calculated easily.  But we do know for sure that each working day only 
    // has 8 business hours.  So we know that 9 business hours later always means at 
    // least 24 hours later
    assertTrue( 24 * HOUR_IN_MILLIS < difference );
  }
}

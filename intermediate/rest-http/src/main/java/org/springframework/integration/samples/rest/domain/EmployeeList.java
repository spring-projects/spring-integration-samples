/*
 * Copyright 2002-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.integration.samples.rest.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * EmployeeList.java: EmployeeList Domain class
 * @author Vigil Bose
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "employee",
    "returnStatus",
    "returnStatusMsg"
})
@XmlRootElement(name = "EmployeeList")
public class EmployeeList {
	
	@XmlElement(name = "Employee", required = true)
	private List<Employee> employee;
	
	@XmlElement(name = "returnStatus", required = true)
	private String returnStatus;
	
	@XmlElement(name = "returnStatusMsg", required = true)
	private String returnStatusMsg;

	/**
	 * @return the employee
	 */
	public List<Employee> getEmployee() {
		if (employee == null){
			employee = new ArrayList<Employee>();
		}
		return employee;
	}

	/**
	 * @return the returnStatus
	 */
	public String getReturnStatus() {
		return returnStatus;
	}

	/**
	 * @param returnStatus the returnStatus to set
	 */
	public void setReturnStatus(String returnStatus) {
		this.returnStatus = returnStatus;
	}

	/**
	 * @return the returnStatusMsg
	 */
	public String getReturnStatusMsg() {
		return returnStatusMsg;
	}

	/**
	 * @param returnStatusMsg the returnStatusMsg to set
	 */
	public void setReturnStatusMsg(String returnStatusMsg) {
		this.returnStatusMsg = returnStatusMsg;
	}
}



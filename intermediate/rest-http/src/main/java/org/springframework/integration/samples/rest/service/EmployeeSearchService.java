/*
 * Copyright 2002-2017 the original author or authors.
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
package org.springframework.integration.samples.rest.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.integration.samples.rest.domain.Employee;
import org.springframework.integration.samples.rest.domain.EmployeeList;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

/**
 * EmployeeSearchService.java: This is the default employee search service
 * @author Vigil Bose
 * @author Gary Russell
 */
@Service("employeeSearchService")
public class EmployeeSearchService {

	private static Log logger = LogFactory.getLog(EmployeeSearchService.class);
	/**
	 * The API <code>getEmployee()</code> looks up the mapped in coming message header's id param
	 * and fills the return object with the appropriate employee details. The return
	 * object is wrapped in Spring Integration Message with response headers filled in.
	 * This example shows the usage of URL path variables and how the service act on
	 * those variables.
	 * @param inMessage
	 * @return an instance of <code>{@link Message}</code> that wraps <code>{@link EmployeeList}</code>
	 */
	@Secured("ROLE_REST_HTTP_USER")
	public Message<EmployeeList> getEmployee(Message<?> inMessage){

		EmployeeList employeeList = new EmployeeList();
		Map<String, Object> responseHeaderMap = new HashMap<String, Object>();

		try{
			MessageHeaders headers = inMessage.getHeaders();
			String id = (String)headers.get("employeeId");
			boolean isFound;
			if (id.equals("1")) {
				employeeList.getEmployee().add(new Employee(1, "John", "Doe"));
				isFound = true;
			}
			else if (id.equals("2")) {
				employeeList.getEmployee().add(new Employee(2, "Jane", "Doe"));
				isFound = true;
			}
			else if (id.equals("0")) {
				employeeList.getEmployee().add(new Employee(1, "John", "Doe"));
				employeeList.getEmployee().add(new Employee(2, "Jane", "Doe"));
				isFound = true;
			}
			else {
				isFound = false;
			}
			if (isFound) {
				setReturnStatusAndMessage("0", "Success", employeeList, responseHeaderMap);
			}
			else {
				setReturnStatusAndMessage("2", "Employee Not Found", employeeList, responseHeaderMap);
			}

		}
		catch (Exception e) {
			setReturnStatusAndMessage("1", "System Error", employeeList, responseHeaderMap);
			logger.error("System error occured :" + e);
		}
		Message<EmployeeList> message = new GenericMessage<EmployeeList>(employeeList, responseHeaderMap);
		return message;
	}

	/**
	 * The API <code>setReturnStatusAndMessage()</code> sets the return status and return message
	 * in the return message payload and its header.
	 * @param status
	 * @param message
	 * @param employeeList
	 * @param responseHeaderMap
	 */
	private void setReturnStatusAndMessage(String status,
						String message,
						EmployeeList employeeList,
						Map<String, Object> responseHeaderMap){

		employeeList.setReturnStatus(status);
		employeeList.setReturnStatusMsg(message);
		responseHeaderMap.put("Return-Status", status);
		responseHeaderMap.put("Return-Status-Msg", message);
	}

}



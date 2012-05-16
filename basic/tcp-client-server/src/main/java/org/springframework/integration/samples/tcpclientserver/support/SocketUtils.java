/*
 * Copyright 2002-2012 the original author or authors.
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
package org.springframework.integration.samples.tcpclientserver.support;

import java.net.ServerSocket;

import javax.net.ServerSocketFactory;

/**
 *
 * @author Gunnar Hillert
 *
 */
public final class SocketUtils {

	public static int findAvailableServerSocket(int seed) {
		for (int i = seed; i < seed+200; i++) {
			try {
				ServerSocket sock = ServerSocketFactory.getDefault().createServerSocket(i);
				sock.close();
				return i;
			} catch (Exception e) { }
		}
		throw new RuntimeException("Cannot find a free server socket");
	}

	public static int findAvailableServerSocket() {
		return findAvailableServerSocket(5678);
	}

}

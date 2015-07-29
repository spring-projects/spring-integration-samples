/*
 * Copyright 2002-2015 the original author or authors.
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
package org.springframework.integration.samples.storedprocedure.jdbc.storedproc.derby;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.support.JdbcUtils;

/**
 *
 * @author Gunnar Hillert
 * @author Gary Russell
 * @since 2.1
 *
 */
public final class DerbyStoredProcedures {

	private DerbyStoredProcedures() {
	}

	public static void findCoffee(int coffeeId, String[] coffeeDescription)
			throws SQLException {
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = DriverManager.getConnection("jdbc:default:connection");
			String sql = "SELECT * FROM COFFEE_BEVERAGES WHERE ID = ? ";
			statement = connection.prepareStatement(sql);
			statement.setLong(1, coffeeId);

			ResultSet resultset = statement.executeQuery();
			resultset.next();
			coffeeDescription[0] = resultset.getString("COFFEE_DESCRIPTION");

		}
		finally {
			JdbcUtils.closeStatement(statement);
			JdbcUtils.closeConnection(connection);
		}

	}

	public static void findAllCoffeeBeverages(ResultSet[] coffeeBeverages)
			throws SQLException {

		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = DriverManager.getConnection("jdbc:default:connection");
			String sql = "SELECT * FROM COFFEE_BEVERAGES";
			statement = connection.prepareStatement(sql);//NOSONAR see below
			coffeeBeverages[0] = statement.executeQuery();
		}
		finally {
//			JdbcUtils.closeStatement(statement); // cannot close due to result set being returned
			JdbcUtils.closeConnection(connection);
		}

	}
}

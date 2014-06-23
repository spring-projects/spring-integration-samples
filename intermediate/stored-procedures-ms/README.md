Spring Integration - Stored Procedure Example - Microsoft SQL Server (Express)
================================================================================

# Overview

This example provides a simple example using the *Stored Procedure Outbound Gateway*. This example will call *Stored Procedure* as well as a *User-defined Function* using *Microsoft SQL Server (Express)*.

# Setup

## Pre-requisites

Access to a *Microsoft SQL Server* or *Microsoft SQL Server Express* database instance.

This sample was tested against: **Microsoft SQL Server 2008 R2 RTM - Express** (Which can be downloaded and used for free). The sample should also work for newer versions (including the full version) of *Microsoft SQL Server*. You can download *Microsoft SQL Server Express 2008: SQL Server Express*:

* [http://www.microsoft.com/en-us/download/details.aspx?id=23650](http://www.microsoft.com/en-us/download/details.aspx?id=23650)

If you have trouble accessing a remote instance of *Microsoft SQL Server Express*, see:

* [http://support.microsoft.com/default.aspx?scid=kb;EN-US;914277#method2](http://support.microsoft.com/default.aspx?scid=kb;EN-US;914277#method2)

## JDBC Driver

This sample uses the [jTDS](http://jtds.sourceforge.net) driver, which is considered to be faster than [Microsoft's JDBC driver](http://msdn.microsoft.com/en-us/sqlserver/aa937724.aspx). Nevertheless, the sample should work with either driver.

#### Creating the Stored Procedure

	USE [your database name]
	GO

	IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[CAPITALIZE_STRING]') AND type in (N'P', N'PC'))
	DROP PROCEDURE [dbo].[CAPITALIZE_STRING]
	GO

	SET ANSI_NULLS ON
	GO

	SET QUOTED_IDENTIFIER ON
	GO

	-- ===========================================================
	-- Author:		Gunnar Hillert
	-- Create date: 2012-Aug-30
	-- Description:	Simple Stored Procedure to capatilize a string
	-- ===========================================================

	CREATE PROCEDURE [dbo].[CAPITALIZE_STRING]
		@inoutString VARCHAR(100) OUTPUT
	AS
	BEGIN
		-- SET NOCOUNT ON added to prevent extra result sets from
		-- interfering with SELECT statements.
		SET NOCOUNT ON;

		select @inoutString = upper(@inoutString);

	END

	GO

#### Creating the Function

	USE [sitest]
	GO

	IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[GET_COOL_NUMBER]') AND type in (N'FN', N'IF', N'TF', N'FS', N'FT'))
	DROP FUNCTION [dbo].[GET_COOL_NUMBER]
	GO

	SET ANSI_NULLS ON
	GO

	SET QUOTED_IDENTIFIER ON
	GO

	-- ===========================================================
	-- Author:		Gunnar Hillert
	-- Create date: 2012-Aug-30
	-- Description:	Simple Function that returns a constant number
	-- ===========================================================

	CREATE FUNCTION [dbo].[GET_COOL_NUMBER]
	(
	)
	RETURNS int
	AS
	BEGIN
		DECLARE @cool_number int = 12345;
		RETURN @cool_number;
	END

	GO

### Setting up the DataSource

You may have to update the *Microsoft SQL Server* properties in:

    /src/main/resources/META-INF/spring/integration/spring-integration-context.xml

	<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource"
		destroy-method="close">
		<property name="driverClass" value="net.sourceforge.jtds.jdbc.Driver" />
		<property name="jdbcUrl" value="jdbc:jtds:sqlserver://172.16.48.128:1433/sitest" />
		<property name="user" value="sitest" />
		<property name="password" value="integration" />
	</bean>


# Run the Sample

* running the "Main" class from within STS (Right-click on Main class --> Run As --> Java Application)
* or from the command line:
    
    $ gradlew :stored-procedures-ms:run

--------------------------------------------------------------------------------

For help please take a look at the Spring Integration documentation:

http://www.springsource.org/spring-integration


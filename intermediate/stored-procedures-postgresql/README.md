Spring Integration - Stored Procedure Example - PostgreSQL
================================================================================

# Overview

This example provides a simple example using the *Stored Procedure Outbound Gateway*
adapter. This example will call 1 PostgreSQL **Stored Function** and 1 **Stored Procedure**.

The second procedure returns a **ResultSet**.

# Setup

## PostgreSQL

Please ensure that you can connect to a running instance of a *PostgreSQL server*. The sample was tested using **PostgreSQL v9.2.1**. 

### Required Table

```SQL
CREATE TABLE "COFFEE_BEVERAGES"
(
  "ID" integer NOT NULL,
  "COFFEE_NAME" text,
  "COFFEE_DESCRIPTION" text,
  CONSTRAINT "COFFEE_BEVERAGES_pkey" PRIMARY KEY ("ID")
)
```

### Sample Data

```SQL
INSERT INTO "COFFEE_BEVERAGES" ("ID", "COFFEE_NAME", "COFFEE_DESCRIPTION") VALUES (1, 'Espresso',     'Espressos keep developers going in the morning. There are never enough of them.');
INSERT INTO "COFFEE_BEVERAGES" ("ID", "COFFEE_NAME", "COFFEE_DESCRIPTION") VALUES (2, 'Cappuccino',   'For the finer moments. Wrap your espresso in a tasty layer of foam.');
INSERT INTO "COFFEE_BEVERAGES" ("ID", "COFFEE_NAME", "COFFEE_DESCRIPTION") VALUES (3, 'Mocha',        'Mmmmh, chocolate.');
INSERT INTO "COFFEE_BEVERAGES" ("ID", "COFFEE_NAME", "COFFEE_DESCRIPTION") VALUES (4, 'Latte',        'If you are more into milk than into foam.');
```

### Stored Procedures

Please create the following Stored Procedure/Function:

```SQL
CREATE OR REPLACE FUNCTION find_all_coffee_beverages()
  RETURNS refcursor AS
$BODY$
DECLARE
    ref refcursor;
BEGIN
  OPEN ref FOR SELECT "ID", "COFFEE_NAME", "COFFEE_DESCRIPTION" FROM "COFFEE_BEVERAGES";
  RETURN ref;
END;
$BODY$
LANGUAGE plpgsql VOLATILE
COST 100;
```

```SQL
CREATE OR REPLACE FUNCTION find_coffee(coffee_name integer)
  RETURNS character varying AS
$BODY$
declare
    description character varying;
begin
    SELECT into description "COFFEE_DESCRIPTION" from "COFFEE_BEVERAGES" where "ID"=coffee_name;
    return description;
end
$BODY$
LANGUAGE plpgsql VOLATILE
COST 100;
```
### Configure the DataSource

Please configure the necessary credentials in order to connect to your database in **/src/main/resources/META-INF/spring/integration/spring-integration-context.xml**. The default setting expects a database **integration** to run on localhost with a usersname of **postgres** and a password of **postgres**.

# Run the Sample

* running the "Main" class from within STS (Right-click on Main class --> Run As --> Java Application)
* or from the command line:
    
    $ gradlew :stored-procedures-postgresql:run

* Follow the screen (command line) instructions.

--------------------------------------------------------------------------------

For help please take a look at the Spring Integration documentation:

http://www.springsource.org/spring-integration


Transaction Synchronization Sample "tx-synch"
=============================================

This sample shows how to use the 2.2.0 Transaction Synchronization feature.

Run the class `TransactionSynchronizationDemo` as a java application (main).

The Spring Integration application consists of a simple flow that reads a file, and stores its contents in a database table.

If the contents of the file starts with "fail", after writing to the database, the 'ConditionalService' throws an exception and rolls back the database update.

There are two synchronization actions

- rename the file, adding '.SUCCEEDED' to the filename when the transaction commits
- rename the file, adding '.FAILED' to the filename when the transaction rolls back

These actions are logged.

The file inbound adapter looks for files in ${java.io.tmpdir}/txSynchDemo

The java.io.tmpdir for your machine is displayed in the console…

````
    This is the Transaction Synchronization Sample -

    Press 'Enter' to terminate.

    Place a file in /var/folders/k0/gch26h6d2ms9t0g7pyhtzfkc0000gn/T/txSynchDemo ending
    with .txt
    If the first line begins with 'fail' the transaction
    transaction will be rolled back.The result of the
    expression evaluation is logged.

=========================================================
/var/folders/k0/gch26h6d2ms9t0g7pyhtzfkc0000gn/T/txSynchDemo
````

To send a good file, put a file in that directory; for example:

echo good > /var/folders/k0/gch26h6d2ms9t0g7pyhtzfkc0000gn/T/txSynchDemo/xx.txt

echo failing > /var/folders/k0/gch26h6d2ms9t0g7pyhtzfkc0000gn/T/txSynchDemo/x.txt


The inbound adapter uses the default filters so each filename may only be used once for each run of the program.


In addition, there is a JDBC inbound adapter that polls the database every 5 seconds, and logs its contents - this can be used to see commit Vs. rollback in the database.

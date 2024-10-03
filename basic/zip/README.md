Spring Integration Zip Sample
===================

This sample illustrates the usage of the Spring Integration Zip Extension. It uses the following components:

* zip-transformer
* unzip-transformer

You can run the application by either

* running the "Main" class from the IDE
* or from the command line using Maven in this sub-project:
    - mvn package
    - mvn exec:java
* or from the command line using Gradle from the root project:
    - gradlew :zip:run

You should see a screen as the following:

```
=========================================================

    Welcome to the Spring Integration Zip Sample

    For more information please visit:
    https://www.springsource.org/spring-integration

=========================================================
17:08:41.883 INFO  [org.springframework.integration.samples.zip.Main.main()][org.springframework.integration.samples.zip.SpringIntegrationUtils]
=========================================================

    Intput directory is: '/dev/spring-integration-extensions/samples/zip/input-zip'
    Intput directory is: '/dev/spring-integration-extensions/samples/zip/input-uncompressed'
    Output directory is: 'target/output/decompressedFilesOut'
    Output directory is: 'target/output/zipFilesOut'

=========================================================
17:08:41.887 INFO  [org.springframework.integration.samples.zip.Main.main()][org.springframework.integration.samples.zip.Main]
=========================================================

    Please press 'q + Enter' to quit the application.

=========================================================
```
## Compressing Files

Drop an uncompressed file into the **input-uncompressed** directory. The file will be compressed and stored under **target/output/zipFilesOut**.

## Uncompressing Files

Drop a compressed file into the **input-zip** directory. The file will be decompressed and stored under **target/output/decompressedFilesOut**.

--------------------------------------------------------------------------------

For help please take a look at the Spring Integration documentation:

https://www.spring.io/spring-integration


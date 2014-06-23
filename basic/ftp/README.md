FTP Samples
===========

## Introduction

This example demonstrates the following aspects of the FTP support available with Spring Integration:

1. Transfer local files via the FTP Outbound Channel Adapter to a remote directory
2. Poll for remote files using the FTP Inbound Channel Adapter
3. Execute explicit FTP command (LS, RM) in order to retrieve a remote file listing and to subsequently delete those files.

## Setup

The samples work out of the box using an embedded Apache FTP Server. Simply execute:

    $ gradlew :ftp:run

and the samples are build as well as executed. The samples are part of a JUnit test suite:

    org.springframework.integration.samples.ftp.TestSuite.java

which comprises the following tests that correspond to the scenarios outlined above:

* org.springframework.integration.samples.ftp.FtpOutboundChannelAdapterSample.class, 
* org.springframework.integration.samples.ftp.FtpInboundChannelAdapterSample.class,
* org.springframework.integration.samples.ftp.FtpOutboundGatewaySample.class

Keep in mind that the tests are meant to be executed in sequence. 

## The Scenarios

### Outbound Channel Adapter
	
This sample will take 2 local files

1. a.txt
2. b.txt

and transfer them to a remote directory '/'.

### Inbound Channel Adapter
	
This test will use the 2 files previously uploaded. Using an Inbound Channel Adapter, the test will poll the remote (Root) directory that will contain 2 files. The adapter will attempt to transfer them to a local directory,  which will be generated. Once copied, the files will be sent as a payload of the message to a channel. We are using a file filter, transferring only files that end with 'txt'. The remote files are not deleted. 

### Outbound Gateway

The last test will re-use the 2 files that are still on the remote FTP server. This test will retrieve and removes them through explicit FTP commands (ls and rm).

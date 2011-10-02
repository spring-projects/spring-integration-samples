This example demonstrates the following aspects of the FTP support available with Spring Integration:
1. FTP Inbound Channel Adapter (transfers files from remote to local directory)
2. FTP Outbound Channel Adapter (transfers files from local to the remote directory)

	#### INBOUND CHANNEL ADAPTER ####
	
To run INBOUND CHANNEL ADAPTER sample execute FtpInboundChannelAdapterSample test. You will see that based on configuration it
will access sample remote directory which contains 3 files and will attempt to transfer them to a local directory which
will be generated. Once copied the files will be sent as a payload of the message to a channel.
The output should look like this:
=====
Received first file message: [Payload=local-dir/a.txt][Headers={timestamp=1290066001349, id=9dca686a-cfd4-4d96-a1a7-761feb005e43}]
Received second file message: [Payload=local-dir/b.txt][Headers={timestamp=1290066001650, id=d33a475d-fa71-4c5b-b73e-3147969f1c6f}]
Received nothing else
=====

As you can see, although the remote directory had 3 files we only received 2 since we were filtering only the files that end with 'txt'.

#### OUTBOUND CHANNEL ADAPTER ####
	
To run OUTBOUND CHANNEL ADAPTER sample execute FtpOutboundChannelAdapterSample test. You will see that based on configuration it
will attempt to transfer this 'readme.txt' file to a remote directory 'remote-target-dir'
The output should look like this:
=====
Successfully transfered 'readme.txt' file to a remote location under the name 'readme.txt'
=====


#### OUTBOUND GATEWAY ####

Run the FtpOutoundGateway sample as a JUnit test; it creates 2 files, retrieves and removes them over ftp. It cleans up
by removing the retrieved files. Test assumes full access to the filesystem via /tmp where the test files are created. 
Requires an ftp server running on localhost.

Requires setting of user and password properties in user.properties.
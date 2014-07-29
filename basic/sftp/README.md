SFTP Sample
===========

This example demonstrates the following aspects of the SFTP support available with Spring Integration:

1. SFTP Inbound Channel Adapter (transfers files from remote to local directory)
2. SFTP Outbound Channel Adapter (transfers files from local to the remote directory)

In order to run this sample for the 'real' SFTP Server you need to:

1. generate private/public keys. Below is simple directions what needs to be done
2. update user.properties file with appropriate values
3. run the sample

By default this sample uses an [Apache MINA](http://mina.apache.org/sshd-project) embedded `SshServer` with predefined 
private and public keys.
Note, the embedded Server is started only when the `port` property remains as `-1`. In this case the target port
for the Embedded Server is selected randomly. For a real SFTP server you should specify correct `host/port` properties.

NOTE: The test cases will create/delete a directory `si.sftp.sample`.
	
## INBOUND CHANNEL ADAPTER
	
To run the NBOUND CHANNEL ADAPTER sample execute **SftpInboundReceiveSample** test. You will see that based on configuration it will access the sample remote directory which contains 3 files and will attempt to securely copy them to a local directory which will be generated. The output should look like this:

	Received first file message: [Payload=local-dir/a.txt][Headers={timestamp=1290066001349, id=9dca686a-cfd4-4d96-a1a7-761feb005e43}]
	Received second file message: [Payload=local-dir/b.txt][Headers={timestamp=1290066001650, id=d33a475d-fa71-4c5b-b73e-3147969f1c6f}]
	No third file was received null

As you can see, although the remote directory had 3 files we only received 2 since we were filtering only the files that end with **txt**.

## OUTBOUND CHANNEL ADAPTER
	
To run the OUTBOUND CHANNEL ADAPTER sample execute the **SftpOutboundTransferSample** test. You will see that based on the configuration it will attempt to transfer the **README.md** file to a remote directory **remote-target-dir**. The output should look like this:

	Successfully transferred 'README.md' file to a remote location under the name 'README.md_foo'

NOTE: You can see that we are using *SpEL* via the **remote-filename-generator-expression** attribute to define the remote file name by simply appending **_foo** to the original file name.

## OUTBOUND GATEWAY

Run the **FtpOutoundGateway** sample as a JUnit test; it creates 2 files, retrieves and removes them over ftp. It cleans up by removing the retrieved files. The test assumes full access to the filesystem via **/tmp** where the test files are created. Requires **sshd** to be running on localhost.

This sample uses a property **private.keyfile** to point to the location of your private key. Requires setting of **user**, **private.keyfile** and, optionally, **passphrase** in **user.properties**.
	
### HOW TO GENERATE KEYS

	>$ ssh-keygen
	 Generating public/private rsa key pair.
	 Enter file in which to save the key (/Users/ozhurakousky/.ssh/id_rsa): ./sftp_rsa
	 Enter passphrase (empty for no passphrase): 
	 Enter same passphrase again: 
	 Your identification has been saved in ./sftp_rsa.
	 Your public key has been saved in ./sftp_rsa.pub.
	 The key fingerprint is:
	 2c:30:7c:18:3c:a0:d5:83:68:78:7e:c1:5f:c1:3e:3d ozhurakousky@oleg.home
	 The key's randomart image is:
	 +--[ RSA 2048]----+
	 |..o*.  ...       |
	 |o+o.Bo  o        |
	 |oo  ==.o .       |
	 |  . .+..o E      |
	 |   .  . S. .     |
	 |       .         |
	 |                 |
	 |                 |
	 |                 |
	 +-----------------+
	>$ ls
	 sftp_rsa	sftp_rsa.pub
	>$

* Create a file ~/.ssh/authorized_keys and give it the required permissions

	>$ chmod 600 ~/.ssh/authorized_keys

* Then edit **authorized_keys** file and paste in the contents of the public key file **sftp_rsa.pub**.
* Move your private key file **sftp_rsa** to the directory **/src/test/resources/META-INF/keys/** directory.

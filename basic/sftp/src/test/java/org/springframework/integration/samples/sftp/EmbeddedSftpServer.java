/*
 * Copyright 2014-2017 the original author or authors.
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

package org.springframework.integration.samples.sftp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import java.util.Collections;

import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.SmartLifecycle;
import org.springframework.core.io.ClassPathResource;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.util.Base64Utils;
import org.springframework.util.StreamUtils;

/**
 * @author Artem Bilan
 */
public class EmbeddedSftpServer implements InitializingBean, SmartLifecycle {

	/**
	 * Let OS to obtain the proper port
	 */
	public static final int PORT = 0;

	private final SshServer server = SshServer.setUpDefaultServer();

	private volatile int port;

	private volatile boolean running;

	private DefaultSftpSessionFactory defaultSftpSessionFactory;

	public void setPort(int port) {
		this.port = port;
	}

	public void setDefaultSftpSessionFactory(DefaultSftpSessionFactory defaultSftpSessionFactory) {
		this.defaultSftpSessionFactory = defaultSftpSessionFactory;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		final PublicKey allowedKey = decodePublicKey();
		this.server.setPublickeyAuthenticator((username, key, session) -> key.equals(allowedKey));
		this.server.setPort(this.port);
		this.server.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(new File("hostkey.ser")));
		this.server.setSubsystemFactories(Collections.singletonList(new SftpSubsystemFactory()));
		final String pathname = System.getProperty("java.io.tmpdir") + File.separator + "sftptest" + File.separator;
		new File(pathname).mkdirs();
		server.setFileSystemFactory(new VirtualFileSystemFactory(Paths.get(pathname)));
	}

	private PublicKey decodePublicKey() throws Exception {
		InputStream stream = new ClassPathResource("META-INF/keys/sftp_rsa.pub").getInputStream();
		byte[] keyBytes = StreamUtils.copyToByteArray(stream);
		// strip any newline chars
		while (keyBytes[keyBytes.length - 1] == 0x0a || keyBytes[keyBytes.length - 1] == 0x0d) {
			keyBytes = Arrays.copyOf(keyBytes, keyBytes.length - 1);
		}
		byte[] decodeBuffer = Base64Utils.decode(keyBytes);
		ByteBuffer bb = ByteBuffer.wrap(decodeBuffer);
		int len = bb.getInt();
		byte[] type = new byte[len];
		bb.get(type);
		if ("ssh-rsa".equals(new String(type))) {
			BigInteger e = decodeBigInt(bb);
			BigInteger m = decodeBigInt(bb);
			RSAPublicKeySpec spec = new RSAPublicKeySpec(m, e);
			return KeyFactory.getInstance("RSA").generatePublic(spec);

		}
		else {
			throw new IllegalArgumentException("Only supports RSA");
		}
	}

	private BigInteger decodeBigInt(ByteBuffer bb) {
		int len = bb.getInt();
		byte[] bytes = new byte[len];
		bb.get(bytes);
		return new BigInteger(bytes);
	}

	@Override
	public boolean isAutoStartup() {
		return PORT == this.port;
	}

	@Override
	public int getPhase() {
		return Integer.MAX_VALUE;
	}

	@Override
	public void start() {
		try {
			this.server.start();
			this.defaultSftpSessionFactory.setPort(this.server.getPort());
			this.running = true;
		}
		catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void stop(Runnable callback) {
		stop();
		callback.run();
	}

	@Override
	public void stop() {
		if (this.running) {
			try {
				server.stop(true);
			}
			catch (Exception e) {
				throw new IllegalStateException(e);
			}
			finally {
				this.running = false;
			}
		}
	}

	@Override
	public boolean isRunning() {
		return this.running;
	}
}

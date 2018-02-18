package org.springframework.integration.samples.filecopy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.support.PeriodicTrigger;

/**
 * 
	



	


 *
 */
@Configuration
public class BinaryConfig {

	@Value("${java.io.tmpdir}")
	private String tmpDir;

	/**
	 * <file:inbound-channel-adapter id="filesIn" directory=
	 * "file:${java.io.tmpdir}/spring-integration-samples/input"/>
	 */
	@Bean
	@InboundChannelAdapter
	public MessageSource<File> filesIn() {
		FileReadingMessageSource source = new FileReadingMessageSource();
		source.setDirectory(new File(tmpDir + "/spring-integration-samples/input"));
		return source;
	}

	/**
	 * <integration:channel id="bytes"/>
	 */
	@Bean
	public MessageChannel bytes() {
		return MessageChannels.direct().get();
	}

	/**
	 * 
	 * <file:file-to-bytes-transformer input-channel="filesIn" output-channel=
	 * "bytes" delete-files="true"/>
	 * <integration:service-activator input-channel="bytes" output-channel=
	 * "filesOut" ref="handler"/>
	 * <file:outbound-channel-adapter id="filesOut" directory=
	 * "file:${java.io.tmpdir}/spring-integration-samples/output"/>
	 */
	@Bean
	public IntegrationFlow copyFlow(@Autowired final Handler handler) {
		return IntegrationFlows.from("filesIn")
				.transform(new GenericTransformer<File, byte[]>() {
			public byte[] transform(File source) {
				try {
					return Files.readAllBytes(source.toPath());
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
		}).handle(new GenericHandler<byte[]>() {
			public Object handle(byte[] payload, Map<String, Object> headers) {
				return handler.handleBytes(payload);
			}
		}).handle(new GenericHandler<byte[]>(){

			public Object handle(byte[] payload, Map<String, Object> headers) {
				FileWritingMessageHandler handler = new FileWritingMessageHandler(new File(tmpDir + "/spring-integration-samples/output"));
				handler.setDeleteSourceFiles(true);
				handler.start();
				return handler;
			}
			
		}).get();
	}

	/**
	 * <bean id="handler" class=
	 * "org.springframework.integration.samples.filecopy.Handler"/>
	 */
	@Bean
	public Handler handler() {
		return new Handler();
	}

	/**
	 * <integration:poller id="poller" default="true" fixed-delay="1000"/>
	 */
	@Bean(name = PollerMetadata.DEFAULT_POLLER)
	public PollerMetadata defaultPoller() {
		PollerMetadata pollerMetadata = new PollerMetadata();
		pollerMetadata.setTrigger(new PeriodicTrigger(1000));
		return pollerMetadata;
	}
}

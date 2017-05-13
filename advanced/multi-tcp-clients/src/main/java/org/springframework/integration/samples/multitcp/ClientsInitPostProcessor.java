package org.springframework.integration.samples.multitcp;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.serializer.DefaultDeserializer;
import org.springframework.core.serializer.DefaultSerializer;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.ConsumerEndpointFactoryBean;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.config.IntegrationConfigUtils;
import org.springframework.integration.ip.config.TcpConnectionFactoryFactoryBean;
import org.springframework.integration.ip.tcp.TcpInboundGateway;
import org.springframework.integration.ip.tcp.TcpOutboundGateway;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNetServerConnectionFactory;
import org.springframework.integration.samples.multitcp.Conn.Server;
import org.springframework.messaging.MessageChannel;

@EnableIntegration
@IntegrationComponentScan
@Configuration
public class ClientsInitPostProcessor implements EnvironmentAware,  BeanDefinitionRegistryPostProcessor{
	private static Logger logger = LoggerFactory.getLogger(ClientsInitPostProcessor.class);  
	private Conn config = new Conn();
	private static int MAX_CLIENTS = 100;
	private static String prefix = "conn.servers";
	private static String ID = "id";
	private static String IP = "ip";
	private static String PORT = "port";
	private static String SO_TIMEOUT = "sotimeout";
	private static String REQUEST_TIMEOUT = "sotimeout";
	private static String REMOTE_TIMEOUT = "sotimeout";
	private static String SERIALIZER = "serializer";
	private static String DESERIALIZER = "deserializer";
	@MessagingGateway(defaultRequestChannel="input")
    public interface SimpleGateway {
		 public Msg send(Msg payload);
    }
	
	@Bean
	public MessageChannel output() {
		return new DirectChannel();
	}
	
	@Bean
	public DefaultSerializer javaSerializer(){
		return new DefaultSerializer();
	}
	
	@Bean
	public DefaultDeserializer javaDeserializer(){
		return new DefaultDeserializer();
	}
	
	@Override
	public void setEnvironment(Environment environment) {
		RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(environment);
		Map<String, Object> vals = propertyResolver.getSubProperties(prefix);
		
		for (int j = 0; j < MAX_CLIENTS; j++) {
			String n = "[" + j + "]." + ID;
			String name = String.valueOf(vals.get(n));
			if(name == null || "null".equalsIgnoreCase(name)){
				break;
			}
			Server ss = new Server();
			ss.setId(name);
			String ip = (String)vals.get("[" + j + "]." + IP);
			Integer port = (Integer)vals.get("[" + j + "]." + PORT);
			Integer soto = (Integer)vals.get("[" + j + "]." + SO_TIMEOUT);
			Integer reqto = (Integer)vals.get("[" + j + "]." + REQUEST_TIMEOUT);
			Integer remto = (Integer)vals.get("[" + j + "]." + REMOTE_TIMEOUT);
			String serial = (String)vals.get("[" + j + "]." + SERIALIZER);
			String deserial = (String)vals.get("[" + j + "]." + DESERIALIZER);
			ss.setIp(ip);
			ss.setPort(port == null ? 4196 : port);
			ss.setSotimeout(soto == null ? 10000 : soto);
			ss.setRequestTimeout(reqto == null ? 100 : reqto);
			ss.setRemoteTimeout(remto == null ? 800 : remto);
			
			if(serial != null && serial.trim().length() > 0 && !"null".equalsIgnoreCase(serial)){
				ss.setSerializer(serial);
			}else{
				ss.setSerializer("javaSerializer");
			}
			if(deserial != null && deserial.trim().length() > 0 && !"null".equalsIgnoreCase(deserial)){
				ss.setDeserializer(serial);
			}else{
				ss.setDeserializer("javaDeserializer");
			}
			config.getServers().add(ss);
			logger.info("Success to add remote server " + ss +" into system.");
		}
	}
	
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

	}

	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		for (Server ss : config.getServers()) {
			getTcpClientConnectionFactoryBeanDefinition(registry, ss);
			getTcpOutboundGatewayBeanDefinition(registry, ss);
			getChannelBeanDefinition(registry, ss);
		}
	}
	
	public void getTcpClientConnectionFactoryBeanDefinition(BeanDefinitionRegistry registry, Server ss){
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(TcpConnectionFactoryFactoryBean.class);
		builder.addConstructorArgValue("client");
		builder.addPropertyValue("host", ss.getIp());
		builder.addPropertyValue("port", ss.getPort());
		builder.addPropertyValue("soTimeout", ss.getSotimeout());
		builder.addPropertyValue("singleUse", "false");
		builder.addPropertyValue("usingNio", "true");
		builder.addPropertyValue("soTcpNoDelay", "true");
		builder.addPropertyReference("serializer", ss.getSerializer());
		builder.addPropertyReference("deserializer", ss.getDeserializer());
		AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
		BeanDefinitionReaderUtils.registerBeanDefinition(new BeanComponentDefinition(beanDefinition, "clientConnectionFactory-" + ss.getId()), registry);
	}
	
	public void getTcpOutboundGatewayBeanDefinition(BeanDefinitionRegistry registry, Server ss){
		BeanDefinitionBuilder handlerBuilder = BeanDefinitionBuilder.genericBeanDefinition(TcpOutboundGateway.class);
		handlerBuilder.addPropertyReference("connectionFactory", "clientConnectionFactory-" + ss.getId());
		handlerBuilder.addPropertyValue("requestTimeout", ss.getRequestTimeout());
		handlerBuilder.addPropertyValue("remoteTimeout", ss.getRemoteTimeout());
		
		AbstractBeanDefinition handlerBeanDefinition = handlerBuilder.getBeanDefinition();
		String handlerBeanName = BeanDefinitionReaderUtils.generateBeanName(handlerBeanDefinition, registry);
		String[] handlerAlias  = new String[] {"tcpOutGateway-" + ss.getId() + IntegrationConfigUtils.HANDLER_ALIAS_SUFFIX};
		BeanDefinitionReaderUtils.registerBeanDefinition(new BeanComponentDefinition(handlerBeanDefinition, handlerBeanName, handlerAlias), registry);
		
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(ConsumerEndpointFactoryBean.class);
		builder.addPropertyValue("inputChannelName", "input-" + ss.getId());
		builder.addPropertyReference("handler", handlerBeanName);
		
		AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
		BeanDefinitionReaderUtils.registerBeanDefinition(new BeanComponentDefinition(beanDefinition, "tcpOutGateway-" + ss.getId()), registry);
	}
	
	public void getChannelBeanDefinition(BeanDefinitionRegistry registry, Server ss){
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(DirectChannel.class);
		AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
		BeanDefinitionReaderUtils.registerBeanDefinition(new BeanComponentDefinition(beanDefinition, "input-" + ss.getId()), registry);
	}
	
	/***************************************/
	/**         TCP Server                **/
	/***************************************/
	
    @Bean
    public TcpInboundGateway tcpInGate(AbstractServerConnectionFactory connectionFactory)  {
        TcpInboundGateway inGate = new TcpInboundGateway();
        inGate.setConnectionFactory(connectionFactory);
        inGate.setRequestChannelName("toSA");
        return inGate;
    }
	
	@Bean
    public MessageChannel toSA() {
        return new DirectChannel();
    }
	
	@Bean
    public AbstractServerConnectionFactory serverCF() {
		TcpNetServerConnectionFactory tcp = new TcpNetServerConnectionFactory(4196);
//		tcp.setSingleUse(true);
		tcp.setSerializer(javaSerializer());
		tcp.setDeserializer(javaDeserializer());
		return tcp;
		
    }
}

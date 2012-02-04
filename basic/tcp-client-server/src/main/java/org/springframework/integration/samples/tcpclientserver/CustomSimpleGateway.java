package org.springframework.integration.samples.tcpclientserver;

/**
 * @author: ceposta
 */
public interface CustomSimpleGateway {

    public CustomOrder send(String text);
}

package com.armzilla.ha;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Created by arm on 9/12/15.
 */
@Configuration
public class TomcatConnectorBean {
    
	private final static Logger logger = LoggerFactory.getLogger(TomcatConnectorBean.class);
	
	@Value("${emulator.portbase}")
    private int portBase;
    
    @Value("${emulator.portcount}")
    private int portCount;
    
    @Bean
    public TomcatServletWebServerFactory servletContainer() {
    	
    	TomcatServletWebServerFactory tomcat = null;
        for(int i = 0; i < portCount; i ++) {
            if(tomcat == null){
                tomcat = new TomcatServletWebServerFactory(portBase + i);
            }else{
                tomcat.addAdditionalTomcatConnectors(createConnector(portBase + i));
                logger.debug("Created additional Tomcat on port " + (portBase + i));
            }
        }
        return tomcat;
    }

    private Connector createConnector(int portNumber) {
    	
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        //Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler(); // ???
        connector.setScheme("http");
        connector.setPort(portNumber);
        return connector;
    }
}

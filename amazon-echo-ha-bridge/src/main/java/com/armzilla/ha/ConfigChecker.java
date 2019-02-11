package com.armzilla.ha;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by arm on 9/16/16.
 */
@Component
public class ConfigChecker {
	
	private final static Logger logger = LoggerFactory.getLogger(ConfigChecker.class);

    @Value("${upnp.config.address}")
    private String responseAddress;

    @Autowired
    private ApplicationContext appContext;

	private boolean appConfigFailed;
    
    @EventListener(ApplicationReadyEvent.class)
    @Order(0) // This should run before any other application event listeners!
    public void checkConfigParameters() {
    	
        if(responseAddress == null || responseAddress.isEmpty() ){
            logConfigurationProblem("Please provide the IP(v4) address of the interface you want the bridge to listen on using --upnp.config.address=<ipadress>");
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    @Order(Integer.MAX_VALUE) // This should run after any other application event listeners!
    public void shutdownAppIfConfigFailed() {

    	if (appConfigFailed) {
    		SpringApplication.exit(appContext);
    	}
    }

    public boolean isConfigurationCorrect() {
    	return !appConfigFailed;
    }
    
	private void logConfigurationProblem(String message) {
		
		logger.error("==[ Configuration Failure! ]==========================================================");
		logger.error(message);
		logger.error("======================================================================================");
		appConfigFailed = true;
	}
}

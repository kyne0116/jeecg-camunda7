package org.jeecg.boot.camunda7;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@SpringBootApplication
public class JeecgApplication {

  @Autowired
  private Environment environment;

  public static void main(String... args) {
    SpringApplication.run(JeecgApplication.class, args);
  }

  @EventListener(ApplicationReadyEvent.class)
  public void onApplicationReady() {
    String port = environment.getProperty("server.port", "8080");
    String contextPath = environment.getProperty("server.servlet.context-path", "");
    String protocol = environment.getProperty("server.ssl.key-store") != null ? "https" : "http";
    String applicationName = environment.getProperty("spring.application.name", "Application");
    String camundaAdminUser = environment.getProperty("camunda.bpm.admin-user.id", "admin");
    String camundaAdminPassword = environment.getProperty("camunda.bpm.admin-user.password", "admin");
    
    String localIp = getLocalIpAddress();
    String baseUrl = protocol + "://localhost:" + port + contextPath;
    String externalUrl = protocol + "://" + localIp + ":" + port + contextPath;
    
    String separator = "=".repeat(80);
    
    log.info("\n{}", separator);
    log.info("{} started successfully!", applicationName);
    log.info("{}", separator);
    log.info("Local Access URL: {}", baseUrl);
    if (!localIp.equals("127.0.0.1")) {
      log.info("External Access URL: {}", externalUrl);
    }
    log.info("Camunda Admin Console: {}/camunda/app/cockpit/", baseUrl);
    log.info("Camunda Admin User: {} / {}", camundaAdminUser, camundaAdminPassword);
    log.info("Camunda REST API: {}/engine-rest/", baseUrl);
    log.info("Profile: {}", String.join(",", environment.getActiveProfiles()));
    log.info("{}", separator);
  }

  private String getLocalIpAddress() {
    try {
      return InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      log.warn("Unable to determine local IP address, using localhost");
      return "127.0.0.1";
    }
  }

}
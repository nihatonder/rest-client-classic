package com.nonder.rest_app;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class Main {
    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        int port = getPort();
        tomcat.setPort(port);
        tomcat.getConnector(); // Needed to create the default connector

        // 1. Create a minimal Tomcat context, avoiding extra scanning
        Context tomcatContext = tomcat.addContext("", null);

        // 2. Create the Spring Web Application Context from our XML
        XmlWebApplicationContext appContext = new XmlWebApplicationContext();
        appContext.setConfigLocation("classpath:applicationContext.xml");

        // 3. Create Spring's DispatcherServlet
        DispatcherServlet dispatcherServlet = new DispatcherServlet(appContext);

        // 4. Register the servlet and force it to initialize on startup
        var wrapper = Tomcat.addServlet(tomcatContext, "dispatcher", dispatcherServlet);
        wrapper.setLoadOnStartup(1); // This is crucial!
        wrapper.addMapping("/");   // Map it to handle all incoming requests

        System.out.println("Starting embedded Tomcat server on port " + port);
        tomcat.start();
        tomcat.getServer().await();
    }

    private static int getPort() {
        String port = System.getenv("PORT");
        if (port == null || port.isEmpty()) {
            return 8081; // Default port
        }
        return Integer.parseInt(port);
    }
}
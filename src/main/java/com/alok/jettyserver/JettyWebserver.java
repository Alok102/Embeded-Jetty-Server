package com.alok.jettyserver;

import com.alok.jettyserver.resource.HelloResource;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public class JettyWebserver {

    public static void main(String[] args) throws Exception{
        System.setProperty("javax.net.debug", "all");
        JettyWebserver jetty = new JettyWebserver();
        jetty.start();
    }

    void start() throws Exception {
        ServletContextHandler handler = buildUsingResourceConfig(); // or buildUsingInitParameter()
        Server server = new Server();
        server.setHandler(handler);
        // HTTP Configuration
        HttpConfiguration http = new HttpConfiguration();

        SecureRequestCustomizer src = new SecureRequestCustomizer();
        src.setSniHostCheck(false);
        http.addCustomizer(src);

        // Configuration for HTTPS redirect
        http.setSecurePort(8443);
        http.setSecureScheme("https");
        ServerConnector connector = new ServerConnector(server);
        connector.addConnectionFactory(new HttpConnectionFactory(http));
        // Setting HTTP port
       // connector.setPort(8080);

        // HTTPS configuration
//        HttpConfiguration https = new HttpConfiguration();
//        https.addCustomizer(new SecureRequestCustomizer(false));

        // Configuring SSL
        SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();

        // Defining keystore path and passwords
        sslContextFactory.setKeyStorePath("C:\\Users\\Alok_K\\OneDrive - Dell Technologies\\Desktop\\SSL\\server.keystore");
        sslContextFactory.setKeyStorePassword("Alok102");
        sslContextFactory.setKeyManagerPassword("Alok102");
        sslContextFactory.setWantClientAuth(true);

        // Configuring the connector
        ServerConnector sslConnector = new ServerConnector(server, new SslConnectionFactory(sslContextFactory, "http/1.1"), new HttpConnectionFactory(http));
        sslConnector.setPort(8443);

        // Setting HTTP and HTTPS connectors
        server.setConnectors(new Connector[]{connector, sslConnector});

        // Starting the Server
        server.start();
        server.join();
    }
    static ServletContextHandler buildUsingResourceConfig() {
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        handler.setContextPath("/");

        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(HelloResource.class);
        resourceConfig.register(HelloResource.GreetingMessageBodyWriter.class);
        handler.addServlet(new ServletHolder(new ServletContainer(resourceConfig)), "/api/*");
        return handler;
    }

}

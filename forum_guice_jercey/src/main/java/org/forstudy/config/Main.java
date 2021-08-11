package org.forstudy.config;

import com.google.inject.servlet.GuiceFilter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.forstudy.listener.GuiceListener;

import java.util.EnumSet;

public class Main {

    public static void main(String[] args) throws Exception {
        Server jettyServer = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(
                jettyServer, "/*", ServletContextHandler.SESSIONS);
        context.addEventListener(new GuiceListener());
        context.addFilter(GuiceFilter.class, "/*",
                EnumSet.of(javax.servlet.DispatcherType.REQUEST,
                        javax.servlet.DispatcherType.ASYNC));
        context.addServlet(DefaultServlet.class, "/*");
        try {
            jettyServer.start();
            jettyServer.join();
        }
        finally {
            jettyServer.destroy();
        }
    }
}

package org.forstudy.listener;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.forstudy.controllers.GroupController;
import org.forstudy.controllers.PostController;
import org.forstudy.controllers.TopicController;
import org.forstudy.controllers.UserController;
import org.forstudy.exceptionhandling.AppExceptionMapper;

import javax.servlet.annotation.WebListener;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import java.util.HashMap;

@WebListener
public class GuiceListener extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {


        Injector injector = Guice.createInjector(new JerseyServletModule() {
            @Override
            protected void configureServlets() {
                bind(GroupController.class);
                bind(UserController.class);
                bind(TopicController.class);
                bind(PostController.class);

                bind(MessageBodyReader.class).to(JacksonJsonProvider.class);
                bind(MessageBodyWriter.class).to(JacksonJsonProvider.class);
                bind(AppExceptionMapper.class);

                HashMap<String, String> options = new HashMap<String, String>();
                options.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
                serve("/*").with(GuiceContainer.class, options);
            }
        }, new JpaPersistModule("db_manager"));
        injector.getInstance(JpaInitializer.class);
        return injector;
    }
}

class JpaInitializer {
    @Inject
    public JpaInitializer(PersistService persistService) {
        persistService.start();
    }
}
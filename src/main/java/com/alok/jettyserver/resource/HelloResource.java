package com.alok.jettyserver.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

@Path("/hello")
public class HelloResource {

    public static class Greeting {
        private String message;

        public Greeting() {
        }

        public Greeting(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    @GET
    @Path("/{param}")
    @Produces(MediaType.APPLICATION_JSON)
    public Greeting hello(@PathParam("param") String name) {
        return new Greeting("Hello! Jetty Server is serving " + name);
    }

    @Provider
    @Produces(MediaType.TEXT_PLAIN)
    public static class GreetingMessageBodyWriter implements MessageBodyWriter<Greeting> {

        @Override
        public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
            return type == Greeting.class;
        }

        @Override
        public void writeTo(Greeting greeting, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream out) throws IOException, WebApplicationException {
            out.write(greeting.getMessage().getBytes(StandardCharsets.UTF_8));
            out.flush();
        }
    }

    @GET
    @Path("/{param}.txt")
    @Produces(MediaType.TEXT_PLAIN)
    public Greeting helloText(@PathParam("param") String name) {
        return new Greeting("Hello " + name);
    }
}

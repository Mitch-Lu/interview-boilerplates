package com.getconvey.interview.resources;


import com.getconvey.interview.model.Message;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Path("/helloworld")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HelloWorldResource {

    private final String messageText;

    public HelloWorldResource(String messageText) {
        this.messageText = messageText;
    }

    @GET
    public Response hello() {
        try {
//            StringUtils.countMatches()
            final java.nio.file.Path path = Paths.get(messageText);
            final BufferedReader reader = Files.newBufferedReader(path);
            reader.lines().forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final Message message = new Message(messageText);

        return Response.ok(message, MediaType.APPLICATION_JSON).build();
    }
}

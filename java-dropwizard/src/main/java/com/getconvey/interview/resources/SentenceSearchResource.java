package com.getconvey.interview.resources;

import com.getconvey.interview.model.Message;
import com.getconvey.interview.model.Sentence;
import com.getconvey.interview.model.SentenceSearch;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/sentences")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SentenceSearchResource {
    private final String resourcePath;

    public SentenceSearchResource(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    @GET
    @Path("/count")
    public Response count(@QueryParam("word") Optional<String> queryParamWord) {
        try {
            final String word;
            if (queryParamWord.isPresent()) {
                word = queryParamWord.get();
                if (word.isBlank()) {
                    throw new BadRequestException("QueryParam: word cannot be empty.");
                }
            } else {
                throw new BadRequestException("QueryParam: word is required for count.");
            }

            final List<Sentence> sentences = Files.newBufferedReader(Paths.get(resourcePath)).lines()
                .map(line -> {
                    final int count = StringUtils.countMatches(line.toLowerCase(), word.toLowerCase());
                    return new Sentence(line, count);
                })
                .sorted(Comparator.comparingInt(Sentence::getCount).thenComparing(Sentence::getSentence))
                .collect(Collectors.toList());
            return Response.ok(new SentenceSearch(word, sentences), MediaType.APPLICATION_JSON).build();
        } catch (NoSuchFileException e) {
            throw new NotFoundException("File NOT FOUND: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new InternalServerErrorException("Cannot load resource: " + e.getMessage(), e);
        }
    }
}

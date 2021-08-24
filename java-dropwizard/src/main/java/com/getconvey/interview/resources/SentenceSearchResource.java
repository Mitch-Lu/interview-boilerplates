package com.getconvey.interview.resources;

import com.getconvey.interview.model.Sentence;
import com.getconvey.interview.model.SentenceSearch;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                .flatMap(line -> {
                    final int wordCount = getWordCount(word, line);
                    if (wordCount > 0) {
                        return Stream.of(new Sentence(line, wordCount));
                    } else {
                        return Stream.empty();
                    }
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

    private int getWordCount(String word, String line) {
        int wordCount = 0;
        final StringTokenizer tokenizer = new StringTokenizer(
            line.replaceAll("[^a-zA-Z0-9]", StringUtils.SPACE),
            StringUtils.SPACE);
        while (tokenizer.hasMoreTokens()) {
            final String token = tokenizer.nextToken();
            if (StringUtils.equalsIgnoreCase(token, word)) {
                wordCount++;
            }
        }
        return wordCount;
    }
}

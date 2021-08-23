package com.getconvey.interview;

import com.getconvey.interview.configuration.AppConfiguration;
import com.getconvey.interview.model.SentenceSearch;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Happy path integration test.
 */
public class AppIT {
    @ClassRule
    public static final DropwizardAppRule<AppConfiguration> RULE_1 =
        new DropwizardAppRule<>(App.class, ResourceHelpers.resourceFilePath("it.yaml"));
    @ClassRule
    public static final DropwizardAppRule<AppConfiguration> RULE_2 =
        new DropwizardAppRule<>(App.class, ResourceHelpers.resourceFilePath("bad.yaml"));

    private static Client client_1;
    private static Client client_2;

    @BeforeClass
    public static void init() {
        client_1 = RULE_1.client();
        client_2 = RULE_2.client();
    }

    @Test
    public void wordCountOnFile02() {
        assertThat(RULE_1.getConfiguration().getFilePath()).isEqualTo("src/main/resources/sentences-02.txt");
        final String queryParam = "eggplant";
        final Response response = client_1.target(
                String.format("http://localhost:%d/sentences/count", RULE_1.getLocalPort()))
            .queryParam("word", queryParam)
            .request()
            .get();

        assertThat(response.getStatus()).isEqualTo(200);
        final SentenceSearch resp = response.readEntity(SentenceSearch.class);
        assertThat(resp.getWord()).isEqualTo(queryParam);
        assertThat(resp.getSentences().size()).isEqualTo(5);
        assertThat(resp.getSentences().get(0).getCount()).isEqualTo(1);
        assertThat(resp.getSentences().get(1).getCount()).isEqualTo(2);
        assertThat(resp.getSentences().get(4).getCount()).isEqualTo(3);
    }

    @Test()
    public void missingQueryParam() {
        final Response response = client_1.target(
                String.format("http://localhost:%d/sentences/count", RULE_1.getLocalPort()))
            .request()
            .get();

        // Bad Request
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test()
    public void emptyQueryParam() {
        final String queryParam = "";
        final Response response = client_1.target(
                String.format("http://localhost:%d/sentences/count", RULE_1.getLocalPort()))
            .queryParam("word", queryParam)
            .request()
            .get();

        // Bad Request
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test()
    public void fileNotFound() {
        assertThat(RULE_2.getConfiguration().getFilePath()).isEqualTo("src/main/resources/sentences-04.txt");
        final String queryParam = "eggplant";
        final Response response = client_2.target(
                String.format("http://localhost:%d/sentences/count", RULE_2.getLocalPort()))
            .queryParam("word", queryParam)
            .request()
            .get();

        // Not Found
        assertThat(response.getStatus()).isEqualTo(404);
    }

}

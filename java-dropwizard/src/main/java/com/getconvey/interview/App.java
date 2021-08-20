package com.getconvey.interview;

import com.getconvey.interview.configuration.AppConfiguration;
import com.getconvey.interview.resources.AppHealthCheck;
import com.getconvey.interview.resources.SentenceSearchResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class App extends Application<AppConfiguration> {

    public static void main(String[] args) throws Exception {
        new App().run(args);
    }

    public void run(AppConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().register(new SentenceSearchResource(configuration.getFilePath()));
        environment.healthChecks().register("AppHealthCheck", new AppHealthCheck());
    }
}

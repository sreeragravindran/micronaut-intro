package helloworld;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import javax.inject.Inject;

@MicronautTest
public class HelloworldTest {

//    @Inject
//    EmbeddedApplication application;

    @Inject
    @Client("/")
    HttpClient client;

//    @Test
//    void testItWorks() {
//        Assertions.assertTrue(application.isRunning());
//    }

    @Test
    public void test_example(){
        HttpRequest<String> request = HttpRequest.GET("/hello/sreerag");
        HttpResponse response = client.toBlocking().exchange(request);

        Assertions.assertTrue(response.status().equals(HttpStatus.OK));
    }
}

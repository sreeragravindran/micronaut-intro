package helloworld.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.netty.DefaultHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.RxHttpClient;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

import javax.inject.Inject;
import java.net.URL;

@Controller("/hello")
public class HelloController {

    @Inject
    RxHttpClient httpClient;

    @Get("/{name}")
    public Maybe<String> hello(String name) throws Exception{
        System.out.println("entering controller");

        // the below call gets executed in IO thread pool
//        return httpClient.retrieve("http://localhost:8000/delay/2000").firstElement();

        // back to event loop
        System.out.println("exiting controller");
        return Maybe.just("Hello " + name + "!");
    }

}
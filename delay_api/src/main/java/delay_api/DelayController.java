package delay_api;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.netty.channel.EventLoopGroup;
import io.reactivex.Maybe;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

@Controller("/delay")
public class DelayController {

    @Inject
    EventLoopGroup eventLoopGroup;

    @Inject
    @Named(TaskExecutors.IO)
    ExecutorService ioExecutor; // lets thread count be controlled from config

    @Get("/event-loop/{value}")
    public String get_blocking_event_loop(int value) {
        System.out.println("entering controller");
        String result = get_data(value).get();
        System.out.println("exiting controller");
        return result;
    }

    @ExecuteOn(TaskExecutors.IO)
    @Get("/io/{value}")
    public String get_from_io_thread_pool(int value) {
        System.out.println("entering controller");
        String result = get_data(value).get();
        System.out.println("exiting controller");
        return result;
    }

    @Get("/reactive/{value}")
    public Single<String> offload_to_reactive_io_schedular(int value) {
        System.out.println("entering controller");

        // offload blocking operation to IO thread schedular
        Single<String> result = Single.just(1).subscribeOn(Schedulers.io())
                //.map(i -> cpu_intensive())
                .map(i -> get_data(value).get()); // blocking operation

        System.out.println("exiting controller");
        return result;
    }


    //offloading the call to the database to the IO thread pool,
    // but then continues on the event loop to create the response body.
    @Get("/reactive-advanced/{value}")
    public Single<Map<String, Object>> switch_to_io_pool_and_back_to_event_loop(int value) {
        System.out.println( "entering controller on thread: " + Thread.currentThread().getId());
        return Single.fromCallable(() -> {
            // This will be executed on the IO thread pool
            // simulating data retreival from database
            return get_data(value).get();
        })
        .subscribeOn(Schedulers.from(ioExecutor))
        .observeOn(Schedulers.from(eventLoopGroup))
        .map(i -> {
            // This will be executed on the netty event loop
              System.out.println("Creating response body on thread: "+ Thread.currentThread().getId());
              Map<String, Object> data = new LinkedHashMap<>(2);
              data.put("id", 1);
              data.put("title", "Ivory Throne");
              return data;
        });
//
    }



    public String cpu_intensive(){
        System.out.println("executing cpu intensive task on thread: "+ Thread.currentThread().getId());
        int x=0;
        for(long i =0; i < 500000000; i++){
            for(long j =0; j < 10; j++){
                x++;
            }
        }
        System.out.println("finished execution on thread :" + Thread.currentThread().getId());
        return "done" ;
    }

    public Supplier<String> get_data(int value) {
        return () -> {
            latency(value);
            return "result";
        };
    }

    public void latency(int value){
        try {
            System.out.println("fetching data on thread: "+ Thread.currentThread().getId());
            Thread.sleep(value);
            System.out.println("fetched data on thread: "+ Thread.currentThread().getId());
        } catch(Exception ex){
        }
    }
}

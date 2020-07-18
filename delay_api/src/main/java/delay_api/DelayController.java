package delay_api;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.reactivex.Maybe;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import java.util.function.Supplier;

@Controller("/delay")
public class DelayController {

    @Get("/{value}")
    public Single<String> get(int value) {
        System.out.println("entering controller");

        // offload cpu intensive to another to another thread
        Single<String> result = Single.just(1).subscribeOn(Schedulers.io())
                //.map(i -> cpu_intensive())
                .map(i -> get_data(value).get());

        System.out.println("exiting controller");
        return result;
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

package the.wuxjian.consumer.demo;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * Created by wuxjian 2021/7/22
 */
public class Demon {
    public static void main(String[] args) {
        Flux.just("tom")
                .map(s -> {
                    System.out.println("[map] Thread name: " + Thread.currentThread().getName());
                    return s.concat("@mail.com");
                })
                .publishOn(Schedulers.newElastic("thread-publishOn"))
                .filter(s -> {
                    System.out.println("[filter] Thread name: " + Thread.currentThread().getName());
                    return s.startsWith("t");
                })
                .subscribeOn(Schedulers.newElastic("thread-subscribeOn"))
                .subscribe(s -> {
                    System.out.println("[subscribe] Thread name: " + Thread.currentThread().getName());
                    System.out.println(s);
                });
    }
}

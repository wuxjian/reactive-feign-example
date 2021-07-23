package the.wuxjian.producer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Created by wuxjian 2021/7/21
 */
@RestController
@Slf4j
public class HelloController {

    @GetMapping("/hello")
    public Mono<String> hello() {
        log.info("调用consumer接口");
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        return Mono.delay(Duration.ofSeconds(5))
//                .then(Mono.just("hello"));
        return Mono.just("hello");
    }
}

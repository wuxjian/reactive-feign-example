package the.wuxjian.producer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Created by wuxjian 2021/7/21
 */
@RestController
public class HelloController {

    @GetMapping("/hello/{name}")
    public Mono<String> hello(@PathVariable String name) {
        return Mono.just(name);
    }
}

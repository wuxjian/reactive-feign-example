package the.wuxjian.consumer.service;

import org.springframework.web.bind.annotation.GetMapping;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

/**
 * Created by wuxjian 2021/7/21
 */
@ReactiveFeignClient(name = "producer")
public interface ConsumerService {

    @GetMapping("/hello")
    Mono<String> hello();
}

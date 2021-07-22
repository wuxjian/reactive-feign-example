package the.wuxjian.consumer.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import the.wuxjian.consumer.service.ConsumerService;

/**
 * Created by wuxjian 2021/7/21
 */
@RestController
public class ConsumerController {

    @Autowired
    private ConsumerService consumerService;

    @GetMapping("/hello")
    public Mono<String> hello() {
        return consumerService.hello();
    }
}

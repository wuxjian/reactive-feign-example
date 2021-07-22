package the.wuxjian.consumer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactivefeign.ReactiveOptions;
import reactivefeign.retry.BasicReactiveRetryPolicy;
import reactivefeign.spring.config.ReactiveRetryPolicies;
import reactivefeign.webclient.WebReactiveOptions;

/**
 * Created by wuxjian 2021/7/20
 */
@Configuration
public class ReactiveFeignConfig {

    //设置一些超时时间
    @Bean
    public ReactiveOptions reactiveOptions() {
        return new WebReactiveOptions.Builder()
                .setWriteTimeoutMillis(10000)
                .setReadTimeoutMillis(1000)
                .setConnectTimeoutMillis(1000)
                .build();
    }

    //重试机制
    @Bean
    public ReactiveRetryPolicies retryOnNext() {
        //不进行重试，retryOnSame是控制对同一个实例的重试策略，retryOnNext是控制对不同实例的重试策略。
        return new ReactiveRetryPolicies.Builder()
                .retryOnSame(BasicReactiveRetryPolicy.retryWithBackoff(0, 10))
                .retryOnNext(BasicReactiveRetryPolicy.retryWithBackoff(1, 10))
                .build();
    }

}

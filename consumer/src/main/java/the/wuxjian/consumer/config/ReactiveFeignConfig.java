package the.wuxjian.consumer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactivefeign.ReactiveOptions;
import reactivefeign.webclient.WebReactiveOptions;

/**
 * Created by wuxjian 2021/7/23
 */
@Configuration
public class ReactiveFeignConfig {

    @Bean
    public ReactiveOptions.Builder optionsBuilder() {
        return new WebReactiveOptions.Builder()
                .setReadTimeoutMillis(5 * 1000) //5秒

                ;
    }
}

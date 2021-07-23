package the.wuxjian.rabbitmq;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Delivery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by wuxjian 2021/7/22
 */
@SpringBootApplication
@Slf4j
public class RabbitMqApplication {

    static final String QUEUE = "reactor.rabbitmq.spring.boot";
    @Autowired
    Mono<Connection> connectionMono;
    @Autowired
    AmqpAdmin amqpAdmin;

    public static void main(String[] args) {
        SpringApplication.run(RabbitMqApplication.class, args);
    }

    // the mono for connection, it is cached to re-use the connection across sender and receiver instances
    // this should work properly in most cases
    @Bean()
    Mono<Connection> connectionMono(RabbitProperties rabbitProperties) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(rabbitProperties.getHost());
        connectionFactory.setPort(rabbitProperties.getPort());
        connectionFactory.setUsername(rabbitProperties.getUsername());
        connectionFactory.setPassword(rabbitProperties.getPassword());
        return Mono.fromCallable(() -> connectionFactory.newConnection("reactor-rabbit")).cache();
    }

    @Bean
    Sender sender(Mono<Connection> connectionMono) {
        return RabbitFlux.createSender(new SenderOptions().connectionMono(connectionMono));
    }

    @Bean
    Receiver receiver(Mono<Connection> connectionMono) {
        return RabbitFlux.createReceiver(new ReceiverOptions().connectionMono(connectionMono));
    }

    @Bean
    Flux<Delivery> deliveryFlux(Receiver receiver) {
        return receiver.consumeNoAck(QUEUE);
    }

    @PostConstruct
    public void init() {
        amqpAdmin.declareQueue(new Queue(QUEUE, false, false, true));
    }

    @PreDestroy
    public void close() throws Exception {
        connectionMono.block().close();
    }

    // a runner that publishes messages with the sender bean and consumes them with the receiver bean
    @Component
    static class Runner implements CommandLineRunner {

        final Sender sender;
        final Flux<Delivery> deliveryFlux;
        final AtomicBoolean latchCompleted = new AtomicBoolean(false);

        Runner(Sender sender, Flux<Delivery> deliveryFlux) {
            this.sender = sender;
            this.deliveryFlux = deliveryFlux;
        }

        @Override
        public void run(String... args) throws Exception {
            int messageCount = 10;
            CountDownLatch latch = new CountDownLatch(messageCount);
            deliveryFlux.subscribe(m -> {
                log.info("Received message {}", new String(m.getBody()));
                latch.countDown();
            });
            log.info("Sending messages...");
            sender.send(Flux.range(1, messageCount).map(i -> new OutboundMessage("", QUEUE, ("Message_" + i).getBytes())))
                    .subscribe();
            latchCompleted.set(latch.await(5, TimeUnit.SECONDS));
        }

    }

}

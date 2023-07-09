package com.odeyalo.sonata.account.service.event.source;

import com.odeyalo.sonata.suite.brokers.events.SonataEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;


@Component
public class KafkaEventSource implements ReactiveSonataEventSource {
    private final KafkaReceiver<String, SonataEvent> kafkaReceiver;

    public KafkaEventSource(KafkaReceiver<String, SonataEvent> kafkaReceiver) {
        this.kafkaReceiver = kafkaReceiver;
    }

    @Override
    public Flux<? extends SonataEvent> getEvents() {
        return kafkaReceiver.receive()
                .map(ConsumerRecord::value)
                .doOnError((err) -> System.out.println("ERROR!"));
    }
}
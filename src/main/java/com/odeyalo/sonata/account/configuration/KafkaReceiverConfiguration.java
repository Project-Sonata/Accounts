package com.odeyalo.sonata.account.configuration;

import com.odeyalo.sonata.suite.brokers.events.SonataEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOffset;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverPartition;
import reactor.kafka.receiver.internals.ConsumerFactory;
import reactor.kafka.receiver.internals.DefaultKafkaReceiver;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Configuration
public class KafkaReceiverConfiguration {
    private static final String TOPIC = "USER_REGISTERED_TOPIC";
    private static final String BOOTSTRAP_SERVERS = "localhost:29092";
    private static final String CLIENT_ID_CONFIG = "account-service-listener";
    private static final String GROUP_ID_CONFIG = "listeners";

    @Bean
    public KafkaReceiver<String, SonataEvent> kafkaReceiver(){

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, CLIENT_ID_CONFIG);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID_CONFIG);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);

        JsonDeserializer<Object> deserializer = new JsonDeserializer<>();
        deserializer.addTrustedPackages("*");

        return new DefaultKafkaReceiver(ConsumerFactory.INSTANCE, ReceiverOptions.create(props)
                .subscription(Collections.singleton(TOPIC))
                .addAssignListener((partitions -> {
                    for (ReceiverPartition partition : partitions) {
                        partition.seekToEnd();
                    }
                }))
                .withValueDeserializer(deserializer)
//                .subscription(Pattern.compile("(.*?)"))
        );
    }
}

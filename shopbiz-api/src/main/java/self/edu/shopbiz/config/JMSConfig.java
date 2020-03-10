package self.edu.shopbiz.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import self.edu.shopbiz.util.AppConstants;

/**
 * Created by mp on 11/15/19.
 */


@Configuration
public class JMSConfig {


    // create an AMQP queue
    @Bean
    Queue queue() {
        return new Queue(AppConstants.SHOPBIZ_MESSAGE_QUEUE, false);
    }

    // create an exchange
    @Bean
    TopicExchange exchange() {
        return new TopicExchange(AppConstants.TOPIC_EXCHANGE_NAME);
    }

    // bind queue and exchange
    // any message sent with this routing key are routed to the queue
    // so exchange will route messages to proper queue
    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(AppConstants.SHOPBIZ_MESSAGE_QUEUE);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

//    @Bean
//    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }
}

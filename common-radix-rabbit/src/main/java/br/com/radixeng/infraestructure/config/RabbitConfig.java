package br.com.radixeng.infraestructure.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

	@Bean
	public Queue queue(RabbitProperties props) {
		return new Queue(props.getQueueName(), true);
	}
}

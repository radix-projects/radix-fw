package br.com.radixeng.infraestructure.sender;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.radixeng.infraestructure.Message;

@Component
public class RabbitSender<T> {
	@Autowired
    private RabbitTemplate rabbitTemplate;
 
    @Autowired
    private Queue queue;
 
    public void send(T object) {
        rabbitTemplate.convertAndSend(this.queue.getName(), new Message<>(object));
    }
}

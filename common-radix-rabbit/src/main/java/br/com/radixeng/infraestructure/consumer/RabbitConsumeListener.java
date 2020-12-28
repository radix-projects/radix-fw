package br.com.radixeng.infraestructure.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import br.com.radixeng.infraestructure.Message;
import lombok.extern.java.Log;

@Log
@Component
public class RabbitConsumeListener {
	
	@RabbitListener(queues = {"teste"})
    public void receive(@Payload Message<?> payload) {
       log.info("Payload recebido: " + payload == null ? "" : payload.toString());
    }
}

package br.com.radixeng.infraestructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "radix.rabbit")
public class RabbitProperties {
	@Getter @Setter
	private String queueName;
}

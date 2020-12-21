# radix-fw

## Material de estudo Stach Spring, como objetivos de absorver conhecimento de algumas ferramentas.

* Spring Boot
* Kafka / RabbitMq
* Splunk
* New Relic
* Docker 
* Kubernetes
* Oracle
* Mongo
* Redis


## Dowlonad do Radix Fw
```bash
  $ git clone git@github.com:radix-projects/radix-fw.git
  $ cd radix-fw
  $ mvn clean install
```

## Modulos:
### Kafka
O modulo kafka tem como objetivo abstrair feature do Spring-kafka e facilitar o uso.

### Pequena configuracao 
#### Adicionar no seu application.properties do seu micro-service referências do bootstrapServers e groupID que se encontram no KafkaProducerConfig e KafkaConsumerConfig.
```java
    @Value("${config.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${config.kafka.group-id}")
    private String groupID;
```
#### Em seu application.properties do micro-service
```properties
    config.kafka.bootstrap-servers=localhost:9092
    config.kafka.group-id=myGroup
```

### Exemplo de como escrever um topico.
#### Necessario habilitar o Kafka em sua aplicacao:

```java
   @EnableKafka
   @SpringBootApplication(scanBasePackages = "com.radix")
   public class ProcessaVendasApplication {
    
        public static void main(String[] args) {
            SpringApplication.run(ProcessaVendasApplication.class, args);
        }
   }
```
#### Em seguidda usar KafkaDispatcher, escrever em um especifico topico.
```java
    @Log
    @Service
    public class MyService {

        private final KafkaDispatcher kafkaDispatcher;

        public MyService(KafkaDispatcher kafkaDispatcher) {
            this.kafkaDispatcher = kafkaDispatcher;
        }

        void action(MyObject myObject) {
          kafkaDispatcher.send("TOPIC_NEW_SERVICE", myObject.getEmail(), new CorrelationId(MyService.class.getSimpleName()), myObject);
        }

    }
```

### Exemplo de como ler um topico.
#### Necessário importar KafkaConsumerConfig e habilitar o Kafka em sua aplicacao:

```java
   @Import(KafkaConsumerConfig.class)
   @EnableKafka
   @SpringBootApplication(scanBasePackages = "com.radix")
   public class ProcessaVendasApplication {
    
        public static void main(String[] args) {
            SpringApplication.run(ProcessaVendasApplication.class, args);
        }
   }
```
#### Em seguidda usar a notacao @KafkaListener passando o topico
```java
    @Log
    @Service
    public class MyService {

        @KafkaListener(topics = "TOPIC_NEW_SERVICE")
        void listener(Message message) {
            log.info(String.format("Listener %s", message));
            //....
        }

    }
```

[Para mais detalhes segue documentação](https://spring.io/projects/spring-kafka)






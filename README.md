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
Adicionar no seu application.properties do seu micro-service referências do bootstrapServers e groupID que se encontram no KafkaProducerConfig e KafkaConsumerConfig.
```java
    @Value("${config.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${config.kafka.group-id}")
    private String groupID;
```
Em seu application.properties do micro-service
```properties
    config.kafka.bootstrap-servers=localhost:9092
    config.kafka.group-id=myGroup
```

Exemplo de como escrever um topico.
Necessario habilitar o Kafka em sua aplicacao:

```java
   @EnableKafka
   @SpringBootApplication(scanBasePackages = "com.radix")
   public class ProcessaVendasApplication {
    
        public static void main(String[] args) {
            SpringApplication.run(ProcessaVendasApplication.class, args);
        }
   }
```
Em seguidda usar KafkaDispatcher, escrever em um especifico topico.
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

Exemplo de como ler um topico.
Necessário importar KafkaConsumerConfig e habilitar o Kafka em sua aplicacao:

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
Em seguidda usar a notacao @KafkaListener passando o topico
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

* [Para mais detalhes segue documentação](https://spring.io/projects/spring-kafka)

### MongoDB
O modulo MongoDb tem como objetivo abstrair feature do spring-boot-starter-data-mongodb e facilitar o uso.

Habilite em seu projeto @EnableMongoRepositories passando no basePackages como padrao "com.radix.infrastructure.persistence.mongo"
```java
  @EnableMongoRepositories(basePackages = {"com.radix.infrastructure.persistence.mongo"})
  @SpringBootApplication(scanBasePackages = "com.radix")
   public class ProcessaVendasApplication {
        public static void main(String[] args) {
            SpringApplication.run(ProcessaVendasApplication.class, args);
        }
   }
```
Crie o seu entity neste diretorio "com.radix.infrastructure.persistence.mongo.myentity"
```java
    package com.radix.infrastructure.persistence.mongo.venda;

    //...

    @Document(value = "myentitys")
    public class MyEntity {

        @Id
        private String id;
        private Long client;
        private String state;

        //getter and setter ...
    }
```
Crie no mesmo diretorio "com.radix.infrastructure.persistence.mongo.myentity" o repository referente ao seu entity 
```java
    package com.radix.infrastructure.persistence.mongo.venda;
    
    //...
    
    public interface MyEntityRepository extends MongoRepository<MyEntity, String>, QuerydslPredicateExecutor<MyEntity> {
    
        @Query("{ 'client' : ?0 }")
        VendaEntity findUsersByCliente(Long client);
    
        @Query("{ 'state' : ?0 }")
        List<VendaEntity> findUsersByStatus(String state);
    
    }
```
Note que na interface MyEntityRepository esta habilitado o queryDsl passando o pojo QuerydslPredicateExecutor<MyEntity>, para Habilitar adicione em seu arquivo pom.xml este plugin
```xml
    <build>
        <plugins>
            <plugin>
                <groupId>com.mysema.maven</groupId>
                <artifactId>apt-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
```

* [Para mais detalhes segue documentação](https://spring.io/projects/spring-data-mongodb)
* [Segue documentação sobre QueryDsl](http://www.querydsl.com/) 

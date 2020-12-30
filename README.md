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

Incluir em sua dependência maven o modulo common-radix-kafka.

```xml
    <dependency>
        <groupId>com.radix</groupId>
        <artifactId>common-radix-kafka</artifactId>
    </dependency>
```

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

Em seguidda usar a notacao @KafkaListener passando o topico.

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

---
### MongoDB
O modulo MongoDb tem como objetivo abstrair feature do spring-boot-starter-data-mongodb e facilitar o uso.

Incluir em sua dependência maven o modulo common-radix-mongo.

```xml
    <dependency>
        <groupId>com.radix</groupId>
        <artifactId>common-radix-mongo</artifactId>
    </dependency>
```

Habilite em seu projeto @EnableMongoRepositories passando no basePackages como padrao "com.radix.infrastructure.persistence.mongo".

```java
  @EnableMongoRepositories(basePackages = {"com.radix.infrastructure.persistence.mongo"})
  @SpringBootApplication(scanBasePackages = "com.radix")
   public class ProcessaVendasApplication {
        public static void main(String[] args) {
            SpringApplication.run(ProcessaVendasApplication.class, args);
        }
   }
```

Incluir em sua aplicação.

```properties
spring.data.mongodb.uri=mongodb://usuario:senha@localhost:27017/nomebanco?AuthMechanism=SCRAM-SHA-256&authSource=admin
spring.data.mongodb.database=nomebanco
```

Crie o seu entity neste diretorio "com.radix.infrastructure.persistence.mongo.myentity".

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
Crie no mesmo diretorio "com.radix.infrastructure.persistence.mongo.myentity" o repository referente ao seu entity.

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
Note que na interface MyEntityRepository esta habilitado o queryDsl passando o pojo QuerydslPredicateExecutor<MyEntity>, para Habilitar adicione em seu arquivo pom.xml este plugin.
```xml
<build>
    <plugins>
        <plugin>
            <groupId>com.mysema.maven</groupId>
            <artifactId>apt-maven-plugin</artifactId>
            <version>${mysema.maven.version}</version>
            <executions>
                <execution>
                    <goals>
                        <goal>process</goal>
                    </goals>
                    <configuration>
                        <outputDirectory>target/generated-sources/java</outputDirectory>
                        <processor>org.springframework.data.mongodb.repository.support.MongoAnnotationProcessor</processor>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

Suba o servidor do mongodb, no diretorio radix-fw/docker-compose.yml adicionamos a imagem do mongodb. Apenas rode os comandos abaixo.

```yml
    mongo:
    image: mongo
    networks:
    - network-net
    environment:
    MONGO_INITDB_ROOT_USERNAME: root
    MONGO_INITDB_ROOT_PASSWORD: MongoDB2021!
    ports:
    - "27017:27017"
```
```bash
    $ docker-compose down
    $ docker-compose up -d
```

* [Para mais detalhes segue documentação](https://spring.io/projects/spring-data-mongodb)
* [Segue documentação sobre QueryDsl](http://www.querydsl.com/) 
* [Segue documentação sobre QueryDsl - Integração MongoDB](http://www.querydsl.com/static/querydsl/4.4.0/reference/html_single/#mongodb_integration) 

### New Relic

Para utilizar o New Relic deve-se seguir o passo a passo disponível na documentação do 
[New Relic](https://docs.newrelic.com/docs/logs/enable-log-management-new-relic/logs-context-java/java-configure-javautillogging)

- Para fazer o cadastro gratuito no [site](https://newrelic.com/signup).
- Adicionar o arquivo newrelic.yml gerado no site com seu cadastro no diretório ./common-radix-new-relic/newrelic.yml
- Adicionar no POM do projeto a dependência, para download do agent:
```xml
        <dependency>
            <groupId>com.newrelic.agent.java</groupId>
            <artifactId>newrelic-java</artifactId>
            <version>${new.relic.agent.version}</version>
            <scope>provided</scope>
            <type>zip</type>
        </dependency>
```
- Adicionar no POM do projeto a dependência, para integração do New Relic com os logs:
```xml
        <dependency>
            <groupId>com.newrelic.logging</groupId>
            <artifactId>jul</artifactId>
            <version>2.0</version>
        </dependency>
```
- Configurar o log do java para utilizar a integração com New Relic, adicionando o arquivo logging.properties
```properties
com.newrelic.logging.jul.NewRelicMemoryHandler.target = java.util.logging.FileHandler
java.util.logging.FileHandler.formatter = com.newrelic.logging.jul.NewRelicFormatter
handlers = com.newrelic.logging.jul.NewRelicMemoryHandler
```
- Adicionar plugin para extrair agent e adicionar ao classpath
```xml
    ...
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-dependency-plugin</artifactId>
                <executions>
                    <execution>
                        <id>unpack-newrelic</id>
                        <phase>package</phase>
                        <goals>
                            <goal>unpack-dependencies</goal>
                        </goals>
                        <configuration>
                            <includeGroupIds>com.newrelic.agent.java</includeGroupIds>
                            <includeArtifactIds>newrelic-java</includeArtifactIds>
                            <excludes>**/newrelic.yml</excludes>
                            <overWriteReleases>false</overWriteReleases>
                            <overWriteSnapshots>false</overWriteSnapshots>
                            <overWriteIfNewer>true</overWriteIfNewer>
                            <outputDirectory>${project.build.directory}</outputDirectory>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
    ...
```
- Adicionar plugin para copiar arquivo newrelic.yml que está na raiz do projeto
```xml
    ...
            <plugin>
                <artifactId>maven-resources-plugin</artifactId>
                <executions>
                    <execution>
                        <id>copy-resources</id>
                        <phase>validate</phase>
                        <goals>
                            <goal>copy-resources</goal>
                        </goals>
                        <configuration>
                            <outputDirectory>${basedir}/target/newrelic/</outputDirectory>
                            <overwrite>true</overwrite>
                            <resources>
                                <resource>
                                    <directory>${basedir}</directory>
                                    <includes>
                                        <include>newrelic.yml</include>
                                    </includes>
                                    <filtering>true</filtering>
                                </resource>
                            </resources>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
    ...
```
- Adicionar no POM do projeto a dependência, para integração do New Relic com os logs:
```xml
        <dependency>
            <groupId>com.newrelic.logging</groupId>
            <artifactId>jul</artifactId>
            <version>2.0</version>
        </dependency>
```
- Para executar a aplicação é necessário adicionar o agent do NewRelic, podendo ser feito de diferentes formas, de acordo com servidor de execução (foi levado em consideração uma aplicação com spring boot, para outros servidores acessar [documentação](https://docs.newrelic.com/docs/agents/java-agent/installation/include-java-agent-jvm-argument)):

    - Linha de comando (JVM Args):
    ```bash
    $ java -javaagent:/full/path/to/newrelic.jar -jar app.jar
    ```
    - Configurando no POM, adicionar o plugin
    ```xml
        ...
            <plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
				<configuration>
					<addResources>true</addResources>
					<jvmArguments>-javaagent:${project.build.directory}/newrelic/newrelic.jar</jvmArguments>
				</configuration>
            </plugin>
        ...
    ```

---
### Oracle
O modulo Oracle tem como objetivo abstrair feature do spring-boot-starter-data-jpa e facilitar o uso.

Incluir em sua dependência maven o modulo common-radix-oracle.

```xml
    <dependency>
        <groupId>com.radix</groupId>
        <artifactId>common-radix-oracle</artifactId>
    </dependency>
```

Incluir no seu application.properties.

```properties
# Oracle settings
spring.oracle.datasource.url=jdbc:oracle:thin:@localhost:1521:ORCLCDB
spring.oracle.datasource.username=sys as sysdba
spring.oracle.datasource.password=Oradoc_db1
spring.oracle.datasource.driver-class-name=oracle.jdbc.driver.OracleDriver

# Jpa settings - opcional
spring.jpa.hibernate.format_sql=true
spring.jpa.show-sql=true
```

Crie seu repository no pacote especificado "com.radix.infrastructure.persistence".

```java 
package com.radix.infrastructure.persistence.company;

// imports...

public interface CompanyRepository extends JpaRepository<CompanyEntity, Long>, CompanyRepositoryQueryDsl {

    Optional<CompanyEntity> findByName(String name);
    Optional<CompanyEntity> findByIdAndName(Long id, String name);
}
```

Exemplo de herança múltipla usando queryDsl.

```java 
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long>, CompanyRepositoryQueryDsl
```

Crie interface CompanyRepositoryQueryDsl exemplo.

```java 
package com.radix.infrastructure.persistence.company.querydsl;

// imports...

public interface CompanyRepositoryQueryDsl {
    Optional<CompanyEntity> queryFindByIdAndName(Long id, String name);
}
```

Em seguida implemente CompanyRepositoryQueryDsl. 

```java 
package com.radix.infrastructure.persistence.company.querydsl;

// imports...

public class CompanyRepositoryQueryDslImpl implements CompanyRepositoryQueryDsl {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<CompanyEntity> queryFindByIdAndName(Long id, String name) {
        Optional<CompanyEntity> companyOptional = Optional.empty();

        if (Objects.nonNull(id) && StringUtils.isNotBlank(name)) {

           QCompanyEntity qCompany = QCompanyEntity.companyEntity;
            JPAQuery query = (JPAQuery) new JPAQuery(entityManager)
                                            .from(qCompany)
                                            .where(qCompany.id.eq(id)
                                            .and(qCompany.name.containsIgnoreCase(name)));
            companyOptional = Optional.ofNullable((CompanyEntity) query.fetchOne());
        }

        return  companyOptional;
    }
```
* [Segue projeto de exmeplo](https://github.com/radix-projects/app-company)
* [Para mais detalhes segue documentação](https://spring.io/projects/spring-data-jpa)
* [Segue documentação sobre QueryDsl](http://www.querydsl.com/static/querydsl/4.4.0/reference/html_single/#jpa_integration) 

### Redis
O modulo Redis tem como objetivo abstrair feature do spring-boot-starter-data-redis, spring-boot-starter-cache, redis.clients e facilitar o uso.

Incluir em sua dependência maven o modulo common-radix-redis.

```xml
    <dependency>
        <groupId>com.radix</groupId>
        <artifactId>common-radix-redis</artifactId>
    </dependency>
```

Incluir no seu application.properties.

```properties
# redis
spring.cache.type=redis
spring.data.redis.repositories.enabled=false
cache.host=localhost
cache.port=6379
cache.password=password
cache.timeout=60
cache.cacheExpirations.Companies=5
```

Adicionar annotation @EnableCaching

```java 
package com.radix;

//imports ...

@EnableCaching
@SpringBootApplication(scanBasePackages = "com.radix")
public class CompanyApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompanyApplication.class, args);
	}

}
```

Em sua camada service incluir anotações de cache.

```java 
package com.radix.domain.company;

//imports ...

@Service
public class CompanyService {

    //...

    @CacheEvict(cacheNames = Company.CACHE_NAME, allEntries = true)
    public Company create(final Company company) {
        CompanyEntity companyEntity = this.companyRepository.save(companyConverter.toEntity(company));
        return companyConverter.toDomain(companyEntity);
    }

    @Cacheable(cacheNames = Company.CACHE_NAME, key="#id")
    public Company findById(final Long id) {
        return companyConverter.toDomain(findByIdOrElseThrow(id));
    }

    @Cacheable(cacheNames = Company.CACHE_NAME)
    public List<Company> findAll() {
        return companyConverter.toCollectionDomain(this.companyRepository.findAll());
    }


    @Caching(evict = {
            @CacheEvict(cacheNames = Company.CACHE_NAME, key="#id"),
            @CacheEvict(cacheNames = Company.CACHE_NAME, key = "T(org.springframework.cache.interceptor.SimpleKey).EMPTY")
    })
    public void delete(Long id) {

        try {

            companyRepository.deleteById(id);

        } catch (EmptyResultDataAccessException e) {
            throw new CompanyNotFoundException(id);

        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(String.format(MSG_COMPANY_IN_USE, id));
        }
    }

    @Caching(
            put = {@CachePut(cacheNames = Company.CACHE_NAME, key="#company.getId()")},
            evict = {@CacheEvict(cacheNames = Company.CACHE_NAME, key = "T(org.springframework.cache.interceptor.SimpleKey).EMPTY")}
    )
    public Company update(final Company company) {
        CompanyEntity companyEntity = findByIdOrElseThrow(company.getId());
        companyEntity.setName(company.getName());
        return companyConverter.toDomain(companyRepository.saveAndFlush(companyEntity));
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = Company.CACHE_NAME, key="{#id, #name}"),
            @CacheEvict(cacheNames = Company.CACHE_NAME, key = "T(org.springframework.cache.interceptor.SimpleKey).EMPTY")
    })
    public Optional<Company> findByIdAndName(Long id, String name) {
        AtomicReference<Optional<Company>> companyOptional = new AtomicReference<>(Optional.empty());
        Optional<CompanyEntity> companyEntityOptional = this.companyRepository.findByIdAndName(id, name);
        companyEntityOptional.ifPresent(companyEntity -> companyOptional.set(Optional.ofNullable(companyConverter.toDomain(companyEntity))));
        return companyOptional.get();
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = Company.CACHE_NAME, key="{#id, #name}"),
            @CacheEvict(cacheNames = Company.CACHE_NAME, key = "T(org.springframework.cache.interceptor.SimpleKey).EMPTY")
    })
    public Optional<Company> queryFindByIdAndName(Long id, String name) {
        AtomicReference<Optional<Company>> companyOptional = new AtomicReference<>(Optional.empty());
        Optional<CompanyEntity> companyEntityOptional = this.companyRepository.queryFindByIdAndName(id, name);
        companyEntityOptional.ifPresent(companyEntity -> companyOptional.set(Optional.ofNullable(companyConverter.toDomain(companyEntity))));
        return companyOptional.get();
    }

}
```

* [Segue projeto de exmeplo](https://github.com/radix-projects/app-company)
* [Para mais detalhes segue documentação](https://spring.io/projects/spring-data-redis)

# GUMGA FRAMEWORK BACKEND

O GUMGA FRAMEWORK BACKEND possibilita a criação de APIs JAVA com diversas funcionalidades comuns a sistemas que utilizam bancos de dados relacionais. Utiliza outros frameworks como SPRING, HIBERNATE, JACKSON, ...

## Getting Started

O framework da gumga ~~está~~ estará disponível através de repositório no mavencentral e para utilizá-lo recomenda-se a criação do projeto via archetype da gumga que cria a estrutura básica de um projeto.
```shell
mvn archetype:generate -DinteractiveMode=false -DarchetypeGroupId=br.com.gumga  -DarchetypeArtifactId=projeto-archetype  -DgroupId=br.com.gumgademo  -DartifactId=exemplo -Dversion=0.1
```


![archetype](https://github.com/GUMGA/framework-backend/blob/master/docs/img/archetype.gif)



### Prerequisites

Para utilização do framework é necessário JAVA e MAVEN. Você pode verificar a instalação destes através dos comandos 
```
java -version
mvn -v
```
![archetype](https://github.com/GUMGA/framework-backend/blob/master/docs/img/javamvn.gif)


### Installing

[Para instalar o JAVA em seu sistema.](http://www.oracle.com/technetwork/pt/java/javase/downloads/index.html)

[maven.apache](https://maven.apache.org/)


## Running the tests

Para executar os testes automatizados do framework basta executar o comando.

```
mvn test
```
![archetype](https://github.com/GUMGA/framework-backend/blob/master/docs/img/testframewok.gif)

## Deployment

Para fazer deploy de aplicações criadas com o framwork, utilize os arquivos WAR presentes nas pastas target dos módulos api e presentation do projeto.

```
./exemplo/exemplo-api/target/exemplo-api.war
./exemplo/exemplo-presentation/target/exemplo.war
```


## Built With
[INFO] --- maven-dependency-plugin:2.1:tree (default-cli) @ exemplo-api ---
[INFO] br.com.gumgademo:exemplo-api:war:0.1
[INFO] +- br.com.gumgademo:exemplo-application:jar:0.1:compile
[INFO] |  +- br.com.gumgademo:exemplo-domain:jar:0.1:compile
[INFO] |  |  \- org.hibernate:hibernate-search-orm:jar:5.1.0.Final:compile
[INFO] |  |     \- org.hibernate:hibernate-search-engine:jar:5.1.0.Final:compile
[INFO] |  |        +- org.apache.lucene:lucene-core:jar:4.10.4:compile
[INFO] |  |        +- org.apache.lucene:lucene-analyzers-common:jar:4.10.4:compile
[INFO] |  |        \- org.apache.lucene:lucene-facet:jar:4.10.4:compile
[INFO] |  |           \- org.apache.lucene:lucene-queries:jar:4.10.4:compile
[INFO] |  \- br.com.gumgademo:exemplo-infrastructure:jar:0.1:compile
[INFO] |     +- gumga.framework:gumga-application:jar:1.2.16:compile
[INFO] |     |  +- org.springframework:spring-context-support:jar:4.2.6.RELEASE:compile
[INFO] |     |  +- javax.mail:mail:jar:1.4.7:compile
[INFO] |     |  |  \- javax.activation:activation:jar:1.1:compile
[INFO] |     |  +- org.jasypt:jasypt:jar:lite:1.9.2:compile
[INFO] |     |  +- org.freemarker:freemarker:jar:2.3.23:compile
[INFO] |     |  +- net.sf.jasperreports:jasperreports:jar:6.2.0:compile
[INFO] |     |  |  +- commons-beanutils:commons-beanutils:jar:1.9.0:compile
[INFO] |     |  |  +- commons-collections:commons-collections:jar:3.2.1:compile
[INFO] |     |  |  +- commons-digester:commons-digester:jar:2.1:compile
[INFO] |     |  |  +- com.lowagie:itext:jar:2.1.7.js4:compile
[INFO] |     |  |  |  +- bouncycastle:bcmail-jdk14:jar:138:compile
[INFO] |     |  |  |  +- bouncycastle:bcprov-jdk14:jar:138:compile
[INFO] |     |  |  |  \- org.bouncycastle:bctsp-jdk14:jar:1.38:compile
[INFO] |     |  |  |     +- org.bouncycastle:bcprov-jdk14:jar:1.38:compile
[INFO] |     |  |  |     \- org.bouncycastle:bcmail-jdk14:jar:1.38:compile
[INFO] |     |  |  +- org.jfree:jcommon:jar:1.0.23:compile
[INFO] |     |  |  +- org.jfree:jfreechart:jar:1.0.19:compile
[INFO] |     |  |  +- org.eclipse.jdt.core.compiler:ecj:jar:4.3.1:compile
[INFO] |     |  |  +- org.codehaus.castor:castor-xml:jar:1.3.3:compile
[INFO] |     |  |  |  +- org.codehaus.castor:castor-core:jar:1.3.3:compile
[INFO] |     |  |  |  +- commons-lang:commons-lang:jar:2.6:compile
[INFO] |     |  |  |  +- stax:stax:jar:1.2.0:compile
[INFO] |     |  |  |  |  \- stax:stax-api:jar:1.0.1:compile
[INFO] |     |  |  |  \- javax.xml.stream:stax-api:jar:1.0-2:compile
[INFO] |     |  |  +- org.olap4j:olap4j:jar:0.9.7.309-JS-3:compile
[INFO] |     |  |  \- com.google.zxing:core:jar:2.3.0:compile
[INFO] |     |  +- org.cogroo.lang.pt_br:cogroo-ann-pt_br:jar:4.0.0:compile
[INFO] |     |  |  +- org.cogroo:cogroo-ann:jar:4.0.0:compile
[INFO] |     |  |  |  +- org.cogroo:cogroo-nlp:jar:4.0.0:compile
[INFO] |     |  |  |  |  +- org.apache.opennlp:opennlp-tools:jar:1.5.3:compile
[INFO] |     |  |  |  |  |  +- org.apache.opennlp:opennlp-maxent:jar:3.0.3:compile
[INFO] |     |  |  |  |  |  \- net.sf.jwordnet:jwnl:jar:1.3.3:compile
[INFO] |     |  |  |  |  +- javax.xml.bind:jaxb-api:jar:2.2.5:compile
[INFO] |     |  |  |  |  +- org.carrot2:morfologik-fsa:jar:1.5.3:compile
[INFO] |     |  |  |  |  |  \- com.carrotsearch:hppc:jar:0.4.1:compile
[INFO] |     |  |  |  |  +- org.carrot2:morfologik-stemming:jar:1.5.2:compile
[INFO] |     |  |  |  |  \- org.carrot2:morfologik-tools:jar:1.5.2:compile
[INFO] |     |  |  |  |     +- org.carrot2:morfologik-polish:jar:1.5.2:compile
[INFO] |     |  |  |  |     \- commons-cli:commons-cli:jar:1.2:compile
[INFO] |     |  |  |  \- log4j:log4j:jar:1.2.17-atlassian-1:compile
[INFO] |     |  |  \- org.cogroo.lang.pt_br:cogroo-res-pt_br:jar:4.0.0:compile
[INFO] |     |  \- org.reflections:reflections:jar:0.9.10:compile
[INFO] |     |     \- com.google.code.findbugs:annotations:jar:2.0.1:compile
[INFO] |     +- gumga.framework:gumga-security:jar:1.2.16:compile
[INFO] |     |  \- org.springframework:spring-web:jar:4.2.6.RELEASE:compile
[INFO] |     +- commons-fileupload:commons-fileupload:jar:1.3.1:compile
[INFO] |     |  \- commons-io:commons-io:jar:2.4:compile (version managed from 2.2)
[INFO] |     +- com.zaxxer:HikariCP:jar:1.3.8:compile
[INFO] |     +- org.springframework:spring-webmvc:jar:4.2.6.RELEASE:compile
[INFO] |     +- com.fasterxml.jackson.datatype:jackson-datatype-hibernate4:jar:2.3.2:compile
[INFO] |     +- com.fasterxml.jackson.datatype:jackson-datatype-joda:jar:2.3.2:compile
[INFO] |     |  \- joda-time:joda-time:jar:2.1:compile
[INFO] |     +- com.h2database:h2:jar:1.4.181:compile
[INFO] |     \- mysql:mysql-connector-java:jar:5.1.33:compile
[INFO] +- br.com.gumgademo:exemplo-gateway:jar:0.1:compile
[INFO] |  \- gumga.framework:gumga-presentation:jar:1.2.16:compile
[INFO] |     +- org.springframework:spring-aop:jar:4.2.6.RELEASE:compile
[INFO] |     |  \- aopalliance:aopalliance:jar:1.0:compile
[INFO] |     +- javax.validation:validation-api:jar:1.1.0.Final:compile
[INFO] |     +- javax.el:el-api:jar:2.2:compile
[INFO] |     +- io.springfox:springfox-swagger2:jar:2.0.1:compile
[INFO] |     |  +- org.mapstruct:mapstruct:jar:1.0.0.Beta4:compile
[INFO] |     |  +- com.wordnik:swagger-annotations:jar:1.5.3-M1:compile
[INFO] |     |  +- com.wordnik:swagger-models:jar:1.5.3-M1:compile
[INFO] |     |  +- io.springfox:springfox-spi:jar:2.0.1:compile
[INFO] |     |  |  \- io.springfox:springfox-core:jar:2.0.1:compile
[INFO] |     |  +- io.springfox:springfox-schema:jar:2.0.1:compile
[INFO] |     |  +- io.springfox:springfox-swagger-common:jar:2.0.1:compile
[INFO] |     |  +- io.springfox:springfox-spring-web:jar:2.0.1:compile
[INFO] |     |  +- org.springframework.plugin:spring-plugin-core:jar:1.2.0.RELEASE:compile
[INFO] |     |  +- org.springframework.plugin:spring-plugin-metadata:jar:1.2.0.RELEASE:compile
[INFO] |     |  \- org.springframework.hateoas:spring-hateoas:jar:0.17.0.RELEASE:compile
[INFO] |     |     \- org.objenesis:objenesis:jar:2.1:compile
[INFO] |     \- org.ajar:swagger-spring-mvc-ui:jar:0.4:compile
[INFO] \- gumga.framework:gumga-domain:jar:1.2.16:compile
[INFO]    +- br.com.insula:opes:jar:1.4.0:compile
[INFO]    |  +- com.google.guava:guava:jar:11.0.2:compile
[INFO]    |  |  \- com.google.code.findbugs:jsr305:jar:1.3.9:compile
[INFO]    |  \- org.slf4j:slf4j-api:jar:1.6.1:compile
[INFO]    +- org.hibernate:hibernate-core:jar:4.3.1.Final:compile (version managed from 4.3.8.Final)
[INFO]    |  +- org.jboss.logging:jboss-logging:jar:3.1.3.GA:compile
[INFO]    |  +- org.jboss.logging:jboss-logging-annotations:jar:1.2.0.Beta1:compile
[INFO]    |  +- org.jboss.spec.javax.transaction:jboss-transaction-api_1.2_spec:jar:1.0.0.Final:compile
[INFO]    |  +- dom4j:dom4j:jar:1.6.1:compile
[INFO]    |  |  \- xml-apis:xml-apis:jar:1.0.b2:compile
[INFO]    |  +- org.hibernate.common:hibernate-commons-annotations:jar:4.0.4.Final:compile
[INFO]    |  +- org.hibernate.javax.persistence:hibernate-jpa-2.1-api:jar:1.0.0.Final:compile
[INFO]    |  +- org.javassist:javassist:jar:3.18.1-GA:compile
[INFO]    |  +- antlr:antlr:jar:2.7.7:compile
[INFO]    |  \- org.jboss:jandex:jar:1.1.0.Final:compile
[INFO]    +- gumga.framework:gumga-core:jar:1.2.16:compile
[INFO]    |  +- org.slf4j:slf4j-simple:jar:1.7.6:compile
[INFO]    |  \- junit:junit:jar:4.11:test (scope managed from compile)
[INFO]    |     \- org.hamcrest:hamcrest-core:jar:1.3:test
[INFO]    +- org.springframework:spring-tx:jar:4.2.6.RELEASE:compile (version managed from 4.0.9.RELEASE)
[INFO]    |  +- org.springframework:spring-beans:jar:4.2.6.RELEASE:compile
[INFO]    |  \- org.springframework:spring-core:jar:4.2.6.RELEASE:compile
[INFO]    |     \- commons-logging:commons-logging:jar:1.2:compile
[INFO]    +- org.springframework:spring-context:jar:4.2.6.RELEASE:compile (version managed from 4.0.9.RELEASE)
[INFO]    |  \- org.springframework:spring-expression:jar:4.2.6.RELEASE:compile
[INFO]    +- net.jodah:typetools:jar:0.3.1:compile
[INFO]    +- gumga.framework:gumga-validation:jar:1.2.16:compile
[INFO]    |  \- org.mockito:mockito-all:jar:1.9.5:compile
[INFO]    +- com.mysema.querydsl:querydsl-apt:jar:3.4.1:compile (version managed from 3.4.0)
[INFO]    |  \- com.mysema.querydsl:querydsl-codegen:jar:3.4.1:compile
[INFO]    |     +- com.mysema.codegen:codegen:jar:0.6.2:compile
[INFO]    |     \- javax.inject:javax.inject:jar:1:compile
[INFO]    +- com.mysema.querydsl:querydsl-jpa:jar:3.4.0:compile
[INFO]    |  \- com.mysema.querydsl:querydsl-core:jar:3.4.0:compile
[INFO]    |     +- com.mysema.commons:mysema-commons-lang:jar:0.2.4:compile
[INFO]    |     +- cglib:cglib:jar:2.2.2:compile
[INFO]    |     |  \- asm:asm:jar:3.3.1:compile
[INFO]    |     \- com.infradna.tool:bridge-method-annotation:jar:1.11:compile
[INFO]    +- org.apache.commons:commons-lang3:jar:3.3:compile
[INFO]    +- org.springframework.data:spring-data-jpa:jar:1.7.2.RELEASE:compile
[INFO]    |  +- org.springframework.data:spring-data-commons:jar:1.9.2.RELEASE:compile
[INFO]    |  +- org.springframework:spring-orm:jar:4.2.6.RELEASE:compile (version managed from 4.0.9.RELEASE)
[INFO]    |  |  \- org.springframework:spring-jdbc:jar:4.2.6.RELEASE:compile
[INFO]    |  +- org.aspectj:aspectjrt:jar:1.8.4:compile
[INFO]    |  \- org.slf4j:jcl-over-slf4j:jar:1.7.10:runtime
[INFO]    +- org.hibernate:hibernate-validator:jar:5.1.0.Final:compile
[INFO]    |  \- com.fasterxml:classmate:jar:1.0.0:compile
[INFO]    +- org.hibernate:hibernate-envers:jar:4.3.1.Final:compile
[INFO]    |  \- org.hibernate:hibernate-entitymanager:jar:4.3.1.Final:compile
[INFO]    \- com.fasterxml.jackson.core:jackson-databind:jar:2.3.2:compile
[INFO]       +- com.fasterxml.jackson.core:jackson-annotations:jar:2.3.0:compile
[INFO]       \- com.fasterxml.jackson.core:jackson-core:jar:2.3.2:compile
[INFO]                                                                         


## Contributing
 
[Para contribuir, utilize o pull requests do github.](https://help.github.com/articles/about-pull-requests/)

## Versioning

Utilizamos [SemVer](http://semver.org/)  [Tags](https://github.com/gumga/framework-backend/tags). 

## Authors

* **Munif Gebara Junior** - *Documentação incial* - [Munif](http://www.munif.com.br)

## License

MIT License - [LICENSE.md](https://opensource.org/licenses/MIT) 


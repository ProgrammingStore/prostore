# Prostore - простой Event sourcing + CQRS фреймворк
На данный момент на рынке мало Event sourcing + CQRS фреймворков.
А то что есть непопулярны и имеют слабую поддержку.
Поэтому многие создают свои in-house решения.
Данный проект может служить примером при создании собственного решения.

## Требования
- Java >= 17
- Apache maven
- Mongodb
- Kafka

## Состав проекта
- `prostore-core` - ядро, основные абстракции
- `prostore-eureka` - имплементация CommandBus на основе Spring Cloud Eureka
- `prostore-mongo` - имплементация EventStore на Mongodb
- `prostore-kafka` - имплементация EventBus на основе Apache Kafka
- `prostore-test-common` - общая библиотека для тестовых проектов
- `prostore-test-service` - тестовый проект "сервис"
- `prostore-test-client` - тестовый проект "клиент"
- `spring-boot-prostore-starter` - основной стартер
- `spring-boot-prostore-eureka-starter` - стартер для eureka
- `spring-boot-prostore-mongo-starter` - стартер для mongo
- `spring-boot-prostore-kafka-starter` - стартер для kafka
- `prostore-eureka-server` - eureka server

При создании собственного решения Event sourcing важно уделить внимание на гибкость.
Не стоит внедрять жесткие зависимости.
Возможно для каких-то случаев вам понадобится заменить базу, например на SQL.
В данном проекте был выбран Mongodb, как наиболее оптимальный для хранения event-ов.
Другие возможные варианты:
- Apache Cassandra
- Redis
- PostgreSQL/MySQL

Не советую выбирать Apache Kafka в качестве имплементации EventStore.

## Запуск тестового проекта
- Запустите локально mongodb
- Запустите локально kafka
- `mvn clean install`
- `mvn spring-boot:run -f prostore-eureka-server`
- `PORT=9000 mvn spring-boot:run -f prostore-test-client`
- `PORT=9001 mvn spring-boot:run -f prostore-test-service`
- `PORT=9002 mvn spring-boot:run -f prostore-test-service`

## Тестирование
Отправьте тестовые запросы на `prostore-test-client`:
- Создание shipment (в ответ получите aggregateId)
  ````
  curl -v -H "Content-Type: application/json" \
       -d '{"destination":"Moscow", "location": "Almaty"}' \
       http://localhost:9000/shipment
  ````
- Получение shipment (вместо AGGREGATE_ID вставьте aggregateId полученный командой создания)
  ````
  curl -v -X GET -H "Content-Type: application/json" \
       -d '{"aggregateId": "AGGREGATE_ID"}' \
       http://localhost:9000/shipment
  ````
- Изменение shipment (вместо AGGREGATE_ID вставьте aggregateId полученный командой создания)
  ````
  curl -v -H "Content-Type: application/json" \
  -d '{"aggregateId":"AGGREGATE_ID", "location": "Sydney"}' \
  http://localhost:9000/shipment/move 
  ````
- Если запущены несколько инстансов `prostore-test-service`, запросы будут балансироваться по round-robin
- Попробуйте перезапустить сервисы. При старте сервиса будет запущен replay event-ов для восстановления аггрегатов 

Notes
---
Все event-ы хранятся в коллекции `MongoEvent` (база `test` по умолчанию)
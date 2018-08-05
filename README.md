## PaymentService
service related to payments

## Getting Started
Dependencies: git, docker, gradle, kafka, and Intellj IDE.

Get postgresql from docker. 
``` bash
docker pull postgres

# run the image
docker run --name jack_postgres -e POSTGRES_PASSWORD=Root1027 -p 5432:5432 postgres
```

Get kafka. For MacOS user
```bash
brew install kafka

# run zookeeper
zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties &
kafka-server-start /usr/local/etc/kafka/server.properties

# run broker
/usr/local/Cellar/kafka/1.1.0/libexec/bin/kafka-console-producer.sh --broker-list localhost:9092 --topic orders "parse.key=true" --property "key.separator=:"
```

Get the source code
```bash
git clone https://github.com/DuduShopping/PaymentService.git
cd PaymentService
```

Open PaymentService with Intellj <br>
Hit refresh gradle <br>
Hit run button <br>
Enjoy hacking







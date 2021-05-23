# Quickstart Guide
URL shortener service

Language: Java

DB: Redis

## Running Requirements
* Docker
* Docker-Compose

## Build Requirements
* JAVA 8
* Maven
* Docker

## Running
```bash
git clone 

cd shorturl

docker-compose up
```

New Url:
``` shell
curl --location --request POST 'http://127.0.0.1:8080/newurl' \
--header 'Content-Type: application/json' \
--data-raw '{
    "url": "https://www.google.com"
}'

Reuslt example:
{"url":"https://www.google.com","shortenUrl":"http://127.0.0.1:8080/2q3Rktog"}
```


Test short url:
``` shell
curl -s -f -I -L http://127.0.0.1:8080/2q3Rktog
```

## Build
Image build using Jib， https://cloud.google.com/java/getting-started/jib?hl=zh-cn

```shell

cd shorturl

#Build and push the image to a container registry
mvn compile jib:build

#Docker Local Build
mvn compile jib:dockerBuild
```

## System Design

Character range： [0-9][a-z][A-B] 

Short Url Length: 8

Short Url Total Size: 64^8

Single Redis supports Short Url Size： 2^32/2

生成算法：規則基於自增序列，將10進制序列轉換為62進制的數值。

自增序列實現： Redis incr command (https://redis.io/commands/incr)

Short url 映射數據存儲在 Redis 中，這樣可以輕鬆支撐 1W+ TPS，同時是這樣也帶來了數據丟失的風險

###  High availability
#### Redis

在 Redis 單實例性能足夠的情況，建議使用 Redis Sentinel

https://redis.io/topics/sentinel

#### Application

Use Nginx for load balancing

### Scalability

#### performance
性能的主要瓶頸來自 Redis,理想情況下,單 Redis 可以支持 TPS 如下:
* Newurl: 2.5w/s
* Redirect: 5W/s

性能擴展的主要目標是 Redis，建議使用 Redis Cluster(https://redis.io/topics/cluster-tutorial)
#### Capacity

Need to be supported by Load Test results

Note: Focus on Redis memory

### Persistence

請在生產開啟 Redis RDB + AOF，以保證更好的持久化效果
（https://redis.io/topics/persistence）




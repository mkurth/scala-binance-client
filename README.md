# scala-binance-client
Pure Scala Client Lib to interact with binance

## usage
until i can manage to deploy my artifacts to maven-central, we're stuck to a local publish
`sbt publishLocal`

`libraryDependencies += "com.mkurth" %%% "scala-binance-client" % "0.1.0-SNAPSHOT"`

```scala

val auth = BinanceAuth("myApiKey", "mySecretButProbablyFromAnEnv")
val client = new BinanceClient(auth)
client.exchangeInfo
client.account

```

## hints for js
if you plan to use this lib pure client side you will need a proxy to forward your
request, because modern browsers will block your request.

```scala
val auth = BinanceAuth(apiKey, apiSecret)
val client = new BinanceClient(auth, Some("http://localhost:9090"))
```

you could use the following nginx conf:

```
worker_processes  2;
user              www-data;

events {
  use           epoll;
  worker_connections  128;
}

http {
  server_tokens off;
  include       mime.types;
  charset       utf-8;

  server {
    listen 80;
    listen [::]:80;

    server_name testProxy;

    location / {
      add_header 'Access-Control-Allow-Origin' 'http://localhost:8080';
      add_header 'Access-Control-Allow_Credentials' 'true';
      add_header 'Access-Control-Allow-Headers' 'X-MBX-APIKEY,Authorization,Accept,Origin,DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Content-Range,Range';
      add_header 'Access-Control-Allow-Methods' 'GET,POST,OPTIONS,PUT,DELETE,PATCH';

      if ($request_method = 'OPTIONS') {
        add_header 'Access-Control-Allow-Origin' 'http://localhost:8080';
        add_header 'Access-Control-Allow_Credentials' 'true';
        add_header 'Access-Control-Allow-Headers' 'X-MBX-APIKEY,Authorization,Accept,Origin,DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Content-Range,Range';
        add_header 'Access-Control-Allow-Methods' 'GET,POST,OPTIONS,PUT,DELETE,PATCH';
        add_header 'Access-Control-Max-Age' 1728000;
        add_header 'Content-Type' 'text/plain charset=UTF-8';
        add_header 'Content-Length' 0;
        return 204;
      }

      proxy_redirect off;
      proxy_pass https://api.binance.com/;
    }
  }

}
```
maybe like this
`docker run --name my-custom-nginx-container -v $(pwd)/nginx.conf:/etc/nginx/nginx.conf:ro -p 9090:80 nginx`
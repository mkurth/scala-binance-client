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
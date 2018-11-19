// shadow sbt-scalajs' crossProject and CrossType from Scala.js 0.6.x
import sbt.Keys.libraryDependencies
import sbtcrossproject.CrossPlugin.autoImport.{CrossType, crossProject}

name := "scala-binance-client"
version := "0.0.1"

val sharedSettings = Seq(
  scalaVersion := "2.12.7",
  libraryDependencies += "com.softwaremill.sttp" %%% "core" % "1.4.2",
  libraryDependencies += "com.softwaremill.sttp" %% "async-http-client-backend-future" % "1.4.2",
  libraryDependencies += "com.softwaremill.sttp" %%% "circe" % "1.4.2",
  libraryDependencies += "io.circe" %%% "circe-generic" % "0.10.1"
)

val library = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Full)
  .settings(sharedSettings)
  .jsSettings(
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.2",
    libraryDependencies += "com.lihaoyi" %%% "utest" % "0.6.3" % "test",
    libraryDependencies += "com.softwaremill.sttp" %%% "core" % "1.4.2",
  )
  .jvmSettings(
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
  )

lazy val js = library.js
lazy val jvm = library.jvm
//lazy val shared = library.shared
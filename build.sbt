// shadow sbt-scalajs' crossProject and CrossType from Scala.js 0.6.x
import sbt.Keys.libraryDependencies
import sbtcrossproject.CrossPlugin.autoImport.{CrossType, crossProject}

version := "0.0.1"
val sttpVersion = "1.5.0"

val sharedSettings = Seq(
  name := "scala-binance-client",
  scalaVersion := "2.12.7",
  organization := "com.mkurth",
  organizationName := "mkurth",
  scmInfo := Some(ScmInfo(url("https://github.com/mkurth/scala-binance-client"), "https://github.com/mkurth/scala-binance-client.git")),
  developers := List(Developer(
    id = "mkurth",
    name = "Michael Kurth",
    email = "poki2.mk@gmail.com",
    url = url("https://github.com/mkurth")
  )),
  description := "Pure scala binance client",
  licenses := List("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt")),
  homepage := Some(url("https://github.com/mkurth/scala-binance-client")),
  libraryDependencies += "com.softwaremill.sttp" %%% "core" % sttpVersion,
  libraryDependencies += "com.softwaremill.sttp" %%% "circe" % sttpVersion,
  libraryDependencies += "io.circe" %%% "circe-generic" % "0.10.1",
  libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % Test
)

val library = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Full)
  .settings(sharedSettings)
  .jsSettings(
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.2",
    libraryDependencies += "com.lihaoyi" %%% "utest" % "0.6.3" % "test",
    libraryDependencies += "com.softwaremill.sttp" %%% "core" % sttpVersion,
  )
  .jvmSettings(
    libraryDependencies += "com.softwaremill.sttp" %% "async-http-client-backend-future" % sttpVersion
  )

lazy val js = library.js
lazy val jvm = library.jvm
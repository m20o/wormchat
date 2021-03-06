name := """wormchat"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "com.typesafe.akka" %% "akka-persistence-experimental" % "2.3.4",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.4" % "test",
  "org.webjars" %% "webjars-play" % "2.3.0-2",
  "org.webjars" % "angularjs" % "1.3.0",
  "org.webjars" % "bootstrap" % "3.2.0-2",
  "org.webjars" % "jquery" % "2.1.1",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test",
  "junit" % "junit" % "4.11" % "test",
  "com.novocode" % "junit-interface" % "0.7" % "test->default"
)

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v")

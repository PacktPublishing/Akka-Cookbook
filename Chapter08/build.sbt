name := "recipes"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies += "com.typesafe.akka" % "akka-actor_2.11" % "2.4.17"
libraryDependencies += "com.typesafe.akka" % "akka-stream_2.11" % "2.4.17"
libraryDependencies += "com.lightbend.akka" % "akka-stream-alpakka-amqp_2.11" % "0.5"
libraryDependencies += "com.typesafe.akka" % "akka-stream-kafka_2.11" % "0.13"
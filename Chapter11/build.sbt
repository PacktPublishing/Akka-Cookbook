organization in ThisBuild := "com.packt.chapter11"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.11.8"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.2.5" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1" % Test

lazy val `akkacookbook` = (project in file("."))
  .aggregate(`akkacookbook-api`, `akkacookbook-impl`, `token-api`,`token-impl`, `consumer-api`, `consumer-impl`, `trip-api`, `trip-impl`, `akka-api`, `akka-impl`)

lazy val `akkacookbook-api` = (project in file("akkacookbook-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `akkacookbook-impl` = (project in file("akkacookbook-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`akkacookbook-api`)

lagomUnmanagedServices in ThisBuild := Map("login" -> "http://localhost:8888")

lazy val `token-api` = (project in file("token-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `token-impl` = (project in file("token-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`token-api`)

lazy val `consumer-api` = (project in file("consumer-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `consumer-impl` = (project in file("consumer-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`consumer-api`,`token-api`)

lazy val `trip-api` = (project in file("trip-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `trip-impl` = (project in file("trip-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`trip-api`)

lazy val `akka-api` = (project in file("akka-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `akka-impl` = (project in file("akka-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`akka-api`)
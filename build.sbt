ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.8.3"

Compile / run / fork := true

lazy val root = (project in file("."))
  .settings(
    name := "bootcamp-student-service-scala",

    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-ember-server" % "0.23.33",
      "org.http4s" %% "http4s-dsl" % "0.23.33",
      "org.http4s" %% "http4s-circe" % "0.23.33",

      "io.circe" %% "circe-generic" % "0.14.15",

      "com.github.pureconfig" %% "pureconfig-generic-scala3" % "0.17.9",

      "org.typelevel" %% "log4cats-slf4j" % "2.7.1",
      "ch.qos.logback" % "logback-classic" % "1.5.18" % Runtime,

      "org.typelevel" %% "munit-cats-effect" % "2.1.0" % Test
    )
  )

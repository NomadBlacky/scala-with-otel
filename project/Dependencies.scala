import sbt._

object Dependencies {
  val AkkaVersion = "2.6.20"

  val munit = "org.scalameta" %% "munit" % "0.7.29" % Test

  val otelSdk = "io.opentelemetry" % "opentelemetry-sdk" % "1.28.0"

  val akkaStream        = "com.typesafe.akka" %% "akka-stream"         % AkkaVersion
  val akkaStreamTestkit = "com.typesafe.akka" %% "akka-stream-testkit" % AkkaVersion % Test
}

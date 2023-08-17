import sbt._

object Dependencies {
  val AkkaVersion = "2.6.20"

  val munit = "org.scalameta" %% "munit" % "0.7.29" % Test

  val otelVersion   = "1.29.0"
  val otelSdk       = "io.opentelemetry"           % "opentelemetry-sdk"       % otelVersion
  val otelJavaAgent = "io.opentelemetry.javaagent" % "opentelemetry-javaagent" % otelVersion

  val akkaStream        = "com.typesafe.akka" %% "akka-stream"         % AkkaVersion
  val akkaStreamTestkit = "com.typesafe.akka" %% "akka-stream-testkit" % AkkaVersion % Test
}

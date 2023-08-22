import Dependencies._

ThisBuild / scalaVersion     := "3.3.0"
ThisBuild / organization     := "dev.nomadblacky"
ThisBuild / organizationName := "NomadBlacky"

ThisBuild / githubWorkflowJavaVersions          := Seq(JavaSpec.temurin("17"))
ThisBuild / githubWorkflowPublishTargetBranches := Seq.empty
ThisBuild / githubWorkflowBuild := Seq(WorkflowStep.Sbt(List("scalafmtCheck", "scalafmtSbtCheck", "test")))

lazy val simple_app = (project in file("simple_app"))
  .enablePlugins(JavaAgent)
  .settings(
    libraryDependencies ++= Seq(otelSdk, munit),
    javaAgents += otelJavaAgent % "dist;runtime",
    run / javaOptions ++= Seq(
      // Configure exporters
      // https://github.com/open-telemetry/opentelemetry-java/blob/main/sdk-extensions/autoconfigure/README.md#exporters
      "-Dotel.traces.exporter=logging-otlp",
      "-Dotel.metrics.exporter=logging-otlp",
      "-Dotel.logs.exporter=logging-otlp"
    )
  )

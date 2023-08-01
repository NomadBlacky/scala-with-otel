import Dependencies._

ThisBuild / scalaVersion     := "3.3.0"
ThisBuild / organization     := "dev.nomadblacky"
ThisBuild / organizationName := "NomadBlacky"

ThisBuild / githubWorkflowJavaVersions          := Seq(JavaSpec.temurin("17"))
ThisBuild / githubWorkflowPublishTargetBranches := Seq.empty
ThisBuild / githubWorkflowBuild := Seq(WorkflowStep.Sbt(List("scalafmtCheck", "scalafmtSbtCheck", "test")))

lazy val simple_app = (project in file("simple_app"))
  .settings(
    libraryDependencies ++= Seq(otelSdk, munit)
  )

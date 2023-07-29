import Dependencies._

ThisBuild / scalaVersion     := "3.3.0"
ThisBuild / organization     := "dev.nomadblacky"
ThisBuild / organizationName := "NomadBlacky"

lazy val simple_app = (project in file("simple_app"))
  .settings(
    libraryDependencies ++= Seq(otelSdk, munit)
  )

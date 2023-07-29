ThisBuild / scalaVersion := "3.3.0"
ThisBuild / organization := "dev.nomadblacky"
ThisBuild / organizationName := "NomadBlacky"

lazy val simple_app = (project in file("simple_app"))
  .settings(
    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test
  )

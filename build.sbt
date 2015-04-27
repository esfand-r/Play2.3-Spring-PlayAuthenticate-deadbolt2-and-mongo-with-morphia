import sbt.Keys._

name := """mycane"""


autoScalaLibrary := false

scalaVersion := "2.11.6"

lazy val commonSettings = Seq(
  organization := "com.mycane",
  version := "0.0.1",
  autoScalaLibrary := false,
  ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) },
  scalaVersion := "2.11.6",
  libraryDependencies ++= Seq(
    javaWs,
    cache,
    "org.springframework" % "spring-context" % "4.1.6.RELEASE",
    "javax.inject" % "javax.inject" % "1",
    "org.springframework" % "spring-test" % "4.1.6.RELEASE" % "test",
    "org.scalatestplus" % "play_2.11" % "1.2.0" % "test"
  )
)

lazy val common = (project in file("modules/common")).
  settings(commonSettings: _*)

lazy val securitycommon = (project in file("modules/securitycommon")).
  settings(commonSettings: _*).
  dependsOn(common)

lazy val notification = (project in file("modules/notification")).
  settings(commonSettings: _*).
  enablePlugins(PlayJava).
  dependsOn(common, securitycommon)

lazy val usermanagement = (project in file("modules/usermanagement")).
  settings(commonSettings: _*).
  dependsOn(common, securitycommon, notification)

lazy val web = (project in file("modules/web")).
  settings(commonSettings: _*).
  enablePlugins(PlayJava).
  dependsOn(common, securitycommon, notification, usermanagement)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    aggregate in update := false,

    // Not yet fully implemented !
    // Couldn't find a plugin to deal with class generation for QueryDSL and morphia.
    // Easier and more accessible plugins exist for maven.
    managedResourceDirectories in Compile in securitycommon <++= baseDirectory {
      base => Seq(base / QueryDSLClassGeneratorRun.generatedSourcePath)
    },

    sourceGenerators in Compile <+=
      (managedDirectory in Compile in securitycommon, fullClasspath in Compile in securitycommon, mainClass in Compile in securitycommon, runner, streams)
        map QueryDSLClassGeneratorRun.createQuerydslClasses
  ).
  enablePlugins(PlayJava, SbtWeb).
  dependsOn(common, securitycommon, notification, usermanagement, web).
  aggregate(common, securitycommon, notification, usermanagement, web)
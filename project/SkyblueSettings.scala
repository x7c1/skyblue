import sbt._
import Keys._
import sbt.Def.SettingList

object SkyblueSettings {
  lazy val commons = new SettingList(Seq(
    // https://mvnrepository.com/artifact/org.scala-lang/scala-library
    scalaVersion := "2.12.6",
    scalacOptions ++= Seq(
      "-deprecation",
      "-feature",
      "-unchecked",
      "-Xfuture",
    ),
    organization := "x7c1",
    libraryDependencies ++= Seq(
      // https://mvnrepository.com/artifact/org.scalatest/scalatest
      "org.scalatest" %% "scalatest" % "3.0.5" % Test,
    ),
  ))
}

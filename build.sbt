lazy val `skyblue-play` = project.
  settings(SkyblueSettings.commons).
  settings(libraryDependencies ++= Seq(
    guice,
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
  )).
  enablePlugins(PlayScala).
  dependsOn(`skyblue-domain`)

lazy val `skyblue-domain` = project.
  settings(SkyblueSettings.commons).
  settings(libraryDependencies ++= Seq(
    "org.apache.tinkerpop" % "gremlin-driver" % "3.4.0",
  ))

lazy val root = Project("skyblue-root", file("."))

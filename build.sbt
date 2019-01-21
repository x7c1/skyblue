lazy val `skyblue-play` = project.
  settings(SkyblueSettings.commons).
  settings(libraryDependencies ++= Seq(
    guice,
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
  )).
  enablePlugins(PlayScala).
  dependsOn(`skyblue-domain`)

lazy val `skyblue-domain` = project.
  settings(SkyblueSettings.commons)

lazy val root = Project("skyblue-root", file("."))

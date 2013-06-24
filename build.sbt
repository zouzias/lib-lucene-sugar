gilt.GiltProject.jarSettings

organization := "com.giltgroupe"

name := "lib-lucene-sugar"

crossScalaVersions in ThisBuild := Seq("2.9.1", "2.9.2")

libraryDependencies ++= Seq(
    "com.google.code.findbugs" % "jsr305" % "2.0.1",
    "org.apache.lucene" % "lucene-core" % "4.3.0",
    "org.apache.lucene" % "lucene-analyzers-common" % "4.3.0",
    "org.apache.lucene" % "lucene-queryparser" % "4.3.0"
) ++ gilt.GiltProject.defaultTestLibs

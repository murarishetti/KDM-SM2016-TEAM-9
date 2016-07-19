import sbt.Keys._

lazy val root = (project in file(".")).
  settings(
    name := "Spark2Ont",
    version := "1.0",
    scalaVersion := "2.11.8"
  )

exportJars := true
fork := true

assemblyJarName := "STweets.jar"

val meta = """META.INF(.)*""".r

assemblyMergeStrategy in assembly := {
  case PathList("javax", "servlet", xs@_*) => MergeStrategy.first
  case PathList(ps@_*) if ps.last endsWith ".html" => MergeStrategy.first
  case n if n.startsWith("reference.conf") => MergeStrategy.concat
  case n if n.endsWith(".conf") => MergeStrategy.concat
  case meta(_) => MergeStrategy.discard
  case x => MergeStrategy.first
}

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.6.0" % "provided",
  "org.apache.spark" %% "spark-mllib" % "1.6.0",
  "edu.stanford.nlp" % "stanford-corenlp" % "3.6.0",
  "edu.stanford.nlp" % "stanford-corenlp" % "3.6.0" classifier "models",
  "edu.stanford.nlp" % "stanford-parser" % "3.6.0",
  "com.google.protobuf" % "protobuf-java" % "2.6.1",
  "org.apache.httpcomponents" % "httpcore" % "4.4.5",
  "org.apache.httpcomponents" % "httpclient" % "4.5.2",
  "com.googlecode.json-simple" % "json-simple" % "1.1.1",
  "com.github.scopt" % "scopt_2.10" % "3.4.0",
  "net.sourceforge.owlapi" % "owlapi-distribution" % "5.0.2",
  "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % "2.7.4",
  "com.fasterxml.jackson.core" % "jackson-core" % "2.7.4",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.7.4",
  "com.fasterxml.jackson.core" % "jackson-annotations" % "2.7.4"
)

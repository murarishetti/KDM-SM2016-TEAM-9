import _root_.sbt.Keys._

name := "kdm"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "edu.stanford.nlp" % "stanford-corenlp" % "3.6.0",
  "edu.stanford.nlp" % "stanford-corenlp" % "3.6.0" classifier "models",
  "edu.stanford.nlp" % "stanford-parser" % "3.6.0",
  "com.google.protobuf" % "protobuf-java" % "2.6.1"
)
libraryDependencies += "org.json" % "json" % "20131018"
javacOptions in (Compile, compile) ++= Seq("-source", "1.8", "-target", "1.8", "-g:lines")
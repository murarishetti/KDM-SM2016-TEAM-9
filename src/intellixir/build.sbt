name := "project"

version := "1.0"

scalaVersion := "2.11.8"

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
libraryDependencies += "com.squareup.okhttp" % "okhttp" % "2.0.0"
libraryDependencies += "org.json" % "json" % "20131018"
javacOptions in (Compile, compile) ++= Seq("-source", "1.8", "-target", "1.8", "-g:lines")
libraryDependencies += "com.googlecode.json-simple" % "json-simple" % "1.1.1"

    
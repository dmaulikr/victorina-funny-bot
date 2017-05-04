name := """victorina-funny-bot"""

version := "0.1"

scalaVersion := "2.11.8"

mainClass in assembly := Some("org.zella.Runner")

libraryDependencies ++= Seq(
  // Uncomment to use Akka
  //"com.typesafe.akka" % "akka-actor_2.11" % "2.3.9",
  "junit"             % "junit"           % "4.12"  % "test",
  "com.novocode"      % "junit-interface" % "0.11"  % "test"
)

// https://mvnrepository.com/artifact/com.typesafe.akka/akka-actor_2.11
libraryDependencies += "com.typesafe.akka" % "akka-actor_2.11" % "2.4.8"
// https://mvnrepository.com/artifact/org.java-websocket/Java-WebSocket
libraryDependencies += "org.java-websocket" % "Java-WebSocket" % "1.3.0"
// https://mvnrepository.com/artifact/io.reactivex/rxjava
libraryDependencies += "io.reactivex" % "rxjava" % "1.1.8"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.7"
// https://mvnrepository.com/artifact/com.google.code.gson/gson
libraryDependencies += "com.google.code.gson" % "gson" % "2.7"
// https://mvnrepository.com/artifact/org.apache.commons/commons-lang3
libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.4"
// https://mvnrepository.com/artifact/com.typesafe/config
libraryDependencies += "com.typesafe" % "config" % "1.3.0"

// https://mvnrepository.com/artifact/com.squareup.retrofit2/retrofit
libraryDependencies += "com.squareup.retrofit2" % "retrofit" % "2.0.2"
// https://mvnrepository.com/artifact/com.squareup.retrofit2/adapter-rxjava
libraryDependencies += "com.squareup.retrofit2" % "adapter-rxjava" % "2.0.2"
// https://mvnrepository.com/artifact/com.squareup.retrofit2/converter-gson
libraryDependencies += "com.squareup.retrofit2" % "converter-gson" % "2.0.2"
// https://mvnrepository.com/artifact/com.squareup.retrofit2/converter-scalars
libraryDependencies += "com.squareup.retrofit2" % "converter-scalars" % "2.0.2"
// https://mvnrepository.com/artifact/com.orientechnologies/orientdb-client
libraryDependencies += "com.orientechnologies" % "orientdb-client" % "2.2.8"

//nullable annotation
libraryDependencies += "com.google.code.findbugs" % "jsr305" % "3.0.0"
// https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-core
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-core" % "2.8.1"
// https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.8.1"

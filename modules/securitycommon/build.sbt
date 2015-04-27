name := "securitycommon"

libraryDependencies ++= Seq(
  javaEbean,
  "org.mongodb" % "mongo-java-driver" % "3.0.0",
  "org.mongodb.morphia" % "morphia" % "0.111",
  "org.mongodb.morphia" % "morphia-logging-slf4j" % "0.111",
  "org.mongodb.morphia" % "morphia-validation" % "0.111",
  "com.mysema.querydsl" % "querydsl-core" % "3.6.3",
  "com.mysema.querydsl" % "querydsl-apt" % "3.6.3",
  "com.mysema.querydsl" % "querydsl-mongodb" % "3.6.3",
  "com.feth" % "play-authenticate_2.11" % "0.6.8",
  "be.objectify" % "deadbolt-java_2.11" % "2.3.3"
)


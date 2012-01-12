libraryDependencies <<= scalaVersion { scala_version => Seq(
  "org.mockito" % "mockito-all" % "1.8.4",
  "org.springframework" % "spring-core" % "3.1.0.RELEASE",
  "org.springframework" % "spring-beans" % "3.1.0.RELEASE",
  "org.springframework" % "spring-jdbc" % "3.1.0.RELEASE",
  "org.springframework" % "spring-tx" % "3.1.0.RELEASE",
  "org.springframework" % "spring-orm" % "3.1.0.RELEASE",
  "org.hibernate" % "hibernate-core" % "3.6.0.CR1",
  "org.hibernate" % "hibernate-validator" % "4.0.2.GA",
  "javassist" % "javassist" % "3.4.GA",
  "org.hsqldb" % "hsqldb" % "2.2.4"
  )
}

parallelExecution in Test := false
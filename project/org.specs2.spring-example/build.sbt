libraryDependencies <<= scalaVersion { scala_version => Seq(
  "org.specs2" %% "specs2" % "1.7.1",
  "junit" % "junit" % "4.7" % "optional",
  "org.springframework" % "spring-core" % "3.1.0.RELEASE",
  "org.springframework" % "spring-beans" % "3.1.0.RELEASE",
  "org.springframework" % "spring-jdbc" % "3.1.0.RELEASE",
  "org.springframework" % "spring-tx" % "3.1.0.RELEASE",
  "org.springframework" % "spring-orm" % "3.1.0.RELEASE",
  "org.springframework" % "spring-test" % "3.1.0.RELEASE",
  "org.hibernate" % "hibernate-core" % "3.6.0.CR1",
  "org.hibernate" % "hibernate-validator" % "4.0.2.GA",
  "javassist" % "javassist" % "3.4.GA",
  "javax.mail" % "mail" % "1.4.1",
  "javax.transaction" % "jta" % "1.1",
  "com.atomikos" % "transactions-jta" % "3.7.0",
  "com.atomikos" % "transactions-jdbc" % "3.7.0",
  "org.apache.activemq" % "activemq-core" % "5.4.1",
  "org.hsqldb" % "hsqldb" % "2.2.4"
  )
}

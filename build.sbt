import sbtrelease._
import Release._
import ReleaseKeys._

/** Project */
name := "spring"

version := "0.6.1"

organization := "org.specs2"

scalaVersion := "2.9.2"

crossScalaVersions := Seq("2.9.2")

/** Shell */
shellPrompt := { state => System.getProperty("user.name") + "> " }

shellPrompt in ThisBuild := { state => Project.extract(state).currentRef.project + "> " }

/** Dependencies */
resolvers ++= Seq("snapshots-repo" at "http://scala-tools.org/repo-snapshots")

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "1.9",
  "org.mockito" % "mockito-all" % "1.8.4",
  "junit" % "junit" % "4.7" % "optional",
  "org.springframework" % "spring-core" % "3.1.1.RELEASE",
  "org.springframework" % "spring-beans" % "3.1.1.RELEASE",
  "org.springframework" % "spring-jdbc" % "3.1.1.RELEASE",
  "org.springframework" % "spring-tx" % "3.1.1.RELEASE",
  "org.springframework" % "spring-orm" % "3.1.1.RELEASE",
  "org.springframework" % "spring-test" % "3.1.1.RELEASE",
  "org.springframework" % "spring-web" % "3.1.1.RELEASE",
  "org.springframework" % "spring-webmvc" % "3.1.1.RELEASE",
  "org.springframework" % "spring-test" % "3.1.1.RELEASE",
  "org.springframework" % "spring-aspects" % "3.1.1.RELEASE",
  "org.springframework" % "spring-instrument" % "3.1.1.RELEASE" % "test->runtime",
  "org.hsqldb" % "hsqldb" % "2.2.4" % "provided",
  "org.htmlparser" % "htmlparser" % "1.6" % "provided",
  "org.hibernate" % "hibernate-core" % "4.0.1.Final" % "provided",
  "javax.persistence" % "persistence-api" % "1.0" % "provided",
  "org.aspectj" % "aspectjweaver" % "1.6.12" % "test->runtime",
  "javax.mail" % "mail" % "1.4.1" % "provided",
  "javax.transaction" % "jta" % "1.1" % "provided",
  "com.atomikos" % "transactions-jta" % "3.7.0" % "provided",
  "com.atomikos" % "transactions-jdbc" % "3.7.0" % "provided",
  "org.apache.activemq" % "activemq-core" % "5.4.1" % "provided",
  "javax.servlet" % "servlet-api" % "2.5" % "provided",
  "org.apache.tomcat" % "jasper" % "6.0.29" % "provided",
  "org.apache.tomcat" % "jasper-jdt" % "6.0.29" % "provided"
  )

/** Compilation */
javacOptions ++= Seq()

javaOptions += "-Xmx2G"

scalacOptions ++= Seq("-deprecation", "-unchecked")

maxErrors := 20 

pollInterval := 1000

logBuffered := false

cancelable := true

testOptions := Seq(Tests.Filter(s =>
  Seq("Spec", "Suite", "Unit", "all").exists(s.endsWith(_)) &&
    !s.endsWith("FeaturesSpec") ||
    s.contains("UserGuide") || 
    s.contains("index") ||
    s.matches("org.specs2.guide.*")))

/** Console */
initialCommands in console := "import org.specs2.spring._"

seq(releaseSettings: _*)

releaseProcess <<= thisProjectRef apply { ref =>
  import ReleaseStateTransformations._
  Seq[ReleasePart](
    initialGitChecks,                     
    checkSnapshotDependencies,    
    inquireVersions,
    setReleaseVersion,                      
    runTest,                                
    commitReleaseVersion,                   
    tagRelease,                             
    setNextVersion,
    commitNextVersion                       
  )
}

publishTo <<= version { v: String =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus + "content/repositories/snapshots")
  else                             Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { x => false }

pomExtra := (
  <url>http://www.cakesolutions.org/specs2-spring.html</url>
  <licenses>
    <license>
      <name>BSD-style</name>
      <url>http://www.opensource.org/licenses/bsd-license.php</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:janm399/specs2-spring.git</url>
    <connection>scm:git:git@github.com:janm399/specs2-spring.git</connection>
  </scm>
  <developers>
    <developer>
      <id>janmachacek</id>
      <name>Jan Machacek</name>
      <url>http://cakesolutions.org</url>
      </developer>
    </developers>
)

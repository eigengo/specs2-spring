import sbtrelease._

/** Project */
name := "spring"

version := "2.2.0-SNAPSHOT"

organization := "org.specs2"

scalaVersion := "2.10.4"

/** Shell */
shellPrompt := { state => System.getProperty("user.name") + "> " }

shellPrompt in ThisBuild := { state => Project.extract(state).currentRef.project + "> " }

/** Dependencies */
resolvers ++= Seq("snapshots-repo" at "http://scala-tools.org/repo-snapshots")

libraryDependencies ++= Seq(
  "org.specs2"         %% "specs2-core"       % "2.3.4",
  "org.specs2"         %% "specs2-mock"       % "2.3.4" % "optional",
  "org.specs2"         %% "specs2-junit"      % "2.3.4" % "optional",
  "org.mockito"         % "mockito-core"      % "1.9.5" % "optional",
  "org.springframework" % "spring-core"       % "3.2.9.RELEASE" % "provided",
  "org.springframework" % "spring-beans"      % "3.2.9.RELEASE" % "provided",
  "org.springframework" % "spring-jdbc"       % "3.2.9.RELEASE" % "provided",
  "org.springframework" % "spring-tx"         % "3.2.9.RELEASE" % "provided",
  "org.springframework" % "spring-test"       % "3.2.9.RELEASE" % "provided",
  "org.springframework" % "spring-test"       % "3.2.9.RELEASE" % "provided",
  "org.springframework" % "spring-orm"        % "3.2.9.RELEASE" % "provided",
  "org.springframework" % "spring-web"        % "3.2.9.RELEASE" % "provided",
  "org.springframework" % "spring-webmvc"     % "3.2.9.RELEASE" % "provided",
  "org.springframework" % "spring-aspects"    % "3.2.9.RELEASE" % "provided",
  "junit"               % "junit"             % "4.7"           % "optional",
  "org.hsqldb"          % "hsqldb"            % "2.2.4"         % "provided",
  "org.hibernate"       % "hibernate-core"    % "4.0.1.Final"   % "provided",
  "javax.persistence"   % "persistence-api"   % "1.0"           % "provided",
  "javax.mail"          % "mail"              % "1.4.1"         % "provided",
  "javax.transaction"   % "jta"               % "1.1"           % "provided",
  "com.atomikos"        % "transactions-jta"  % "3.7.0"         % "provided",
  "com.atomikos"        % "transactions-jdbc" % "3.7.0"         % "provided",
  "org.apache.activemq" % "activemq-core"     % "5.4.1"         % "provided",
  "org.springframework" % "spring-instrument" % "3.2.9.RELEASE" % "test->runtime",
  "org.aspectj"         % "aspectjweaver"     % "1.6.12"        % "test->runtime"
  )

/** Compilation */
javacOptions ++= Seq("-source", "1.7", "-target", "1.7")

javaOptions += "-Xmx2G -XX:MaxPermSize=1024m"

scalacOptions ++= Seq("-deprecation", "-unchecked")

maxErrors := 20 

pollInterval := 1000

logBuffered := false

cancelable := true

credentials += Credentials(Path.userHome / ".sonatype")

testOptions := Seq(Tests.Filter(s =>
  Seq("Spec", "Suite", "Unit", "all").exists(s.endsWith(_)) &&
    !s.endsWith("FeaturesSpec") ||
    s.contains("UserGuide") || 
    s.contains("index") ||
    s.matches("org.specs2.guide.*")))

/** Console */
initialCommands in console := "import org.specs2.spring._"

publishTo <<= version { v: String =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus + "content/repositories/snapshots")
  else                             Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { x => false }

pomExtra := (
  <url>http://www.eigengo.com/</url>
  <licenses>
    <license>
      <name>BSD-style</name>
      <url>http://www.opensource.org/licenses/bsd-license.php</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:eigengo/specs2-spring.git</url>
    <connection>scm:git:git@github.com:eigengo/specs2-spring.git</connection>
  </scm>
  <developers>
    <developer>
      <id>janmachacek</id>
      <name>Jan Machacek</name>
      <url>http://www.eigengo.com</url>
      </developer>
    <developer>
      <id>anirvanchakraborty</id>
      <name>Anirvan Chakraborty</name>
      <url>http://www.eigengo.com</url>
    </developer>
  </developers>
)

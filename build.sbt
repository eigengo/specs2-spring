import sbtrelease._
import Release._
import ReleaseKeys._

/** Project */
name := "specs2-spring"

version := "0.3"

organization := "org.specs2"

scalaVersion := "2.9.1"

crossScalaVersions := Seq("2.9.0")

docBookXslFoStyleSheet in DocBook := "src/main/docbook/styles/pdf/custom.xsl"

/** Shell */
shellPrompt := { state => System.getProperty("user.name") + "> " }

shellPrompt in ThisBuild := { state => Project.extract(state).currentRef.project + "> " }

/** Dependencies */
resolvers ++= Seq("snapshots-repo" at "http://scala-tools.org/repo-snapshots")

libraryDependencies <<= scalaVersion { scala_version => Seq(
  "org.specs2" %% "specs2" % "1.7.1",
  "org.mockito" % "mockito-all" % "1.8.4",
  "junit" % "junit" % "4.7" % "optional",
  "org.springframework" % "spring-core" % "3.1.0.RELEASE",
  "org.springframework" % "spring-beans" % "3.1.0.RELEASE",
  "org.springframework" % "spring-jdbc" % "3.1.0.RELEASE",
  "org.springframework" % "spring-tx" % "3.1.0.RELEASE",
  "org.springframework" % "spring-orm" % "3.1.0.RELEASE",
  "org.springframework" % "spring-test" % "3.1.0.RELEASE",
  "org.springframework" % "spring-web" % "3.1.0.RELEASE",
  "org.springframework" % "spring-webmvc" % "3.1.0.RELEASE",
  "org.springframework" % "spring-test" % "3.1.0.RELEASE",
  "org.springframework" % "spring-aspects" % "3.1.0.RELEASE",
  "org.hsqldb" % "hsqldb" % "2.2.4",
  "org.htmlparser" % "htmlparser" % "1.6",
  "org.hibernate" % "hibernate-core" % "4.0.1.Final",
  "javax.persistence" % "persistence-api" % "1.0",
  "org.aspectj" % "aspectjweaver" % "1.6.12",
  "javax.mail" % "mail" % "1.4.1",
  "javax.transaction" % "jta" % "1.1",
  "com.atomikos" % "transactions-jta" % "3.7.0",
  "com.atomikos" % "transactions-jdbc" % "3.7.0",
  "org.apache.activemq" % "activemq-core" % "5.4.1",
  "javax.servlet" % "servlet-api" % "2.5",
  "org.apache.tomcat" % "jasper" % "6.0.29",
  "org.apache.tomcat" % "jasper-jdt" % "6.0.29",
  "saxon" % "saxon" % "6.5.3", /* SBT PDF Dependency */
  "xml-resolver" % "xml-resolver" % "1.2",
  "net.sf.docbook" % "docbook-xsl" % "1.76.1",
  "net.sf.docbook" % "docbook-xsl-saxon" % "1.0.0",
  "org.apache.xmlgraphics" % "fop" % "1.0",
  "xerces" % "xercesImpl" % "2.10.0"
  )
}

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
    //releaseTask(check in Posterous in ref),  
    inquireVersions,                        
    setReleaseVersion,                      
    runTest,                                
    commitReleaseVersion,                   
    tagRelease,                             
    //releaseTask(publish in Global in ref),
    //releaseTask(publish in Posterous in ref),    
    setNextVersion,                         
    commitNextVersion                       
  )
}


/** Publishing */
credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

publishTo <<= (version) { version: String =>
  val nexus = "http://nexus-direct.scala-tools.org/content/repositories/"
  if (version.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus+"snapshots/") 
  else                                   Some("releases" at nexus+"releases/")
}

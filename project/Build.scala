import sbt._

object Specs2Spring extends Build {

  lazy val root = Project("specs2-spring", file(".")) aggregate(core, coreExample, web) //, webExample)
  lazy val core = Project("org.specs2.spring", file("org.specs2.spring")) 
  lazy val coreExample = Project("org.specs2.spring-example", file("org.specs2.spring-example")) dependsOn(core)

  lazy val web = Project("org.specs2.spring.web", file("org.specs2.spring.web")) dependsOn(core)
  lazy val webExample = Project("org.specs2.spring.web-example", file("org.specs2.spring.web-example")) dependsOn(web)

  // lazy val documentation = Project("org.specs2.spring.documentation", file("org.specs2.spring.documentation"))
}
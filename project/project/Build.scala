import sbt._

object Specs2Spring extends Build {

  lazy val root = Project("root", file("."))
  lazy val sub1: Project = Project("org.specs2.spring", file("org.specs2.spring"));
  lazy val sub2 = Project("proj2", file("dir2"))
}
resolvers ++= Seq("coda" at "http://repo.codahale.com")

resolvers += "gseitz@github.com" at "http://gseitz.github.com/maven/"

addSbtPlugin("com.github.gseitz" % "sbt-release" % "0.4")

resolvers += "sbt-idea-repo" at "http://mpeltonen.github.com/maven/"

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "0.11.0")

addSbtPlugin("de.undercouch" % "sbt-docbook-plugin" % "0.2-SNAPSHOT")

resolvers += "sbt-plugin-releases" at "http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases/"

addSbtPlugin("com.jsuereth" % "xsbt-gpg-plugin" % "0.5")
name := "recipes"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies += "com.typesafe.akka" % "akka-actor_2.11" % "2.4.17"
libraryDependencies += "com.typesafe.akka" % "akka-persistence_2.11" % "2.4.17"
libraryDependencies += "org.iq80.leveldb"  % "leveldb" % "0.7"
libraryDependencies += "org.fusesource.leveldbjni"   % "leveldbjni-all"   % "1.8"
libraryDependencies += "com.typesafe.akka" %% "akka-persistence-cassandra" % "0.25.1"

resolvers += Resolver.jcenterRepo
libraryDependencies += "com.hootsuite" %% "akka-persistence-redis" % "0.6.0"

libraryDependencies +=  "com.typesafe.akka" %% "akka-persistence-query-experimental" % "2.4.17"
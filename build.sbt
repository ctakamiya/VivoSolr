name := "VivoSolr"

version := "1.0"

scalaVersion := "2.11.12"

exportJars := true

val sparkSolrVersion = "3.9.0.7.2.9.0-203"
val logVersion = "2.14.1"
val logApiScala = "12.0"
val sparkVersion = "2.4.5"

resolvers := List("Cloudera repo" at "https://repository.cloudera.com/artifactory/cloudera-repos/",
  "Jetty Releases" at "https://repo.hortonworks.com/content/repositories/jetty-hadoop/",
  "Shibboleth repo" at "https://build.shibboleth.net/nexus/content/groups/public"
)

libraryDependencies ++= Seq(
  "com.lucidworks.spark" % "spark-solr" % sparkSolrVersion classifier "shaded",
  "org.apache.solr" % "solr-core" % "8.4.1",
  "com.hortonworks.hive" % "hive-warehouse-connector_2.11" % "1.0.0.7.2.9.0-203" % "provided",
  "org.apache.logging.log4j" % "log4j-api" % logVersion,
  "org.apache.logging.log4j" % "log4j-core" % logVersion,
  "org.apache.logging.log4j" %% "log4j-api-scala" % logApiScala,
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided"
)

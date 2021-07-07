# VivoSolr

Este projeto é um protótipo que tem como objetivos:

* extrair dados de Tabela Transacionais do Hive (Acid) pelo Spark
* Uso do modo DIRECT_READER_V1 sem usar os elementos do LLAP e outros intermediários
* Indexação no Solr



## Pré-requisitos

Para a execução deste protótipo são necessários:

* usuário (exemplo: ctakamiya) como permissão de leitura no HDFS (Ranger)
* keytab deste usuário (exemplo ctakamiya.keytab)

## Parâmetros do submit

```
$ spark-submit --jars /opt/cloudera/parcels/CDH/jars/spark-solr-3.9.0.7.2.11.0-150-shaded.jar,/opt/cloudera/parcels/CDH/lib/hive_warehouse_connector/hive-warehouse-connector-assembly-1.0.0.7.2.11.0-150.jar,/opt/cloudera/parcels/CDH/lib/hive/lib/hive-exec.jar  --files ctakamiya.keytab,jaas-client.conf --name spark-solr --conf spark.hadoop.hive.metastore.uris=thrift://ctakamiya-1.ctakamiya.root.hwx.site:9083 --conf spark.hadoop.hive.zookeeper.quorum=ctakamiya-1.ctakamiya.root.hwx.site:2181 --conf spark.sql.hive.hiveserver2.jdbc.url="jdbc:hive2://ctakamiya-1.ctakamiya.root.hwx.site:2181/default;serviceDiscoveryMode=zooKeeper;zooKeeperNamespace=hiveserver2" --conf spark.sql.hive.hiveserver2.jdbc.url.principal="hive/_HOST@ROOT.HWX.SITE" --conf spark.sql.extensions="com.hortonworks.spark.sql.rule.Extensions" --conf spark.kryo.registrator=com.qubole.spark.hiveacid.util.HiveAcidKyroRegistrator --conf spark.sql.warehouse.dir=/warehouse/tablespace/managed/hive  --conf spark.security.credentials.hiveserver2.enabled=true  --conf spark.datasource.hive.warehouse.read.mode=DIRECT_READER_V1 --conf "spark.executor.extraJavaOptions=-Djavax.net.ssl.trustStore=/var/lib/cloudera-scm-agent/agent-cert/cm-auto-global_truststore.jks -Djavax.net.ssl.trustStorePassword="  --conf "spark.network.timeout=500s" --driver-java-options="-Djavax.net.ssl.trustStore=/var/lib/cloudera-scm-agent/agent-cert/cm-auto-global_truststore.jks -Djavax.net.ssl.trustStorePassword="  --master yarn --num-executors 1 --driver-memory 1g --executor-memory 3g  --class com.lucidworks.spark.SparkApp vivosolr_2.11-1.0.jar etl -zkHost ctakamiya-1.ctakamiya.root.hwx.site:2181/solr-infra olr -collection testcollection -csvPath nyc_yellow_taxi_sample_1k.csv -solrJaasAuthConfig=jaas-client.conf

```

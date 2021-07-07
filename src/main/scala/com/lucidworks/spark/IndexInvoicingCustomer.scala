package com.lucidworks.spark

import com.lucidworks.spark.SparkApp.RDDProcessor
import com.lucidworks.spark.util.SolrSupport
import org.apache.solr.common.SolrInputDocument
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Row
import shaded.apache.commons.cli.{CommandLine, Option}

class IndexInvoicingCustomer  extends RDDProcessor {
  override def getName: String = "etl"

  override def getOptions: Array[Option] =
    Array {
      Option.builder("csvPath").argName("PATH").hasArgs
        .required(true)
        .desc("Path to the CSV file to index")
        .build()
    }


  override def run(conf: SparkConf, cli: CommandLine): Int = {

    val spark = SparkSession
      .builder
      .appName("Indexacao no Solr de dados de tabela Hive")
      .config(conf)
      .enableHiveSupport
      .getOrCreate
    val hive = com.hortonworks.hwc.HiveWarehouseSession
      .session(spark)
      .build()

    val dados = hive.sql("""SELECT DISTINCT
        |       coalesce(customer_id, '') invoice_customerkey,
        |       coalesce(customer_name, '') contact_fullname,
        |       coalesce(customer_documenttype, '') contactidentif_identificationdocumenttypekey,
        |       coalesce(customer_documentnumber, '') contactidentif_identificationdocumentnumber
        |  FROM p_bigd_fastdata_db.tbgdt_invoicing_api_dataset
        | WHERE SOURCE = 'AMDOCS' AND p_date = '20201109_112008'""".stripMargin)


    val zkhost = cli.getOptionValue("zkHost", "localhost:9983")
    val collection = cli.getOptionValue("collection", "collection1")
    val batchSize = Integer.parseInt(cli.getOptionValue("batchSize", "100"))

    val schema = dados.schema



    val dados2: RDD[SolrInputDocument] = dados.rdd.map {
      invoiceCustomer: Row => {
        val doc: SolrInputDocument = new SolrInputDocument
        var i: Int = 0
        for (i <- 0 to schema.length - 1) {
          doc.setField(schema(i).name, invoiceCustomer(i))
        }
        doc
      }
      }
    SolrSupport.indexDocs(zkhost, collection, batchSize, dados2)

    hive.close()
    spark.stop()
    spark.close()
    0
  }
}


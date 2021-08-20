package com.zeotap.utility.spark.ops

import com.holdenkarau.spark.testing.{Column, DataframeGenerator}
import com.zeotap.utility.spark.types.{ArrayColumn, DataColumn, SparkDataframe}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, SparkSession}

object SparkDataframeOps {

  implicit class SparkOps(dataGenerators: SparkDataframe) {
    def getOne()(implicit sparkSession: SparkSession): Option[DataFrame] = getArbitraryGenerator().sample

    def getArbitraryGenerator()(implicit sparkSession: SparkSession) =
      DataframeGenerator.arbitraryDataFrameWithCustomFields(sparkSession.sqlContext, getSchema())(dataGenerators
        .dataColumns.map {
        case d: DataColumn => new Column(d.name, d.dataGenerator)
        case a: ArrayColumn => new Column(a.dataColumn.name, a.dataGenerator)
      }: _*).arbitrary

    def getSchema(): StructType = StructType(dataGenerators.dataColumns.map(x => x.generateSchema))
  }
}

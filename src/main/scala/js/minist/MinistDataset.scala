package js.minist

import js.mathlib.Vector
import org.deeplearning4j.datasets.fetchers.MnistDataFetcher
import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator
import org.nd4j.linalg.api.ndarray.INDArray

import scala.collection.mutable.ArrayBuffer

/**
  * Created by jonysugiantohp on 11/11/16.
  */
object MinistDataset {
  val batchsize=100
  val numsamples=MnistDataFetcher.NUM_EXAMPLES
  val trainData = new MnistDataSetIterator(batchsize, numsamples, true)
  val testData = new MnistDataSetIterator(batchsize, MnistDataFetcher.NUM_EXAMPLES_TEST, false)

  def convertLabels(lbls:INDArray):Int={
    var ret=lbls.getDouble(0).toInt
    var size=lbls.length()
    for(i<-0 until size){
      if(lbls.getDouble(i)>0){
        return i
      }
    }
    return ret
  }

  def nextBatchTrain():(Array[Vector], Array[Int])={
    var ds=trainData.next()
    val inputsarray=ds.getFeatureMatrix()
    val outputs=ArrayBuffer[Int]()
    var inputs=ArrayBuffer[Vector]()
    val rowsize=inputsarray.rows()
    for(i<-0 until rowsize){
      outputs.append(convertLabels(ds.getLabels().getRow(i)))
      var row=inputsarray.getRow(i)
      var column=row.length()
      var temp=Array.fill(column)(0d)
      for(j<-0 until column){
        temp(j)=row.getDouble(j)
      }
      inputs.append(Vector(temp))
    }
    (inputs.toArray, outputs.toArray)
  }

  def nextBatchTest():(Array[Vector], Array[Int])={
    var ds=testData.next()
    val inputsarray=ds.getFeatureMatrix()
    val outputs=ArrayBuffer[Int]()
    var inputs=ArrayBuffer[Vector]()
    val rowsize=inputsarray.rows()
    for(i<-0 until rowsize){
      outputs.append(convertLabels(ds.getLabels().getRow(i)))
      var row=inputsarray.getRow(i)
      var column=row.length()
      var temp=Array.fill(column)(0d)
      for(j<-0 until column){
        temp(j)=row.getDouble(j)
      }
      inputs.append(Vector(temp))
    }
    (inputs.toArray, outputs.toArray)
  }

  def validationTest(network:MinistSupervisedNetwork): Double ={
    var batchcounter=0
    var totaltest=0
    var totalright=0
    while(testData.hasNext){
      println("batch counter:"+batchcounter)
      batchcounter=batchcounter+1
      var td=nextBatchTest()
      for(i<-0 until td._2.length){
        totaltest=totaltest+1
        var x=td._1(i)
        var c=td._2(i)
        var twonodes=network.findTwoNearestNodeBy_x_w(x)
        if(twonodes.y1.c==c){
          totalright=totalright+1
        }
      }
    }
    totalright.toDouble/totaltest.toDouble
  }

  def main(args: Array[String]): Unit = {
    for(i<-0 until 100) {
      val ds = testData.next
      println(ds.getFeatureMatrix().getRow(0).getRow(0).data().asDouble().length)
      println("==========================")
      println(ds.getLabels.getRow(0))
      println(convertLabels(ds.getLabels().getRow(0)))
    }
  }
}
package js.gng

import java.io.{FileReader, FileWriter}

import com.google.gson.Gson

/**
  * Created by jonysugiantohp on 10/11/16.
  */
class LearnConfig {
  var learningrate: Double = 0.1
  var max_age: Int = 100
  var max_nodes: Int = 25
  var decay_error: Double = 0.001
  var lambda: Int = 1000
  var learn:Boolean=true
  var run:Boolean=true
}

object LearnConfig{
  val gson = new Gson();

  def readFromJSon(filename: String): LearnConfig = {
    try {
      val reader = new FileReader(filename)
      val jc = gson.fromJson(reader, classOf[LearnConfig]);
      reader.close()
      jc
    } catch {
      case e: Exception => {
        e.printStackTrace();
      }
        null
    }
  }

  def writeToJson(filename: String, jc:LearnConfig): Unit ={
    try {
      val writer = new FileWriter(filename)
      gson.toJson(jc, writer);
      writer.flush()
      writer.close()
    } catch {
      case e: Exception => {
        e.printStackTrace();
      }
    }
  }

  def main(args: Array[String]): Unit = {
    val jc = new LearnConfig
    val json = gson.toJson(jc);
    System.out.println(json);
    writeToJson("/data/tmp/network.conf", jc)
    val rjc=readFromJSon("/data/tmp/network.conf")
    println(rjc.lambda)

  }
}
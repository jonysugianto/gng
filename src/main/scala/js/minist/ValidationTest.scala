package js.minist

import js.gng.{LearnConfig, NetworkPersistent}

/**
  * Created by jonysugiantohp on 11/11/16.
  */
object ValidationTest {
  def main(args: Array[String]): Unit = {
    var conf=LearnConfig.readFromJSon("/data/tmp/network.conf")
    var gv=new MinistGraphViewer()
    val network=NetworkPersistent.loadSupervisedNetwork("/data/tmp/minist.network").asInstanceOf[MinistSupervisedNetwork]

    //network.neuronListener=gv.neuronListener
    //network.edgeListener=gv.edgeListener
    //gv.graph.display()

    var start=System.currentTimeMillis()
    var accuracy=MinistDataset.validationTest(network)
    var duration=System.currentTimeMillis()-start
    println("duration:"+duration.toDouble/1000.0)
    println("accuracy:"+accuracy)
  }

}

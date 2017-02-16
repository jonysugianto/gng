package js.minist

import js.gng.{LearnConfig, NetworkPersistent, SupervisedNetwork}
/**
  * Created b    println("T:"+T)
  * y jonysugiantohp on R = findNearestNeuron(n)._2/2.0
     03/11/16.
  */
object InitialTraining{


  def main(args: Array[String]): Unit = {
    var conf=LearnConfig.readFromJSon("/data/tmp/network.conf")
    var gv=new MinistGraphViewer()
    val network=new MinistSupervisedNetwork()

    network.neuronListener=gv.neuronListener
    network.edgeListener=gv.edgeListener
    gv.graph.display()

    var initdata=MinistDataset.nextBatchTrain()
    network.initSeedNeuron(initdata._1(0), initdata._1(1), initdata._2(0), initdata._2(1))
    MinistDataset.trainData.reset()
    var iter_counter=0
    var start=System.currentTimeMillis()
    while(MinistDataset.trainData.hasNext){
      iter_counter=iter_counter+1
      var td=MinistDataset.nextBatchTrain()
      for(i<-0 until td._2.length){
        var x=td._1(i)
        var c=td._2(i)
        network.one_step_learn(x, c, conf.learningrate, conf.max_age, conf.max_nodes,
          conf.decay_error, conf.lambda)
      }

      if((iter_counter%10)==0){
        println("data processed:"+iter_counter*MinistDataset.batchsize)
        conf=LearnConfig.readFromJSon("/data/tmp/network.conf")
      }
    }
    var duration=System.currentTimeMillis()-start
    println("duration:"+duration.toDouble/1000.0)
    NetworkPersistent.saveSupervisedNetwork("/data/tmp/minist.network", network.asInstanceOf[SupervisedNetwork])
    var accuracy=MinistDataset.validationTest(network)
    println("accuracy:"+accuracy)
  }
}

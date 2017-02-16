package js.gng

import js.mathlib

/**
  * Created by jonysugiantohp on 10/11/16.
  */
class BaseNetwork extends GNGGraph{

  def findNearestNodeBy_x_w(x: mathlib.Vector): Array[(Neuron, Double)] = {
    var temp = getAllNodes()
    var nodes = temp.map(n => {
      var d = n.computeDistanceTo_w(x)
      (n, d)
    })
    var sorted_nodes = nodes.sortWith(_._2 < _._2)
    sorted_nodes
  }

  def findTwoNearestNodeBy_x_w(x: mathlib.Vector): TwoNearestNode = {
    var sorted_nodes = findNearestNodeBy_x_w(x)
    TwoNearestNode(sorted_nodes(0)._1, sorted_nodes(0)._2, sorted_nodes(1)._1, sorted_nodes(1)._2)
  }

  def findTwoNearestNodeBy_x_w_Parallel(x: mathlib.Vector): TwoNearestNode = {
    var temp = nodeMap.values.par
    var nodes = temp.map(n => {
      var d = n.computeDistanceTo_w(x)
      (n, d)
    })
    var sorted_nodes = nodes.toArray.sortWith(_._2 < _._2)
    TwoNearestNode(sorted_nodes(0)._1, sorted_nodes(0)._2, sorted_nodes(1)._1, sorted_nodes(1)._2)
  }

  def inference(x:mathlib.Vector):(Neuron,Double)={
    val twonodes=findTwoNearestNodeBy_x_w_Parallel(x)
    (twonodes.y1, twonodes.d_y1)
  }


  def findNearestNeuron(n: Neuron): (Neuron, Double) = {
    var temp = getAllNodesExcept(n)
    var nodes = temp.map(ni => {
      var d = n.computeDistanceTo_w(ni.w)
      (n, d)
    })
    var sorted_nodes = nodes.sortWith(_._2 < _._2)
    (sorted_nodes(0)._1, sorted_nodes(0)._2)
  }

  def findNearestNeuron_Parallel(n: Neuron): (Neuron, Double) = {
    var temp = getAllNodesExcept(n).par
    var nodes = temp.map(ni => {
      var d = n.computeDistanceTo_w(ni.w)
      (n, d)
    })
    var sorted_nodes = nodes.toArray.sortWith(_._2 < _._2)
    (sorted_nodes(0)._1, sorted_nodes(0)._2)
  }

  def findNearestFromNeurons(n: Neuron, neurons: Array[Neuron]): (Neuron, Double) = {
    var nodes = neurons.map(ni => {
      var d = n.computeDistanceTo_w(ni.w)
      (n, d)
    })
    var sorted_nodes = nodes.sortWith(_._2 < _._2)
    (sorted_nodes(0)._1, sorted_nodes(0)._2)
  }

  def findFurthestFromNeurons(n: Neuron, neurons: Array[Neuron]): (Neuron, Double) = {
    var nodes = neurons.map(ni => {
      var d = n.computeDistanceTo_w(ni.w)
      (n, d)
    })
    var sorted_nodes = nodes.sortWith(_._2 > _._2)
    (sorted_nodes(0)._1, sorted_nodes(0)._2)
  }

  def findMeanDistanceFromNeurons(n: Neuron, neurons: Array[Neuron]): Double = {
    var total_d=0.0
    neurons.foreach(nb=>{
      total_d=total_d+n.computeDistanceTo_w(nb.w)
    })

    var R = total_d/neurons.length.toDouble
    R
  }

  def findMeanErrorFromNeurons(winner:Neuron, neurons: Array[Neuron]): Double = {
    var total_e=0.0
    neurons.foreach(nb=>{
      total_e=total_e+nb.error
    })

    total_e=total_e+winner.error
    var err = total_e/(neurons.length.toDouble+1.0)
    err
  }

  def compute_mean_error():Double={
    val nodes=getAllNodes()
    var total=0.0
    nodes.foreach(n=>{
      total=total+n.error
    })
    total/nodes.length.toDouble
  }


  def compute_Radius(n: Neuron, max_nodes: Int): Double = {
    var R = 1.0
    val neighbours = getNeighbourNodes(n)
    if (neighbours.length == 0) {
      R = findNearestNeuron_Parallel(n)._2/2.0
    } else {
      R = findFurthestFromNeurons(n, neighbours)._2
//      R = findMeanDistanceFromNeurons(n, neighbours)
      var mean_neighborhhood_error=findMeanErrorFromNeurons(n, neighbours)
      var ratio=compute_mean_error() / mean_neighborhhood_error
      R = R * (math.log(ratio+1)+1)
    }
    var ratio_nodes=(numberNodes().toDouble/max_nodes.toDouble)
    R = R * (math.log(ratio_nodes+1)+0.3)
//    R = R * math.sqrt(ratio_nodes)
    return R
  }

  def remove_low_utility_nodes(max_nodes:Int, mean_error: Double): Int = {
    val nodes=getAllNodes()
    /*for(i<-0 until nodes.length){
      if(nodes(i).error<=0.0){
        removeNeuron(nodes(i))
      }
    }
*/
    if(max_nodes<numberNodes()){
      val orderedNeurons = orderNeuronByError()
      var ratio_to_mean_error=2.0
      var removed_node=0
      var size=orderedNeurons.length-max_nodes
      var total=0.0
      for(i<-0 until size){
        total=total+(orderedNeurons(i).error/mean_error)
      }
      total=total/(orderedNeurons.length-max_nodes).toDouble
      ratio_to_mean_error=total
      for (i <- 0 until orderedNeurons.length - 2) {
        if ((orderedNeurons(i).error / mean_error) <= ratio_to_mean_error){
          removed_node=removed_node+1
          removeNeuron(orderedNeurons(i))
        }
      }
      return removed_node
    }
    0
   }

  def decreaseError(decay_error: Double): Unit = {
    val nodes = getAllNodes()
    nodes.foreach(n => {
      n.error = n.error * (1.0 - decay_error)
    })
  }

  def orderNeuronByError(): Array[Neuron] = {
    val neurons=getAllNodes()
    var sorted_nodes = neurons.sortWith(_.error < _.error)
    sorted_nodes
  }

}

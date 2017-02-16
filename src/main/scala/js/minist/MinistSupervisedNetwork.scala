package js.minist

import js.gng.{Neuron, SupervisedNetwork}

/**
  * Created by jonysugiantohp on 11/11/16.
  */
class MinistSupervisedNetwork extends SupervisedNetwork{
  def compute_Radius2(n: Neuron, max_nodes: Int): Double = {
    var R = 1.0
    val neighbours = getNeighbourNodes(n)
    if (neighbours.length == 0) {
      //  R = findNearestNeuron(n)._2/2.0
      R = findNearestNeuron_Parallel(n)._2
    } else {
      //R = findMeanDistanceFromNeurons(n, neighbours)
      R = findFurthestFromNeurons(n, neighbours)._2
    }
//    R = R * math.sqrt(compute_mean_error() / n.error)*(numberNodes().toDouble/max_nodes.toDouble)
    //R = R * math.sqrt(compute_mean_error() / n.error)
    //R = R * compute_mean_error() / n.error
    //R = R * (compute_mean_error() / n.error)*(numberNodes().toDouble/max_nodes.toDouble)
    return R
  }
}

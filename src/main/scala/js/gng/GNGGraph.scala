package js.gng

import js.mathlib.Vector

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer


/**
  * Created by jonysugiantohp on 10/11/16.
  */
class GNGGraph extends Serializable {
  val nodeMap = new mutable.HashMap[Int, Neuron]()
  val edgeMap = new mutable.HashMap[String, Edge]()
  val graph = new mutable.HashMap[Int, ArrayBuffer[Int]]()
  var edge_id = 0
  var neuron_id = 0

  @transient var neuronListener: (Neuron, Boolean) => Unit = null
  @transient var edgeListener: (Edge, Boolean) => Unit = null

  def genEdgeId(): Int = {
    edge_id = edge_id + 1
    edge_id
  }

  def genNeuronId(): Int = {
    neuron_id = neuron_id + 1
    neuron_id
  }

  def create(x: Vector): Neuron = {
    var ret = new Neuron(genNeuronId(), x)
    return ret
  }


  def addNeuron(n: Neuron): Unit = {
    nodeMap.put(n.id, n)
    graph.put(n.id, new ArrayBuffer[Int]())
    if (neuronListener != null) {
      neuronListener(n, true)
    }
  }

  def removeNeuron(n: Neuron): Unit = {
    var listedge = graph.getOrElse(n.id, null)
    var size = listedge.size
    for (i <- 0 until size) {
      removeEdge(n.id, listedge(0))
    }
    nodeMap.remove(n.id)
    graph.remove(n.id)
    if (neuronListener != null) {
      neuronListener(n, false)
    }
  }

  def addEdge(e: Edge): Unit = {
    edgeMap.put(e.source.id + "-" + e.target.id, e)
    edgeMap.put(e.target.id + "-" + e.source.id, e)
    graph.getOrElse(e.source.id, null).append(e.target.id)
    graph.getOrElse(e.target.id, null).append(e.source.id)
    if (edgeListener != null) {
      edgeListener(e, true)
    }
  }

  def removeEdge(e: Edge): Unit = {
    var index_edge_from_source = graph.getOrElse(e.source.id, null).indexOf(e.target.id)
    graph.getOrElse(e.source.id, null).remove(index_edge_from_source)

    var index_edge_from_target = graph.getOrElse(e.target.id, null).indexOf(e.source.id)
    graph.getOrElse(e.target.id, null).remove(index_edge_from_target)

    edgeMap.remove(e.source.id + "-" + e.target.id)
    edgeMap.remove(e.target.id + "-" + e.source.id)
    if (edgeListener != null) {
      edgeListener(e, false)
    }
  }

  def getEdge(src: Int, target: Int): Edge = {
    edgeMap.getOrElse(src + "-" + target, null)
  }

  def removeEdge(src: Int, target: Int): Unit = {
    var edge = getEdge(src, target)
    removeEdge(edge)
  }

  def getAllEdges(): Array[Edge] = {
    val ret = new mutable.HashMap[Int, Edge]()
    edgeMap.foreach(item => {
      ret.put(item._2.id, item._2)
    })
    ret.values.toArray
  }

  def getAllNodes(): Array[Neuron] = {
    nodeMap.values.toArray
  }

  def numberNodes(): Int = {
    nodeMap.size
  }

  def getAllNodesExcept(n: Neuron): Array[Neuron] = {
    val nodes = getAllNodes()
    val ret = ArrayBuffer[Neuron]()
    nodes.foreach(t => {
      if (!n.equals(t)) {
        ret.append(t)
      }
    })
    ret.toArray
  }

  def getEdgesFromNeuron(n: Neuron): Array[Edge] = {
    val listtargets = graph.getOrElse(n.id, null)
    listtargets.map(t => {
      getEdge(n.id, t)
    }).toArray
  }

  def getNeighbourNodes(n: Neuron): Array[Neuron] = {
    val listtargets = graph.getOrElse(n.id, null)
    listtargets.map(t => {
      nodeMap.getOrElse(t, null)
    }).toArray
  }

  def removeNodesWithoutNeighbours() :Int={
    val nodes = getAllNodes()
    var number_removed_nodes = 0
    for (i <- 0 until nodes.length) {
      var edges = graph.getOrElse(nodes(i).id, null)
      if (edges.length == 0) {
        removeNeuron(nodes(i))
        number_removed_nodes = number_removed_nodes + 1
      }
    }
    number_removed_nodes
  }

  def createEdgeBetweenTwoNodes(y1: Neuron, y2: Neuron): Unit = {
    val edge_y1_y2 = getEdge(y1.id, y2.id)
    if (edge_y1_y2 != null) {
      edge_y1_y2.age = 0
    } else {
      var new_edge_y1_y2 = new Edge(genEdgeId(), y1, y2)
      addEdge(new_edge_y1_y2)
    }
  }
}
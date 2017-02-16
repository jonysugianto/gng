package js.minist

import js.gng.{Edge, GNGGraph, Neuron}
import org.graphstream.graph._
import org.graphstream.graph.implementations._

/**
  * Created by jonysugiantohp on 07/11/16.
  */
class MinistGraphViewer {

  val graph = new SingleGraph("Minist Graph Viewer")
  graph.setAttribute("ui.label")

  def init(network:GNGGraph): Unit ={
    val nodes=network.getAllNodes()
    nodes.foreach(n=>{
      neuronListener(n, true)
    })
    val edges=network.getAllEdges()
    edges.foreach(e=>{
      edgeListener(e, true)
    })
  }

  def neuronListener(n:Neuron, append:Boolean): Unit ={
    if(append){
      var neuron_id = n.id.toString
      var node = graph.addNode(neuron_id).asInstanceOf[Node]
      node.setAttribute("ui.label", n.c.asInstanceOf[Object])
    }else{
      //println("vertex removed")
      var neuron_id = n.toString
      graph.removeNode(neuron_id)
    }
  }

  def edgeListener(e:Edge, append:Boolean): Unit ={
    if(append){
      var edge_name = e.source.id.toString + "_" + e.target.id.toString
      graph.addEdge(edge_name, e.source.id.toString, e.target.id.toString)
    }else{
      graph.removeEdge(e.source.id.toString, e.target.id.toString)
    }
  }
}

package js.gng

/**
  * Created by jonysugiantohp on 03/11/16.
  */
class Edge(val id:Int, val source:Neuron, val target:Neuron) extends Serializable{
  var age:Int=0

  override def toString: String = source.id.toString+":"+target.id.toString
}
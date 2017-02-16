package js.gng

import js.mathlib

/**
  * Created by jonysugiantohp on 03/11/16.
  */

class Neuron(val id:Int, val w:mathlib.Vector) extends Serializable{
  var error:Double=Double.MaxValue
  var c:Int=0
  var number_winner:Double=0

  override def equals(obj: scala.Any): Boolean = {
    val n=obj.asInstanceOf[Neuron]
    if(id==n.id){
      return true
    }else{
      return false
    }
  }

  override def toString: String = id.toString

  def computeDistanceTo_w(x:mathlib.Vector):Double={
    w.computeDistance(x)
  }
}

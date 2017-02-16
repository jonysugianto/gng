package js.gng

import js.mathlib

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * Created by jonysugiantohp on 12/11/16.
  */
class SemiSupervisedNetwork extends SupervisedNetwork{

  def compute2MostProbableClass(x:mathlib.Vector, k:Int): Array[(Int,Int)] ={
    var nearestNeurons=findNearestNodeBy_x_w(x)
    var k_nearestNeurons=nearestNeurons.take(k)
    //compute number of class in this list
    var map=new mutable.HashMap[Int, Int]()
    k_nearestNeurons.foreach(n=>{
      if(map.getOrElse(n._1.c, 0)==0){
        map.put(n._1.c, 1)
      }else{
        var counted_so_far=map.getOrElse(n._1.c, 0)
        counted_so_far=counted_so_far+1
        map.put(n._1.c, counted_so_far)
      }
    })
    //compute class probability
    var arrayClassAndNumber=new ArrayBuffer[(Int, Int)]()
    map.foreach(item=>{
      arrayClassAndNumber.append((item._1, item._2))
    })
    var sorted_by_number_class = arrayClassAndNumber.sortWith(_._2 > _._2)
    sorted_by_number_class.toArray
  }

  def computeInformativness(x:mathlib.Vector, membershipSensibility:Double): Unit ={
    null
  }

}

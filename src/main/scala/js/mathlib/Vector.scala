package js.mathlib

/**
  * Created by jonysugiantohp on 14/11/16.
  */
class Vector extends Serializable{
  var data:Array[Double]=null

  override def toString: String = {
    var ret=""
    for(i<-0 until data.length){
      val d="%.2f".format(data(i))
      ret=ret+d+" "
    }
    return ret
  }

  def VectorClone():Vector ={
    Vector(data)
  }

  def copy(offset:Int, values:Array[Double]): Unit ={
    for(i<-0 until values.length) {
      data(i+offset)=values(i)
    }
  }

  def size()=data.length

  def add(v:Vector):Vector={
    var ret=new Vector
    var temp=Array.fill(size())(0d)
    for(i<-0 until temp.length){
      temp(i)=data(i)+v.data(i)
    }
    ret.data=temp
    ret
  }

  def addi(v:Vector):Vector={
    for(i<-0 until data.length){
      data(i)=data(i)+v.data(i)
    }
    this
  }

  def add(scalar:Double):Vector={
    var ret=new Vector
    var temp=Array.fill(size())(0d)
    for(i<-0 until temp.length){
      temp(i)=data(i)+scalar
    }
    ret.data=temp
    ret
  }

  def addi(scalar:Double):Vector={
    for(i<-0 until data.length){
      data(i)=data(i)+scalar
    }
    this
  }

  def sub(v:Vector):Vector={
    var ret=new Vector
    var temp=Array.fill(size())(0d)
    for(i<-0 until temp.length){
      temp(i)=data(i)-v.data(i)
    }
    ret.data=temp
    ret
  }

  def subi(v:Vector):Vector={
    for(i<-0 until data.length){
      data(i)=data(i)-v.data(i)
    }
    this
  }

  def sub(scalar:Double):Vector={
    var ret=new Vector
    var temp=Array.fill(size())(0d)
    for(i<-0 until temp.length){
      temp(i)=data(i)-scalar
    }
    ret.data=temp
    ret
  }

  def subi(scalar:Double):Vector={
    for(i<-0 until data.length){
      data(i)=data(i)-scalar
    }
    this
  }

  def mul(v:Vector):Vector={
    var ret=new Vector
    var temp=Array.fill(size())(0d)
    for(i<-0 until temp.length){
      temp(i)=data(i)*v.data(i)
    }
    ret.data=temp
    ret
  }

  def muli(v:Vector):Vector={
    for(i<-0 until data.length){
      data(i)=data(i)*v.data(i)
    }
    this
  }

  def mul(scalar:Double):Vector={
    var ret=new Vector
    var temp=Array.fill(size())(0d)
    for(i<-0 until temp.length){
      temp(i)=data(i)*scalar
    }
    ret.data=temp
    ret
  }

  def muli(scalar:Double):Vector={
    for(i<-0 until data.length){
      data(i)=data(i)*scalar
    }
    this
  }

  def sum():Double={
    var sum=0.0
    data.foreach(d=>{
      sum=sum+d
    })
    sum
  }

  def makeNonNegative(): Unit ={
    for(i<-0 until data.length){
      if(data(i)<0){
        data(i)=0
      }
    }
  }

  def computeDistance(x:Vector):Double={
    var d0=sub(x)
    var dw=math.sqrt(d0.muli(d0).sum())
    dw
  }
}

object Vector{

  def apply(size:Int):Vector={
    var v=new Vector
    v.data=Array.fill(size)(0d)
    v
  }

  def apply(size:Int, defval:Double):Vector={
    var v=new Vector
    v.data=Array.fill(size)(defval)
    v
  }

  def apply(values:Array[Double]):Vector={
    var v=new Vector
    v.data=values.map(v=>{v})
    v
  }

  def mergeVector(v1:Vector, v2:Vector):Vector={
    val ret=Vector(v1.size()+v2.size())
    ret.copy(0, v1.data)
    ret.copy(v1.size(), v2.data)
    ret
  }

  def mergeVector(vs:Array[Vector]):Vector={
    var ret=mergeVector(vs(0), vs(1))
    for(i<-2 until vs.length){
      ret=mergeVector(ret, vs(i))
    }
    ret
  }
}
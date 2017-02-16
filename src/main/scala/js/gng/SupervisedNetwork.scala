package js.gng

import js.mathlib

class SupervisedNetwork extends BaseNetwork {
  var one_step_learning_counter=0
  var number_unknown_input=0
  var number_known_input=0

  def initSeedNeuron(d1: mathlib.Vector, d2: mathlib.Vector, c1:Int, c2:Int): Unit = {
    var n1 = new Neuron(genNeuronId(), d1)
    var n2 = new Neuron(genNeuronId(), d2)
    n1.c=c1
    n2.c=c2

    addNeuron(n1)
    addNeuron(n2)

    n1.error=0
    n2.error=0

   // var e = new Edge(genEdgeId(),n1, n2)
   // addEdge(e)
  }

  def handleUnknownInput(x: mathlib.Vector, c:Int, twonodes: TwoNearestNode): Unit ={
    var new_weight=x.add(twonodes.y1.w).mul(0.5)
    var new_neuron = create(new_weight)
    new_neuron.error = new_neuron.computeDistanceTo_w(x)
    //createEdgeBetweenTwoNodes(twonodes.y1, twonodes.y2)
    //var new_neuron = create(x)
    new_neuron.c = c
    addNeuron(new_neuron)
    //addNeuron(new_neuron)
    //new_neuron.error=0
    if(twonodes.y1.c==c) {
      createEdgeBetweenTwoNodes(twonodes.y1, new_neuron)
    }

    if(twonodes.y1.c==twonodes.y2.c) {
      createEdgeBetweenTwoNodes(twonodes.y1, twonodes.y2)
    }
  }

  def handleKnownInput(x: mathlib.Vector, c:Int, twonodes: TwoNearestNode, learningrate: Double, maxage:Int): Unit = {
    if (twonodes.y1.c == c) {
      val dw = x.sub(twonodes.y1.w).mul(learningrate)
      twonodes.y1.w.addi(dw)

      twonodes.y1.error = twonodes.y1.error+twonodes.d_y1

      val eta_yn = 0.01 * learningrate
      val neighbours = getNeighbourNodes(twonodes.y1)
      neighbours.foreach(nb => {
        if (c != nb.c) {
          val dw = x.sub(nb.w).mul(eta_yn)
          nb.w.subi(dw)
        }
      })
    } else {
      val dw = x.sub(twonodes.y1.w).mul(learningrate)
      twonodes.y1.w.subi(dw)

      twonodes.y1.error = twonodes.y1.error+twonodes.d_y1

      val eta_yn = 0.01 * learningrate
      val neighbours = getNeighbourNodes(twonodes.y1)
      neighbours.foreach(nb => {
        if (c == nb.c) {
          val dw = x.sub(nb.w).mul(eta_yn)
          nb.w.addi(dw)
        }
      })
    }

    val edges_from_y1 = getEdgesFromNeuron(twonodes.y1)
    edges_from_y1.foreach(e => {
      if(e.source.c==e.target.c) {
        e.age = e.age + 1
      }
    })

    edges_from_y1.foreach(e=>{
      if (e.age > maxage) {
        removeEdge(e)
      }
    })

    if(twonodes.y1.c==twonodes.y2.c) {
      createEdgeBetweenTwoNodes(twonodes.y1, twonodes.y2)
    }
  }

  def one_step_learn(x: mathlib.Vector, c:Int, learningrate: Double,
                     maxage: Int, max_nodes: Int,
                     decay_error: Double, lambda: Int): (Neuron, Double) = {
    one_step_learning_counter=one_step_learning_counter+1
    val twonodes = findTwoNearestNodeBy_x_w_Parallel(x)
    val ty1 = compute_Radius(twonodes.y1, max_nodes)
    if(ty1<twonodes.d_y1){
      number_unknown_input=number_unknown_input+1
      handleUnknownInput(x,c, twonodes)
    }else{
      number_known_input=number_known_input+1
      handleKnownInput(x, c, twonodes, learningrate, maxage)
    }
    decreaseError(decay_error)

    if(one_step_learning_counter > lambda){
      one_step_learning_counter=0
      var removed_node=removeNodesWithoutNeighbours()
      var mean_error = compute_mean_error()
      removed_node=removed_node+remove_low_utility_nodes(max_nodes, mean_error)
      println("removed_node:"+removed_node+" nodes:"+ getAllNodes().length+ "  edges:"+ getAllEdges().length +"  number_unknown_input:" + number_unknown_input + "  number_known_input:" + number_known_input + "  mean_error:" + mean_error)
      number_unknown_input=0
      number_known_input=0
    }
    (twonodes.y1, twonodes.d_y1)
  }

  def compute(x:mathlib.Vector, c:Int, conf:LearnConfig):(Neuron,Double)={
    if(conf.learn){
      one_step_learn(x, c, conf.learningrate, conf.max_age, conf.max_nodes, conf.decay_error, conf.lambda)
    }else{
      val twonodes=findTwoNearestNodeBy_x_w_Parallel(x)
      (twonodes.y1, twonodes.d_y1)
    }
  }
}
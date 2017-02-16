package js.gng

import java.io.{FileInputStream, FileOutputStream, ObjectInputStream, ObjectOutputStream}

/**
  * Created by jonysugiantohp on 10/11/16.
  */
object NetworkPersistent {

  def saveUnSupervisedNetwork(filename: String, network: UnsupervisedNetwork): Unit = {
    try {
      val fout = new FileOutputStream(filename);
      val oos = new ObjectOutputStream(fout);
      oos.writeObject(network);
      oos.close();
    } catch {
      case e: Exception => {
        e.printStackTrace()
      }
    }
  }

  def loadUnSupervisedNetwork(filename: String): UnsupervisedNetwork = {
    try {
      val fin = new FileInputStream(filename);
      val ois = new ObjectInputStream(fin);
      var ret = ois.readObject().asInstanceOf[UnsupervisedNetwork];
      ois.close();
      return ret;
    } catch {
      case e: Exception => {
        e.printStackTrace()
        return null
      }
    }
  }

  def saveSupervisedNetwork(filename: String, network: SupervisedNetwork): Unit = {
    try {
      val fout = new FileOutputStream(filename);
      val oos = new ObjectOutputStream(fout);
      oos.writeObject(network);
      oos.close();
    } catch {
      case e: Exception => {
        e.printStackTrace()
      }
    }
  }

  def loadSupervisedNetwork(filename: String): SupervisedNetwork = {
    try {
      val fin = new FileInputStream(filename);
      val ois = new ObjectInputStream(fin);
      var ret = ois.readObject().asInstanceOf[SupervisedNetwork];
      ois.close();
      return ret;
    } catch {
      case e: Exception => {
        e.printStackTrace()
        return null
      }
    }
  }
}
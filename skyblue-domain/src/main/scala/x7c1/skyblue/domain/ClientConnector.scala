package x7c1.skyblue.domain

import org.apache.tinkerpop.gremlin.driver.{Client, Cluster}
import scala.compat.java8.FutureConverters.CompletionStageOps
import scala.concurrent.{ExecutionContext, Future}

object ClientConnector {
  def apply(): ClientConnector = {
    val create = () => {
      Cluster.build
        .addContactPoint("localhost")
        .port(8182)
        .create()
    }
    new ClientConnector(create)
  }
}

class ClientConnector private (create: () => Cluster) {

  private var cluster: Option[Cluster] = None

  private val timer = new BufferingTimer(delay = 3000)

  def begin()(implicit context: ExecutionContext): Future[Client] = {
    Future {
      currentCluster.connect[Client]()
    }
  }

  def end()(implicit context: ExecutionContext): Unit = {
    timer.touch {
      println("closing cluster...")

      val f1 = currentCluster.closeAsync()
      val f2 = f1.toScala.asInstanceOf[Future[Unit]]
      f2 onComplete {
        tr =>
          println(tr, "closed!!!")
          println("...", currentCluster.isClosed, currentCluster.isClosing)
      }

      println("...", currentCluster.isClosed, currentCluster.isClosing)
    }
  }

  def currentCluster = {
    cluster match {
      case Some(c) =>
        c
      case None =>
        assign()
    }
  }

  def assign(): Cluster = {
    val c = create()
    cluster = Some(c)
    c
  }
}

package x7c1.skyblue.domain

import org.apache.tinkerpop.gremlin.driver.{Client, Cluster}
import x7c1.skyblue.domain.FutureTimer.await

import scala.concurrent.{ExecutionContext, Future}

object ClientConnector {

  def apply(): ClientConnector = {
    new ClientConnectorImpl(create())
  }

  private val create = () => {
    Cluster.build
      .addContactPoint("localhost")
      .port(8182)
      .create()
  }
}

trait ClientConnector {
  def begin()(implicit context: ExecutionContext): Future[Client]
}

private class ClientConnectorImpl(cluster: Cluster) extends ClientConnector {

  override def begin()(implicit context: ExecutionContext): Future[Client] = {
    Future successful cluster.connect[Client]()
  }
}

/**
 * module designed for testing.
 * performance is so slow and not scalable.
 */
object CloseableClientConnector {

  def apply(): CloseableClientConnector = {
    new CloseableClientConnector(create)
  }

  private val create = () => {
    Cluster.build
      .addContactPoint("localhost")
      .port(8182)
      .create()
  }
}

class CloseableClientConnector private (create: () => Cluster) extends ClientConnector {

  private var maybeCluster: Option[Cluster] = None

  private val timer = new BufferingTimer(delay = 3000)

  override def begin()(implicit context: ExecutionContext): Future[Client] = {
    loadCluster() map (_.connect[Client]())
  }

  def end()(implicit context: ExecutionContext): Unit = {
    timer touch {
      println("closing cluster...")
      maybeCluster.foreach(_.closeAsync())
    }
  }

  private def loadCluster()(implicit c: ExecutionContext): Future[Cluster] =
    synchronized {
      maybeCluster match {
        case Some(cluster) if cluster.isClosed =>
          Future successful assign()
        case Some(cluster) if cluster.isClosing =>
          await(millis = 1000) flatMap (_ => loadCluster())
        case Some(cluster) =>
          Future successful cluster
        case _ =>
          Future successful assign()
      }
    }

  private def assign(): Cluster = synchronized {
    val cluster = create()
    maybeCluster = Some(cluster)
    cluster
  }
}

package x7c1.skyblue.domain

import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource

import scala.concurrent.{ExecutionContext, Future}

object SourceProvider {

  def autoCloseable()(implicit c: ExecutionContext): SourceProvider = {
    val default = CloseableClientConnector()
    val provider = new SourceProviderImpl(connector = default)
    new CloseableProviderImpl(provider, onFinish = () => default.end())
  }
}

trait SourceProvider {

  def using[A](block: GraphTraversalSource => A)(
      implicit context: ExecutionContext): Future[A]
}

private class SourceProviderImpl(connector: ClientConnector) extends SourceProvider {

  override def using[A](block: GraphTraversalSource => A)(
      implicit context: ExecutionContext): Future[A] = {

    connector
      .begin()
      .map(DriverRemoteConnection.using(_, "g"))
      .map(AnonymousTraversalSource.traversal().withRemote)
      .map(block)
  }
}

private class CloseableProviderImpl(provider: SourceProvider, onFinish: () => Unit)
  extends SourceProvider {

  override def using[A](block: GraphTraversalSource => A)(
      implicit context: ExecutionContext): Future[A] = {

    val f = provider.using(block)
    f.onComplete(_ => onFinish())
    f
  }
}

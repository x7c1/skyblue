package x7c1.skyblue.domain

import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource

import scala.concurrent.{ExecutionContext, Future}

object SourceProvider {
  def apply(): SourceProvider = {
    val default = ClientConnector()
    new SourceProviderImpl(connector = default)
  }
}

trait SourceProvider {
  def using[A](block: GraphTraversalSource => A)(
    implicit context: ExecutionContext): Future[A]
}

private class SourceProviderImpl(connector: ClientConnector) extends SourceProvider {

  override def using[A](block: GraphTraversalSource => A)(
    implicit context: ExecutionContext): Future[A] = {

    val result = connector
      .begin()
      .map(DriverRemoteConnection.using(_, "g"))
      .map(AnonymousTraversalSource.traversal().withRemote)
      .map(block)

    result.onComplete(_ => connector.end())
    result
  }
}

package x7c1.skyblue.domain.sample

import scala.concurrent.duration.DurationLong
import scala.concurrent.{Await, Future}

object await {
  def unapply[A](arg: Future[A]): Option[A] = {
    val a = Await.result(arg, 10.seconds)
    Some(a)
  }
}

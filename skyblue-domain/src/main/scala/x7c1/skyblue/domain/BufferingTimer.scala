package x7c1.skyblue.domain

import java.util.{Timer, TimerTask}

class BufferingTimer(delay: Int){
  private lazy val timer = new Timer()
  private var task: Option[TimerTask] = None

  def touch[A](f: => A): Unit = synchronized {
    task foreach { _.cancel() }
    task = Some apply new BufferedTask(f)
    task foreach { timer.schedule(_, delay) }
  }
  private class BufferedTask[A](f: => A) extends TimerTask {
    override def run(): Unit = f
  }
}

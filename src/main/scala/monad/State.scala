package monad

abstract class State[S, A] { self =>
  def runState(s: S): (S, A)

  def flatMap[B](f: A => State[S, B]): State[S, B] =
    (s: S) => {
      val (n, a) = self.runState(s)
      f(a).runState(n)
    }

  def map[B](f: A => B): State[S, B] =
    (s: S) => {
      val (n, a) = self.runState(s)
      (n, f(a))
    }
}

object State {
  def apply[S, A](f: S => (S, A)): State[S, A] =
    (s: S) => f(s)

  def unit[S]: State[S, Unit] =
    (s: S) => (s, ())

  def get[S]: State[S, S] =
    (s: S) => (s, s)

  def set[S](newState: S): State[S, Unit] =
    (_: S) => (newState, ())
}
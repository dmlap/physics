package com.github.dmlap.impulse

trait Universe {
  type N
  implicit def nIsNumeric[N: Numeric](n: N): Numeric[N] =
    n: Numeric[N]

  case class Time(val Magnitude: N)
  implicit def timeToN(time: Time) = time.Magnitude

  trait CoordinateSystem {
    type Vector <: Product
    
    case class Position(val Displacement: Vector)
    val Origin: Position

    trait Particle {
      val InitialPosition: Position
      def position(time: Time): Position
    }
  }
}

trait IntUniverse extends Universe {
  type N = Int

  trait CartesianPlaneSystem extends CoordinateSystem {
    type Vector = Tuple2[N, N]
    object Origin extends Position(0, 0)

    implicit def vectorToRichVector(vector: Vector): RichVector =
      RichVector(vector)
    case class RichVector(val Vector: Vector) {
      def *(n: N): Vector = (Vector._1 * n, Vector._2 * n)
      def /(n: N): Vector = (Vector._1 / n, Vector._2 / n)
      def +(rhs: Vector): Vector = (Vector._1 + rhs._1, Vector._2 + rhs._2)
    }

    implicit def positionToVector(position: Position) = position.Displacement
    implicit def positionToRichVector(position: Position) =
      position.Displacement: RichVector

    case class ConstantAccelerationParticle(override val InitialPosition: Position,
					    val InitialVelocity: Vector,
					    val Acceleration: Vector) extends Particle {
      def position(time: Time) =
	Position(InitialPosition + (InitialVelocity * time) + (Acceleration * time * time) / 2)
    }
  }
}

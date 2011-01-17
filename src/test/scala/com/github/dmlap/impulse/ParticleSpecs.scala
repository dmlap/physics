package com.github.dmlap.impulse

import org.specs._

object ParticleSpecs extends Specification("Particle") {
  "ConstantAccelerationParticle" should {
    "remain motionless with zero acceleration and initial velocity" in {
      new IntUniverse {
	new CartesianPlaneSystem {
	  val pos = Position(1, 2)
	  val particle = ConstantAccelerationParticle(pos, (0, 0), (0, 0))
	  particle.position(Time(0))	must_== pos
	  particle.position(Time(1))	must_== pos
	  particle.position(Time(17))	must_== pos
	}
      }
    }
    "move at the initial velocity if there is zero acceleration" in {
      new IntUniverse {
	new CartesianPlaneSystem {
	  val particle = ConstantAccelerationParticle(Position(0, 0), (1, 1), (0, 0))
	  particle.position(Time(0))	must_== particle.InitialPosition
	  particle.position(Time(1))	must_== Position(1, 1)
	  particle.position(Time(17))	must_== Position(17, 17)
	}
      }
    }
    "move at a proportial velocity under acceleration " in {
      new IntUniverse {
	new CartesianPlaneSystem {
	  val particle = ConstantAccelerationParticle(Position(0, 0), (0, 0), (1, 1))
	  particle.position(Time(0)) must_== particle.InitialPosition
	  particle.position(Time(1)) must_== Position(0, 0)
	  particle.position(Time(2)) must_== Position(2, 2)
	  particle.position(Time(4)) must_== Position(8, 8)
	}
      }
    }
  }
}

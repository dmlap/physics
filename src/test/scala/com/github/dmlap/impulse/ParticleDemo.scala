package com.github.dmlap.impulse

import scala.swing._
import java.awt.event.{ ActionEvent, ActionListener }
import java.awt.geom.Ellipse2D
import java.awt.Dimension
import java.util.concurrent.{ Executors, ScheduledFuture, TimeUnit }
import javax.swing.Timer

object ParticleDemo extends SwingApplication {
  val StartTime = System.currentTimeMillis
  val Component = (new IntUniverse {
    val timeScale = 100
    def now: Time = Time(((System.currentTimeMillis - StartTime) / timeScale).toInt)
    val Plane = new CartesianPlaneSystem {

      val particle =
	new ConstantAccelerationParticle(Position(5, 5), (10, 30), (0, -1))

      val Component = new Component {
	private def positionToEllipse(pos: Position): Ellipse2D.Double =
	  new Ellipse2D.Double(pos._1, size.height - pos._2, 10, 10)
	var lastEllipse: Ellipse2D.Double =
	  positionToEllipse(particle.position(now))
        preferredSize = new Dimension(800, 600)
	foreground = new Color(100, 100, 100)
        
	override def paintComponent(graphics: Graphics2D) {
          super.paintComponent(graphics)
	  // clear the old particle
	  graphics.setColor(background)
	  graphics.fill(lastEllipse)
	  // draw the new particle
          graphics.setColor(foreground)
	  lastEllipse = positionToEllipse(particle.position(now))
          graphics.fill(lastEllipse)
        }
      }
    }
  }).Plane.Component
  object MainFrame extends MainFrame {
    title = "Particle Demo"
    contents = Component
    override def closeOperation() {
      shutdown()
      super.closeOperation()
    }
  }
  val Executor = Executors.newSingleThreadScheduledExecutor()
  var task: Option[ScheduledFuture[_]] = None
  override def startup(args: Array[String]) {
    MainFrame.visible = true
    task = Some(Executor.scheduleAtFixedRate(new Runnable {
      def run() {
	Swing.onEDT {
	  Component.repaint()
	}
      }
    }, 0, 16, TimeUnit.MILLISECONDS))
  }
  override def shutdown() {
    task.map(_.cancel(true))
    Executor.shutdownNow()
  }
}

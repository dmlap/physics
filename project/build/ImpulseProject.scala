import sbt._

class ImpulseProject(info: ProjectInfo) extends DefaultProject(info) {
  override def libraryDependencies = 
      Set("org.scala-tools.testing" % "specs_2.8.1" % "1.6.7" % "test->default",
	  "org.scala-lang" % "scala-swing" % "2.8.1" % "test->default") ++
	super.libraryDependencies
}

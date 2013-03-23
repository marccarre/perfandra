import sbt._
import Keys._
import play.Project._
import com.github.play2war.plugin._

object ApplicationBuild extends Build {

  val appName         = "perfandra-gui"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Compatibility with old servlet containers like Apache Tomcat 6:
    Play2WarKeys.servletVersion := "2.5"
  ).settings(Play2WarPlugin.play2WarSettings: _*)

}

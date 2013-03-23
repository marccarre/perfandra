import sbt._
import Keys._
import play.Project._
import com.github.play2war.plugin._
import com.gu.SbtJasminePlugin._

object ApplicationBuild extends Build {

  val appName         = "perfandra-gui"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm
  )


  val main = play.Project(appName, appVersion, appDependencies)
    .settings(Play2WarPlugin.play2WarSettings: _*)
    .settings(jasmineSettings : _*)
    .settings(
      // Compatibility with old servlet containers (e.g. Apache Tomcat 6):
      Play2WarKeys.servletVersion := "2.5",
        
      // Jasmine configuration, overridden as we don't follow the default project structure sbt-jasmine expects
      appJsDir <+= baseDirectory / "app/assets/javascripts",
      appJsLibDir <+= baseDirectory / "public/javascripts",
      jasmineTestDir <+= baseDirectory / "test/assets/javascripts",
      jasmineConfFile <+= baseDirectory / "test/assets/javascripts/jasmineConfFile.js",
      
      // Link Jasmine to the standard 'sbt test' action. 
      // When running 'test' Jasmine tests will be run and after that other Play tests will be executed.
      (test in Test) <<= (test in Test) dependsOn (jasmine)        
  )
}

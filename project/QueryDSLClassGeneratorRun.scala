import sbt.Keys._
import sbt._

object QueryDSLClassGeneratorRun {
  //path to generated sources for querydsl
  val generatedSourcePath = "target/lib_managed"

  // Generate Query dsl classes
  def createQuerydslClasses(base: File,
                            cp: Classpath,
                            mainClass: Option[String],
                            run: ScalaRun,
                            s: TaskStreams): Seq[File] = {
    val scala: File = base / generatedSourcePath

    // Quite hacky way to execute a main method and generate querydsl classes
    run.run("com.mycane.security.model.QueryDSLClassGenerator", cp.files, Seq(generatedSourcePath), s.log)

    (scala ** "*.java").get
  }
}

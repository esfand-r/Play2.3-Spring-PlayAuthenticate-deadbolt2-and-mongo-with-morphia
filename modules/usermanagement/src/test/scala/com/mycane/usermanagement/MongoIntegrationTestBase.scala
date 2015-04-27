package com.mycane.usermanagement

import com.mongodb.MongoClient
import de.flapdoodle.embed.mongo.{Command, MongodExecutable, MongodProcess}
import de.flapdoodle.embed.process.extract.ITempNaming
import org.scalatest.BeforeAndAfterAll
import org.scalatestplus.play.PlaySpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.{ContextConfiguration, TestContextManager}

/**
 * Created by esfandiaramirrahimi on 2015-05-10.
 */
@ContextConfiguration(classes = Array(classOf[TestConfig]))
class MongoIntegrationTestBase extends PlaySpec with BeforeAndAfterAll {
  @Autowired
  private val mongoClient: MongoClient = null

  override def beforeAll() = {
    new TestContextManager(this.getClass()).prepareTestInstance(this)
  }

  override def afterAll() = {
    mongoClient.close()
  }
}

object MongoIntegrationTestBase {
  class BasicExecutableNaming extends ITempNaming {
    def nameFor(prefix: String, postfix: String): String = {
      return Command.MongoD.commandName
    }
  }

}


package com.mycane.usermanagement.service.user

import com.mycane.security.model.usermanagement.{MyCaneUsernamePasswordAuthUser, User}
import com.mycane.usermanagement.MongoIntegrationTestBase
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by esfandiaramirrahimi on 2015-05-10.
 */
class UserServiceIntegrationTest extends MongoIntegrationTestBase {
  @Autowired
  var userService: IUserService = null

  "A User service" should {
    "be able to create a user from Auth User" in {
      val authUser: MyCaneUsernamePasswordAuthUser = new MyCaneUsernamePasswordAuthUser("test", "pass", "test@test.com")
      userService.create(authUser)
      val savedUser: User = userService.findByAuthUserIdentity(authUser)

      savedUser.getEmail mustBe "test@test.com"
    }
  }
}

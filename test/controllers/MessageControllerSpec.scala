package controllers

import connectors.BackendConnector
import play.api.test.Helpers._
import play.api.test._

import scala.concurrent.{ExecutionContext, Future}

class MessageControllerSpec extends ControllerTestResources {

  private val mockBackendConnector = mock[BackendConnector]
  private val messageController = new MessageController(mockBackendConnector, messagesApi, mockCC)

  "TemplateController GET /" should {

    "return OK and show the homepage without a MESSAGE session" in {

      val request = messageController.getHomepage.apply(FakeRequest())

      status(request) shouldBe OK
      contentType(request) shouldBe Some("text/html")
      contentAsString(request) should include("Home")
      contentAsString(request) should not include "Response:"
    }

    "return OK and show the homepage with a MESSAGE session" in {

      val request = messageController.getHomepage.apply(FakeRequest().withSession("MESSAGE" -> "test1"))

      status(request) shouldBe OK
      contentType(request) shouldBe Some("text/html")
      contentAsString(request) should include("Home")
      contentAsString(request) should include("Response: test1")
    }
  }

  "TemplateController POST /" should {

    "redirect to getHomepage with successful form data" in {
      (mockBackendConnector.sendMessage(_: String)(_: ExecutionContext)).expects(*, *).returns(Future successful "response")

      val request = messageController.submitMessage.apply(FakeRequest()
        .withFormUrlEncodedBody(validMessageInputForm: _*))

      status(request) shouldBe SEE_OTHER
      redirectLocation(request).get shouldBe routes.MessageController.getHomepage().url
    }

    "return BAD_REQUEST and show form with errors when form data has errors" in {
      val request = messageController.submitMessage.apply(FakeRequest()
        .withFormUrlEncodedBody(invalidMessageInputForm: _*))

      status(request) shouldBe BAD_REQUEST
      contentAsString(request) should include("You must enter a value")
    }

    "return BAD_REQUEST and show form with errors when missing form data" in {
      val request = messageController.submitMessage.apply(FakeRequest())

      status(request) shouldBe BAD_REQUEST
    }
  }
}

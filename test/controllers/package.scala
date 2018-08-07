import org.scalamock.scalatest.MockFactory
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.i18n.MessagesApi
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.ControllerComponents
import play.api.test.Helpers.stubControllerComponents
import support.UnitSpec

package object controllers {

  trait ControllerTestResources extends UnitSpec with MockFactory with GuiceOneAppPerSuite {

    implicit override lazy val app: Application = new GuiceApplicationBuilder().build()

    protected val messagesApi: MessagesApi = app.injector.instanceOf[MessagesApi]
    protected val mockCC: ControllerComponents = stubControllerComponents()
  }

  val validMessageInputForm = Seq(
    "message" -> "Message1"
  )

  val invalidMessageInputForm = Seq(
    "message" -> ""
  )
}

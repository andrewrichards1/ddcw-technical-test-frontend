package forms

import play.api.libs.json.Json
import support.UnitSpec
import forms.MessageInputForm.messageInputForm
import models.MessageInput
import play.api.data.FormError

class MessageInputFormSpec extends UnitSpec {
  val messageEmptyMessage: String = "message.message-input.form.need-value"
  val messageEmptyFormError: FormError = FormError("message", List(messageEmptyMessage))

  "return no errors with valid data" in {
    val postData = Json.obj("message" → "test1")

    val validatedForm = messageInputForm.bind(postData)

    assert(validatedForm.errors.isEmpty)
  }

  "return an error when input field is left blank" in {
    val postData = Json.obj("message" → "")

    val validatedForm = messageInputForm.bind(postData)

    assert(validatedForm.errors.contains(messageEmptyFormError))
  }

  "return no errors when unbinding the form" in {
    val unboundForm = messageInputForm.mapping.unbind(MessageInput("test1"))

    unboundForm("message") shouldBe "test1"
  }
}
package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class LoginPageV2 {
  public SelenideElement loginInput = $("[data-test-id=login] input");
  public SelenideElement passwordInput = $("[data-test-id=password] input");
  public SelenideElement buttonActionLogin = $("[data-test-id=action-login]");
  public SelenideElement errorNotification = $("[data-test-id=error-notification]");

  public VerificationPage validLogin(DataHelper.AuthInfo info) {
    loginInput.setValue(info.getLogin());
    passwordInput.setValue(info.getPassword());
    buttonActionLogin.click();
    return new VerificationPage();
  }

  public void invalidLogin(DataHelper.AuthInfo info) {
    loginInput.setValue(info.getLogin());
    passwordInput.setValue(info.getPassword());
    buttonActionLogin.click();
    errorNotification.shouldBe(visible);
  }
}
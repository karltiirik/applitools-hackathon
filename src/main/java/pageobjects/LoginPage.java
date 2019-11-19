package pageobjects;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class LoginPage {

    public SelenideElement logo = $(".logo-w");
    public SelenideElement header = $(".auth-header");

    public SelenideElement usernameField = $("#username");
    public SelenideElement passwordField = $("#password");

    private ElementsCollection fieldLabels = $$(".form-group label");
    public SelenideElement firstFieldLabel = fieldLabels.get(0);
    public SelenideElement secondFieldLabel = fieldLabels.get(1);

    public SelenideElement userIcon = $(".os-icon-user-male-circle");
    public SelenideElement passwordIcon = $(".os-icon-fingerprint");

    public SelenideElement loginButton = $("#log-in");

    public SelenideElement checkboxLabel = $(".form-check-label");
    public SelenideElement checkbox = $(".form-check-input");

    public ElementsCollection socialLinks = $$(".buttons-w img");

    public SelenideElement alert = $("[id^=random_id]");

    public LoginPage enterCredentials(String username, String password) {
        usernameField.setValue(username);
        passwordField.setValue(password);
        return this;
    }

    public AppPage login() {
        return login("test_user", "test_password");
    }

    public AppPage login(String username, String password) {
        enterCredentials(username, password);
        loginButton.click();
        return new AppPage();
    }
}

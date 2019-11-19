import com.codeborne.selenide.CollectionCondition;
import com.google.common.collect.Comparators;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pageobjects.AppPage;
import pageobjects.ExpensesPage;
import pageobjects.LoginPage;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.url;
import static pageobjects.AppPage.ColumnNames.AMOUNT;

@RunWith(JUnitParamsRunner.class)
public class TraditionalTests extends BaseTest {

    private LoginPage loginPage = new LoginPage();

    @Before
    public void beforeEachTest() {
        open(baseUrl);
    }

    @Test
    public void testLoginPageUiElements() {
        loginPage.logo.shouldBe(visible);
        loginPage.header.shouldHave(text("Login Form"));

        loginPage.firstFieldLabel.shouldHave(text("Username"));
        loginPage.userIcon.shouldBe(visible);
        loginPage.usernameField.shouldHave(attribute("placeholder", "Enter your username"));

        loginPage.secondFieldLabel.shouldHave(text("Password"));
        loginPage.passwordIcon.shouldBe(visible);
        loginPage.passwordField.shouldHave(attribute("placeholder", "Enter your password"));

        loginPage.loginButton.shouldHave(text("Log In"));
        loginPage.checkboxLabel.shouldHave(text("Remember Me"));
        loginPage.checkbox.shouldNotBe(selected);
        loginPage.socialLinks.shouldHave(CollectionCondition.size(3));
        // COMMENT: This test doesn't catch that the space between checkbox and it's label has increased
    }

    @Test
    @Parameters({
            "test_user, , Password must be present",
            ", test_password, Username must be present",
            ", , Both Username and Password must be present"
    })
    public void testInvalidLoginInput(String username, String password, String expectedErrorMessage) {
        loginPage.enterCredentials(username, password);
        loginPage.loginButton.click();
        loginPage.alert
                .shouldBe(visible)
                .shouldHave(text(expectedErrorMessage))
                .shouldNotBe(CustomCondition.hiddenByZValue());
        // COMMENT: Doesn't catch that the alert's visual look has changed
    }

    @Test
    public void testValidLoginInput() {
        loginPage
                .login("test_user", "test_password")
                .headerBar.shouldBe(visible);
    }

    @Test
    public void testTableSort() {
        AppPage appPage = loginPage.login();

        Map<String, Double> dataBeforeSorting = appPage.getDescriptionsWithAmounts();
        appPage.column(AMOUNT).click();
        Map<String, Double> dataAfterSorting = appPage.getDescriptionsWithAmounts();

        softly.assertThat(Comparators.isInOrder(dataAfterSorting.values(), Comparator.naturalOrder()))
                .as("Check amounts ordered by DESC")
                .isTrue();
        softly.assertThat(dataAfterSorting)
                .as("Check data is in sync")
                .isEqualTo(dataBeforeSorting);
    }

    @Test
    public void testCanvasChartTest() {
        ExpensesPage expensesPage = loginPage
                .login()
                .openCompareExpenses();
        expensesPage.chart.shouldBe(visible);

        // Check labels
        List<String> monthNames = Arrays.asList("January", "February", "March", "April", "May", "June", "July");
        List<String> chartLabels = expensesPage.getChartLabels();
        softly.assertThat(chartLabels)
                .as("Check chart labels")
                .isEqualTo(monthNames);

        // Check 2017 data
        List<Integer> expectedDataForYear2017 = Arrays.asList(10, 20, 30, 40, 50, 60, 70);
        List<Integer> dataForYear2017 = expensesPage.getDataForYear(2017);
        softly.assertThat(dataForYear2017)
                .as("Check 2017 data")
                .isEqualTo(expectedDataForYear2017);

        // Check 2018 data
        List<Integer> expectedDataForYear2018 = Arrays.asList(8, 9, -10, 10, 40, 60, 40);
        List<Integer> dataForYear2018 = expensesPage.getDataForYear(2018);
        softly.assertThat(dataForYear2018)
                .as("Check 2018 data")
                .isEqualTo(expectedDataForYear2018);

        expensesPage.showDataForNextYearButton.click();

        // Check 2017 data is still correct
        dataForYear2017 = expensesPage.getDataForYear(2017);
        softly.assertThat(dataForYear2017)
                .as("Check 2017 data")
                .isEqualTo(expectedDataForYear2017);

        // Check 2018 data is still correct
        dataForYear2018 = expensesPage.getDataForYear(2018);
        softly.assertThat(dataForYear2018)
                .as("Check 2018 data")
                .isEqualTo(expectedDataForYear2018);

        // Check 2019 data
        List<Integer> dataForYear2019 = expensesPage.getDataForYear(2019);
        List<Integer> expectedDataForYear2019 = Arrays.asList(5, 10, 15, 20, 25, 30, 35);
        softly.assertThat(dataForYear2019)
                .as("Check 2019 data")
                .isEqualTo(expectedDataForYear2019);
    }

    @Test()
    public void testDynamicContent() {
        AppPage appPage = loginPage.login();
        // open page with parameter so ads are shown
        open(url() + "?showAd=true");

        appPage.ad1.shouldBe(visible);
        appPage.ad2.shouldBe(visible);
        // COMMENT: Catches only that one of the ads is missing
    }

}
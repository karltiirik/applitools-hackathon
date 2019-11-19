import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.EyesRunner;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Eyes;
import com.codeborne.selenide.WebDriverRunner;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.*;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import pageobjects.ExpensesPage;
import pageobjects.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.url;
import static pageobjects.AppPage.ColumnNames.AMOUNT;

@RunWith(JUnitParamsRunner.class)
public class VisualAITests extends BaseTest {

    @Rule
    public TestName name = new TestName();

    private static Eyes eyes;
    private static WebDriver driver;
    private LoginPage loginPage = new LoginPage();

    @BeforeClass
    public static void before() {
        // Initialize the Runner for your test.
        EyesRunner runner = new ClassicRunner();

        // Initialize the eyes SDK
        eyes = new Eyes(runner);

        // Set your personal Applitools API Key from your environment variables.
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));

        // set batch name
        BatchInfo batch = new BatchInfo("Hackathon batch");
        eyes.setBatch(batch);

        // Get WebDriver instance from Selenide
        driver = WebDriverRunner.getAndCheckWebDriver();
    }

    @Before
    public void openApp() {
        eyes.open(driver,
                "ACME demo app",
                name.getMethodName(),
                new RectangleSize(browserWidth, browserHeight));
        eyes.setForceFullPageScreenshot(false);
        driver.get(baseUrl);
    }

    @Test
    public void testLoginPageUiElements() {
        eyes.checkWindow("Login view");
    }

    @Test
    @Parameters({
            "test_user, , Empty password field",
            ", test_password, Empty username field",
            ", , Both fields empty"
    })
    public void testInvalidLoginInput(String username, String password, String testName) {
        loginPage.enterCredentials(username, password);
        loginPage.loginButton.click();
        eyes.checkWindow("Login view");
    }

    @Test
    public void testValidLoginInput() {
        loginPage.login("test_user", "test_password");
        eyes.checkWindow("Main view");
    }

    @Test
    public void testTableSort() {
        loginPage
                .login()
                .column(AMOUNT).click();
        eyes.setForceFullPageScreenshot(true);
        eyes.checkWindow("Main view");
    }

    @Test
    public void testCanvasChartTest() {
        ExpensesPage expensesPage = loginPage
                .login()
                .openCompareExpenses();
        eyes.checkWindow("Compare expenses view - years 2017, 2018");
        expensesPage.showDataForNextYearButton.click();
        eyes.checkWindow("Compare expenses view - years 2017, 2018, 2019");
    }

    @Test()
    public void testDynamicContent() {
        loginPage.login();
        // open page with parameter so ads are shown
        open(url() + "?showAd=true");
        eyes.checkWindow("Main view with ads");
    }

    @After
    public void afterEachTest() {
        eyes.closeAsync();
    }

    @AfterClass
    public static void after() {
        eyes.abortIfNotClosed();
    }

}
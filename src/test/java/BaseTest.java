import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.junit.SoftAsserts;
import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;

import static com.codeborne.selenide.AssertionMode.SOFT;

public class BaseTest {

    // AUT
    static String baseUrl = "https://demo.applitools.com/hackathonV2.html";

    // Browser settings
    static int browserWidth = 1200;
    static int browserHeight = 800;
    private static String browser = "chrome";
    private static boolean headless = true;

    // Use soft assertion for Selenide & AssertJ
    private SoftAsserts selenideSoftAsserts;
    JUnitSoftAssertions softly;

    @Rule
    public TestRule chain =
            RuleChain.outerRule(selenideSoftAsserts = new SoftAsserts())
                    .around(softly = new JUnitSoftAssertions());

    @BeforeClass
    public static void setConf() {
        Configuration.browser = browser;
        Configuration.headless = headless;
        Configuration.browserSize = String.format("%sx%s", browserWidth, browserHeight);
        Configuration.assertionMode = SOFT;
    }
}

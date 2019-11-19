import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Driver;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomCondition {
    public static Condition hiddenByZValue() {
        return new Condition("hiddenByZValue") {
            @Override
            public boolean apply(Driver driver, WebElement element) {
                // If an element has a z-index lower than 0, it's hidden to the user (but exists in DOM)
                String[] styleElements = element.getAttribute("style").replace(" ", "").split(";");
                Map<String, String> styleElementsMap = Arrays.stream(styleElements)
                        .map(el -> el.split(":"))
                        .collect(Collectors.toMap(e -> e[0], e -> e[1]));
                if (styleElementsMap.containsKey("z-index")) {
                    return Integer.parseInt(styleElementsMap.get("z-index")) < 0;
                } else {
                    return false;
                }
            }
        };
    }
}

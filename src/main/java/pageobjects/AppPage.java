package pageobjects;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.SelenideWait;
import org.openqa.selenium.By;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;

public class AppPage {
    public SelenideElement headerBar = $(".top-bar");
    public SelenideElement showExpensesChartLink = $("#showExpensesChart");
    public SelenideElement ad1 = $("#flashSale").$("img");
    public SelenideElement ad2 = $("#flashSale2").$("img");
    public SelenideElement transactionsTable = $("#transactionsTable");

    public SelenideElement column(ColumnNames colName) {
        return $(colName.locator);
    }

    public Map<String, Double> getDescriptionsWithAmounts() {
        SelenideElement transactionsTableBody = transactionsTable.$("tbody");
        List<Double> amounts = transactionsTableBody
                .$$(".text-right")
                .texts()
                .stream()
                .map(str -> str.replaceAll("[^\\d.-]+", ""))
                .map(Double::parseDouble)
                .collect(Collectors.toList());
        List<String> descriptions = transactionsTableBody
                .$$(".cell-with-media")
                .texts();
        LinkedHashMap<String, Double> descriptionsWithAmounts = new LinkedHashMap<>();
        for (int i = 0; i < descriptions.size(); i++) {
            descriptionsWithAmounts.put(descriptions.get(i), amounts.get(i));
        }
        return descriptionsWithAmounts;
    }

    public ExpensesPage openCompareExpenses() {
        showExpensesChartLink.click();
        return new ExpensesPage();
    }

    public enum ColumnNames {
        AMOUNT("#amount");

        private String locator;

        ColumnNames(String locator) {
            this.locator = locator;
        }
    }
}

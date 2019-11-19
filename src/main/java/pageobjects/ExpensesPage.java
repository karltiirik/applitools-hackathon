package pageobjects;

import com.codeborne.selenide.SelenideElement;

import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.executeJavaScript;

public class ExpensesPage {
    public SelenideElement chart = $("#canvas");
    public SelenideElement showDataForNextYearButton = $("#addDataset");

    public List<String> getChartLabels() {
        return executeJavaScript("return barChartData.labels;");
    }

    public List<Integer> getDataForYear(int year) {
        int years = (int) (long) executeJavaScript("return barChartData.datasets.length;");
        if (years < 1) {
            return null;
        }
        for (int i = 0; i < years; i++) {
            Object yearLabel = executeJavaScript("return barChartData.datasets[" + i + "].label;");
            int dataSetYear = yearLabel instanceof String ? Integer.parseInt((String) yearLabel) : (int) (long) yearLabel;
            if (dataSetYear == year) {
                List<Long> data = executeJavaScript("return barChartData.datasets[" + i + "].data;");
                return data.stream().map(Long::intValue).collect(Collectors.toList());
            }
        }
        return null;
    }
}

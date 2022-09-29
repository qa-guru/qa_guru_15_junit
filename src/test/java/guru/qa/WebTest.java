package guru.qa;

import com.codeborne.selenide.CollectionCondition;
import guru.qa.data.Locale;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class WebTest {

    //ТЕСТОВЫЕ ДАННЫЕ: ["Selenide", "JUnit"]
    @ValueSource(strings = {"Selenide", "JUnit"})
    @ParameterizedTest(name = "Проверка числа результатов поиска в Яндексе для запроса {0}")
    // [test_data] == (String testData)
    void yandexSearchCommonTest(String testData) {
        open("https://ya.ru");
        $("#text").setValue(testData);
        $("button[type='submit']").click();
        $$("li.serp-item")
                .shouldHave(CollectionCondition.size(10))
                .first()
                .shouldHave(text(testData));
    }

    @CsvSource(value = {
            "Selenide, Selenide - это фреймворк для автоматизированного тестирования",
            "JUnit, В этом туториале по JUnit 5 рассказывается о том"
    })
    @ParameterizedTest(name = "Проверка числа результатов поиска в Яндексе для запроса {0}")
    void yandexSearchCommonTestDifferentExpectedText(String searchQuery, String expectedText) {
        open("https://ya.ru");
        $("#text").setValue(searchQuery);
        $("button[type='submit']").click();
        $$("li.serp-item")
                .shouldHave(CollectionCondition.size(10))
                .first()
                .shouldHave(text(expectedText));
    }

    static Stream<Arguments> selenideSiteButtonsTextDataProvider() {
        return Stream.of(
                Arguments.of(List.of("Quick start", "Docs", "FAQ", "Blog", "Javadoc", "Users", "Quotes"), Locale.EN),
                Arguments.of(List.of("С чего начать?", "Док", "ЧАВО", "Блог", "Javadoc", "Пользователи", "Отзывы"), Locale.RU)
        );
    }

    @MethodSource("selenideSiteButtonsTextDataProvider")
    @ParameterizedTest(name = "Проверка отображения названия кнопок для локали: {1}")
    void selenideSiteButtonsText(List<String> buttonsTexts, Locale locale) {
        open("https://selenide.org/");
        $$("#languages a").find(text(locale.name())).click();
        $$(".main-menu-pages a").filter(visible)
                .shouldHave(CollectionCondition.texts(buttonsTexts));
    }

    @EnumSource(Locale.class)
    @ParameterizedTest
    void checkLocaleTest(Locale locale) {
        open("https://selenide.org/");
        $$("#languages a").find(text(locale.name())).shouldBe(visible);
    }
}

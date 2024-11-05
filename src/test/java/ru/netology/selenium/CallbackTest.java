package ru.netology.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CallbackTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);

        driver.get("http://localhost:9999/");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    public void shouldCallback() {
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван Иванович");
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71231231212");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();

        form.findElement(By.cssSelector("button")).click();

        String actualText = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText().trim();
        String expectText = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";

        assertEquals(expectText, actualText);
    }

    @Test
    public void shouldBeValidationName() {
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Ivan Ivanovich");

        form.findElement(By.cssSelector("button")).click();

        String actualText = form.findElement(By.cssSelector(".input_invalid .input__sub")).getText().trim();
        String expectText = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";

        String actualColor = form.findElement(By.cssSelector(".input_invalid .input__sub")).getCssValue("color");
        String expectColor = "rgba(255, 92, 92, 1)";

        assertEquals(expectText, actualText);
        assertEquals(expectColor, actualColor);
    }

    @Test
    public void shouldBeValidationTel() {
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван Иванович");
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("71231231212");

        form.findElement(By.cssSelector("button")).click();

        String actualText = form.findElement(By.cssSelector(".input_invalid .input__sub")).getText().trim();
        String expectText = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";

        String actualColor = form.findElement(By.cssSelector(".input_invalid .input__sub")).getCssValue("color");
        String expectColor = "rgba(255, 92, 92, 1)";

        assertEquals(actualText, expectText);
        assertEquals(expectColor, actualColor);
    }

    @Test
    public void shouldBeValidationAgreement() {
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван Иванович");
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71231231212");

        form.findElement(By.cssSelector("button")).click();

        String actualColor = form.findElement(By.cssSelector(".input_invalid .checkbox__text")).getCssValue("color");
        String expectColor = "rgba(255, 92, 92, 1)";

        assertEquals(expectColor, actualColor);
    }
}

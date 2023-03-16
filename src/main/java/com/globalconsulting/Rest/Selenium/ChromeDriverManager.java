package com.globalconsulting.Rest.Selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

@Component
public class ChromeDriverManager {
    private WebDriver driver;

    public WebDriver getDriver() {
        //Hay que agregarle el options porque hubo una nueva actualizacion de chrome y cambio ciertos parametros.
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        if (driver == null) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver(options);
        }
        return driver;

    }


}


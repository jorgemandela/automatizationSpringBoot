package com.globalconsulting.Rest.Controller;

import com.globalconsulting.Rest.Contact.ContactUsForm;
import com.globalconsulting.Rest.Selenium.ChromeDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AppController {
    private final ChromeDriverManager chromeDriverManager;

    //Con esta anotacion inyectamos la clase ChromeDriverManager.
    @Autowired
    public AppController(ChromeDriverManager chromeDriverManager) {
        this.chromeDriverManager = chromeDriverManager;
    }

    //Utilizamos la anotacion @PostMapping para enviar datos al URL que este caso es localhost:8080/contact-us y con @RequestBody tomamos los datos necesarios que se encuentran en la clase ConcactUsForm.
    @PostMapping(value = "/contact-us")
    public ResponseEntity<Map> sendContactUsForm(@RequestBody ContactUsForm form) {
        WebDriver driver = chromeDriverManager.getDriver();
        driver.get("https://www.consultoriaglobal.com.ar/cgweb/");

        //Espera que la página cargue completamente.
        //Se debe esperar 60 segundos hasta que cargue la página
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        wait.until(ExpectedConditions.titleContains("Consultoría Global"));
        System.out.println("Se accedió a la pagina de Consultoria Global correctamente");

        // Encontrar el elemento de la pestaña "Contáctenos" por su texto
        WebElement contactenosTab = driver.findElement(By.xpath("//a[text()='Contáctenos']"));

        // Hacer clic en la pestaña "Contáctenos"
        contactenosTab.click();
        System.out.println("Accediendo a la pestaña Contáctanos");

        // Buscar los elementos del formulario
        WebElement nombre = driver.findElement(By.name("your-name"));
        WebElement correo = driver.findElement(By.name("your-email"));
        WebElement asunto = driver.findElement(By.name("your-subject"));
        WebElement mensaje = driver.findElement(By.name("your-message"));
        WebElement enviar = driver.findElement(By.cssSelector("input[type='submit']"));

        // Completar los campos del formulario
        nombre.sendKeys(form.getName());
        correo.sendKeys(form.getEmail());
        asunto.sendKeys(form.getSubject());
        mensaje.sendKeys(form.getMessage());
        System.out.println("Fueron cargados los datos necesarios del formulario");

        // Enviar el formulario
        enviar.click();
        System.out.println("Enviando los datos.");

        // Esperar la respuesta del servidor
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(
                                By.cssSelector(".wpcf7-form-control-wrap > span.wpcf7-not-valid-tip")
                        )
                );

        // Comprobar si se ha producido un error en el envío del formulario
        String responseMessage;
        if (element.getText().equals("La dirección e-mail parece inválida.")) {
            responseMessage = "Se accedió a la pógina de manera correcta." +
                    "La pestaña Contáctenos fue encontrada y se accedió a la misma de forma satisfactoria." +
                    "Fueron cargados todos los datos requeridos en el formulario." +
                    "Hubo un problema con la dirección email.";
            System.out.println("Se ha producido un error: dirección de correo inválida");
        } else {
            responseMessage = "El correo proporcionado es correcto.";
        }

        // Cerrar el navegador (Se recomienda comentar este metodo si se desean realizar varias solicitudes al servidor.
        driver.quit();

        // Devolver la respuesta al cliente (De manera simple)
        //return new ResponseEntity<>(responseMessage, HttpStatus.OK);

        // Devolver la respuesta al cliente en formato JSON
        Map<String, String> messages = new HashMap<>();
        messages.put("messages", responseMessage);
        return new ResponseEntity<Map>(messages, HttpStatus.OK);


    }
}
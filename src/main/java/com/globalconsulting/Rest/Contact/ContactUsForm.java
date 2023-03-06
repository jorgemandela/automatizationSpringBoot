package com.globalconsulting.Rest.Contact;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

//En esta clase de servicio establecemos
@Service
@Data
public class ContactUsForm {

    public String name= "nombre";
    public String email= "email";
    public String subject = "subject";
    public String message = "message";
}

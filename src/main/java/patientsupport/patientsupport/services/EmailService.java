package patientsupport.patientsupport.services;

import javax.validation.ValidationException;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import patientsupport.patientsupport.models.email.EmailConfiguration;
import patientsupport.patientsupport.models.email.Mail;



/**
 * EmailService
 */
@Service
public class EmailService {

    private EmailConfiguration emailConfiguration;

    public EmailService(EmailConfiguration emailConfiguration){
        this.emailConfiguration = emailConfiguration;
    }


    @PostMapping
    public void sendMessage(@RequestBody Mail mail, 
                            BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            throw new ValidationException("Model is not valid");
        }

        //Create a mail sender
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(this.emailConfiguration.getHost());
        mailSender.setPort(this.emailConfiguration.getPort());
        mailSender.setUsername(this.emailConfiguration.getUsername());
        mailSender.setPassword(this.emailConfiguration.getPassword());

        // Create an email instance
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(mail.getEmail());
        mailMessage.setTo("yuliusmj@gmail.com");
        mailMessage.setSubject("Task");
        mailMessage.setText("This is a test");

        // send Mail
        mailSender.send(mailMessage);

    }

    
    
}
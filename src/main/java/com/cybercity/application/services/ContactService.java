package com.cybercity.application.services;

import com.cybercity.application.dtos.ContactDTO;
import com.cybercity.application.entities.ContactEntity;
import com.cybercity.application.repositories.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;
    private final JavaMailSender mailSender;
    private final ModelMapper modelMapper;

    @Transactional
    public ContactDTO saveContact(ContactDTO dto) {
       
        ContactEntity entity = modelMapper.map(dto, ContactEntity.class);

        ContactEntity saved = contactRepository.save(entity);

       
        sendEmailToAdmin(saved);

       
        return modelMapper.map(saved, ContactDTO.class);
    }

    private void sendEmailToAdmin(ContactEntity contact) {
        String adminEmail = "abhay28rahul03@gmail.com";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(adminEmail);
        message.setSubject("New Contact Form Submission");
        message.setText(
                "You have received a new contact form submission:\n\n" +
                        "Name: " + contact.getName() + "\n" +
                        "Email: " + contact.getEmail() + "\n" +
                        "Description: " + contact.getDescription()
        );

        mailSender.send(message);
    }
}

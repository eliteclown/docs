package com.cybercity.application.controllers;

import com.cybercity.application.dtos.ContactDTO;
import com.cybercity.application.services.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contact")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ContactController {

    private final ContactService contactService;

    
    @PostMapping
    public ResponseEntity<ContactDTO> createContact(@RequestBody ContactDTO dto) {
        ContactDTO savedContact = contactService.saveContact(dto);
        return ResponseEntity.ok(savedContact);
    }
}

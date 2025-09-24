package com.cybercity.application.controllers;

import com.cybercity.application.dtos.WebinarDTO;
import com.cybercity.application.services.WebinarService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/webinar")
@CrossOrigin(origins = "*")
public class WebinarController {

    private final WebinarService webinarService;

    public WebinarController(WebinarService webinarService) {
        this.webinarService = webinarService;
    }

    @PostMapping("/create")
    public WebinarDTO createWebinar(@RequestBody WebinarDTO webinarDTO) {
        return webinarService.createWebinar(webinarDTO);
    }

    @GetMapping("/get/{id}")
    public WebinarDTO getWebinarById(@PathVariable Long id) {
        return webinarService.getWebinarById(id);
    }

    @GetMapping("/getAll")
    public List<WebinarDTO> getAllWebinars() {
        return webinarService.getAllWebinars();
    }

  
    @DeleteMapping("/delete/{id}")
    public String deleteWebinar(@PathVariable Long id) {
        webinarService.deleteWebinar(id);
        return "Webinar deleted with id " + id;
    }
}

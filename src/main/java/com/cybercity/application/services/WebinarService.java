package com.cybercity.application.services;

import com.cybercity.application.dtos.WebinarDTO;
import com.cybercity.application.entities.WebinarEntity;
import com.cybercity.application.repositories.WebinarRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WebinarService {

    private final WebinarRepository webinarRepo;
    private final ModelMapper modelMapper;

    public WebinarService(WebinarRepository webinarRepo, ModelMapper modelMapper) {
        this.webinarRepo = webinarRepo;
        this.modelMapper = modelMapper;
    }

    public WebinarDTO createWebinar(WebinarDTO webinarDTO) {
        WebinarEntity webinar = modelMapper.map(webinarDTO, WebinarEntity.class);

       

        WebinarEntity saved = webinarRepo.save(webinar);
        return modelMapper.map(saved, WebinarDTO.class);
    }

    public WebinarDTO getWebinarById(Long id) {
        WebinarEntity webinar = webinarRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Webinar not found with id: " + id));
        return modelMapper.map(webinar, WebinarDTO.class);
    }

    public List<WebinarDTO> getAllWebinars() {
        return webinarRepo.findAll()
                .stream()
                .map(entity -> modelMapper.map(entity, WebinarDTO.class))
                .collect(Collectors.toList());
    }

   

    public void deleteWebinar(Long id) {
        WebinarEntity webinar = webinarRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Webinar not found with id: " + id));
        webinarRepo.delete(webinar);
    }
}

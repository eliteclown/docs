package com.cybercity.application.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebinarDTO {
    private Long webinarId;
    private String title;
    private String speaker;
    private String teamChatLink;
    private String image;
    private String description;
    private String webinarDate;
    
    
}

package com.cybercity.application.dtos;

import com.cybercity.application.entities.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private Long paymentId;
    private Long userId;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private String method;
    private Set<PaymentStatus> status;
}

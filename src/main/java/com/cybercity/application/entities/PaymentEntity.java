package com.cybercity.application.entities;


import com.cybercity.application.entities.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "payments")
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long paymentId;

    @JoinColumn(name = "user_id",referencedColumnName = "userId")
    @ManyToOne(fetch = FetchType.EAGER)
    private UserEntity userEntity;

    private String orderId;
    private BigDecimal amount;
    @CreationTimestamp
    private LocalDateTime paymentDate;
    private String method;
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<PaymentStatus> status;
}

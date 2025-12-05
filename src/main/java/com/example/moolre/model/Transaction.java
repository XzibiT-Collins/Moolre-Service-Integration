package com.example.moolre.model;

import com.example.moolre.enums.Currency;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@Table(name = "transactions")
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Transaction type cannot be null")
    private Integer type;
    @NotNull(message = "Channel cannot be null")
    private Integer channel;
    @NotNull(message = "Currency cannot be null")
    Currency currency;
    private BigDecimal amount;
    @NotBlank(message = "Receiver cannot be blank")
    @NotNull(message = "Receiver cannot be null")
    private String receiver;
    private String reference;
    private String sublistid;
    @NotBlank(message = "External Reference cannot be blank")
    @NotNull(message = "External Reference  be null")
    private String externalref;
    private String accountnumber;

    @CreatedDate
    private LocalDateTime createdAt;
}

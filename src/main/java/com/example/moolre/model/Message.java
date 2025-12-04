package com.example.moolre.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@Table(name = "messages")
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Recipient cannot be blank")
    private String recipient;
    @NotBlank(message = "Message body cannot be blank")
    private String messageBody;
    @NotBlank(message = "Message reference cannot be blank")
    private String ref;

    @CreatedDate
    private LocalDateTime createdAt;
}

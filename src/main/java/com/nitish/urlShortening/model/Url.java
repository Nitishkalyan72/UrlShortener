package com.nitish.urlShortening.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Lob
    private String originalUrl;
    private String shortLink;
    private LocalDateTime creationDate;
    private LocalDateTime expirationDate;
}

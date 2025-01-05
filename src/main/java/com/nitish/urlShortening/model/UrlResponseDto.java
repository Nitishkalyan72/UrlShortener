package com.nitish.urlShortening.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlResponseDto {
    private String originalUrl;
    private String shortUrl;
    private LocalDateTime expirationDate;

}

package com.nitish.urlShortening.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlDto {
    private String url;
    private String expirationDate;

}

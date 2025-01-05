package com.nitish.urlShortening.controller;


import com.nitish.urlShortening.model.Url;
import com.nitish.urlShortening.model.UrlDto;
import com.nitish.urlShortening.model.UrlErrorResponseDto;
import com.nitish.urlShortening.model.UrlResponseDto;
import com.nitish.urlShortening.service.UrlService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.time.LocalDateTime;

@RestController
public class UrlShorteningController {
    @Autowired
    private UrlService urlService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateShortLink(@RequestBody UrlDto urlDto) {
        Url urlToReturn = urlService.generateShortLink(urlDto);
        if(urlToReturn != null){
            UrlResponseDto urlResponseDto = new UrlResponseDto();
            urlResponseDto.setShortUrl(urlToReturn.getShortLink());
            urlResponseDto.setOriginalUrl(urlToReturn.getOriginalUrl());
            urlResponseDto.setExpirationDate(urlToReturn.getExpirationDate());
            return new ResponseEntity<UrlResponseDto>(urlResponseDto,HttpStatus.OK);
        }
        UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
        urlErrorResponseDto.setStatus("404");
        urlErrorResponseDto.setError("unable to generate shortLink, Please try again");
        return new ResponseEntity<>(urlErrorResponseDto,HttpStatus.OK);

    }

    @GetMapping("/{shortLink}")
    public ResponseEntity<?> redirectToOriginalUrl(@PathVariable String shortLink,HttpServletResponse response) throws IOException {//@PathVariable annotation in Spring Boot is used to extract a value from the URI path and bind it to a method parameter in a controller.
        if(StringUtils.isEmpty(shortLink)){
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
            urlErrorResponseDto.setStatus("400");
            urlErrorResponseDto.setError("Invalid Url");
            return new ResponseEntity<>(urlErrorResponseDto,HttpStatus.OK);
        }
        Url urlToRet = urlService.getEncodedUrl(shortLink);
        if(urlToRet == null){
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
            urlErrorResponseDto.setStatus("400");
            urlErrorResponseDto.setError("Url does not exist or it might be expired");
            return new ResponseEntity<>(urlErrorResponseDto,HttpStatus.OK);
        }
        if(urlToRet.getExpirationDate().isBefore(LocalDateTime.now())){
            urlService.deleteShortLink(urlToRet);
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
            urlErrorResponseDto.setStatus("200");
            urlErrorResponseDto.setError("Url expired,Please generate new one");
            return new ResponseEntity<>(urlErrorResponseDto,HttpStatus.OK);
        }
        response.sendRedirect(urlToRet.getOriginalUrl());
        return null;

    }

}

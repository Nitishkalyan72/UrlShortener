package com.nitish.urlShortening.service;


import com.nitish.urlShortening.model.Url;
import com.nitish.urlShortening.model.UrlDto;
import org.springframework.stereotype.Service;

@Service
public interface UrlService {
    public Url generateShortLink(UrlDto urlDto);
    public Url persistShortLink(Url url);
    public Url getEncodedUrl(String url);
    public void deleteShortLink(Url url);
}

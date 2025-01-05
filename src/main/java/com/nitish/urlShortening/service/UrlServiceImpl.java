package com.nitish.urlShortening.service;

import com.google.common.hash.Hashing;
import com.nitish.urlShortening.model.Url;
import com.nitish.urlShortening.model.UrlDto;
import com.nitish.urlShortening.repository.UrlRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Service
public class UrlServiceImpl implements UrlService {

    @Autowired
    private UrlRepository urlRepository;

    @Override
    public Url generateShortLink(UrlDto urlDto) {
        if (StringUtils.isNotEmpty(urlDto.getUrl())){
            String encodedUrl = encodeUrl(urlDto.getUrl());
            Url urlToPersist = new Url();
            urlToPersist.setCreationDate(LocalDateTime.now());
            urlToPersist.setOriginalUrl(urlDto.getUrl());
            urlToPersist.setShortLink(encodedUrl);
            urlToPersist.setExpirationDate(getExpirationDate(urlDto.getExpirationDate(),urlToPersist.getCreationDate()));//If expiry date given by user then well in good otherwise we will make our own expiry date
            Url urlToReturn = persistShortLink(urlToPersist);
            if(urlToReturn != null)
                return urlToReturn;

            return null;

        }
        return null;
    }

    private LocalDateTime getExpirationDate(String expirationDate, LocalDateTime creationDate) {
        if(StringUtils.isBlank(expirationDate)){
            return creationDate.plusSeconds(60);
        }
        LocalDateTime expirationDateToRet = LocalDateTime.parse(expirationDate);
        return expirationDateToRet;


    }

    private String encodeUrl(String url) {
        String encodedUrl = "";
        LocalDateTime time = LocalDateTime.now();
        encodedUrl = Hashing.murmur3_32()
                .hashString(url.concat(time.toString()), StandardCharsets.UTF_8)
                .toString();
        return encodedUrl;
    }

    @Override
    public Url persistShortLink(Url url) {
        Url urlToReturn = urlRepository.save(url);
        return urlToReturn;
    }

    @Override
    public Url getEncodedUrl(String url) {
        Url urlToReturn = urlRepository.findByShortLink(url);
        return urlToReturn;
    }

    @Override
    @Transactional //With @Transactional, Spring guarantees that either all operations succeed or nothing is saved, ensuring database consistency.
    public void deleteShortLink(Url url) {
        urlRepository.delete(url);
    }
}

package edu.wgu.d387_sample_code.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newFixedThreadPool;

class LocaleResource implements Callable<String> {
    String property;
    String language;
    String country;
    public LocaleResource(String property, String language, String country) {
        this.property = property;
        this.language = language;
        this.country = country;
    }

    @Override
    public String call() {
        Locale locale = new Locale(language, country);
        ResourceBundle resourceBundle = ResourceBundle.getBundle("locale", locale);
        return resourceBundle.getString(property);
    }
}

@RestController
@RequestMapping("/locale")
@CrossOrigin
public class LocaleController {

    @GetMapping("welcome")
    public ResponseEntity<List<String>> getWelcomeMessages() {
        ExecutorService es = newFixedThreadPool(2);
        List<String> result = new ArrayList<>();

        List<Future<String>> futures = new ArrayList<>();
        futures.add(es.submit(new LocaleResource("welcome", "en", "US")));
        futures.add(es.submit(new LocaleResource("welcome", "fr", "CA")));

        // block and wait for the results. result order always is "en" -> "fr"
        // Note: no need a thread-safe List / synchronizedList
        for (Future<String> future:futures) {
            try {
                result.add(future.get(100, TimeUnit.MILLISECONDS));
            } catch (Exception e) {
                // Timeout
            }
        }

        return ResponseEntity.ok(result);
    }
}

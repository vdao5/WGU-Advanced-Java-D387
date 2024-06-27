package edu.wgu.d387_sample_code.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/time")
@CrossOrigin
public class TimeController {
    // Daily live time, in UTC
    private static String liveTime = "06:30:00";
    private static String PATTERN = "MM/DD/yyyy, hh:mm a";

    @GetMapping("/live")
    public ResponseEntity<List<String>> getLiveTime() {

        // Set live time
        Instant instant = Instant.now()
                .atZone(ZoneOffset.UTC)
                .with(LocalTime.parse(liveTime))
                .toInstant();

        // Add 1 day if passed
        if (instant.isBefore(Instant.now())) {
            instant = instant.plusSeconds(60*60*24);
        }

        List<String> result = new ArrayList<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(PATTERN);

        result.add(instant.atZone(ZoneId.of("US/Eastern")).format(dtf));
        result.add(instant.atZone(ZoneId.of("US/Mountain")).format(dtf));
        result.add(instant.atZone(ZoneId.of("UTC")).format(dtf));

        return ResponseEntity.ok(result);
    }
}

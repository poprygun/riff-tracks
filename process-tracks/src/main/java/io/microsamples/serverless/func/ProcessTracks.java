package io.microsamples.serverless.func;

import lombok.Builder;
import lombok.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import reactor.core.publisher.Flux;

import java.util.UUID;
import java.util.function.Function;

@SpringBootConfiguration
@EnableAutoConfiguration
public class ProcessTracks implements Function<Flux<Track>, Flux<ProcessedTrack>>{


    public static void main(String[] args) {
        SpringApplication.run(ProcessTracks.class, args);
    }

    @Override
    public Flux<ProcessedTrack> apply(Flux<Track> trackFlux) {
        return trackFlux.map(t -> ProcessedTrack.builder().id(t.getId())
                .latitude(t.getLatitude())
                .longitude(t.getLongitude())
                .processed(true).build());
    }
}

@Value
@Builder
class Track {
    private UUID id;
    private double latitude;
    private double longitude;
}

@Value
@Builder
class ProcessedTrack {
    private UUID id;
    private double latitude;
    private double longitude;
    private boolean processed;
}



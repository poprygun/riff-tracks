package io.microsamples.serverless.func;

import lombok.Builder;
import lombok.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

import java.util.UUID;
import java.util.function.Supplier;

import static io.github.benas.randombeans.api.EnhancedRandom.randomStreamOf;

@SpringBootApplication
public class GenerateTracksApplication {

    @Bean
    Supplier<Flux<Track>> generateTracks() {
        return () -> Flux.fromStream(randomStreamOf(10, Track.class));
    }

    public static void main(String[] args) {
        SpringApplication.run(GenerateTracksApplication.class, args);
    }

}

@Value
@Builder
class Track {
    private UUID id;
    private double latitude;
    private double longitude;
}




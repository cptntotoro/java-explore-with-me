package ru.practicum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

public class StatsClient {
    private final String statsServerUrl;
    private final WebClient webClient;

    public StatsClient(@Value("${stats.server.url}") String statsServerUrl) {
        this.statsServerUrl = statsServerUrl;
        webClient = WebClient.create(statsServerUrl);
    }

    public void addRequest(RequestDto requestDto) {
        webClient.post().uri("/hit").bodyValue(requestDto).retrieve().bodyToMono(Object.class).block();
    }

    public ResponseEntity<List<RequestOutputDto>> getStats(String start,
                                                           String end,
                                                           List<String> uris,
                                                           Boolean unique) {

        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/stats")
                            .queryParam("start", start)
                            .queryParam("end", end);
                    if (uris != null)
                        uriBuilder.queryParam("uris", String.join(",", uris));
                    if (unique != null)
                        uriBuilder.queryParam("unique", unique);
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ResponseEntity<List<RequestOutputDto>>>() {})
                .block();
    }
}
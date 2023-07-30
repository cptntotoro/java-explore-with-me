package ru.practicum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

public class StatsClient {
    private final WebClient webClient;

    public StatsClient(@Value("${stats.server.url}") String statsServerUrl) {
        webClient = WebClient.create(statsServerUrl);
    }

    public void addRequest(RequestDto requestDto) {
        webClient.post().uri("/hit").bodyValue(requestDto).retrieve().bodyToMono(Object.class).block();
    }

    public ResponseEntity<List<RequestOutputDto>> getStats(String start,
                                                           String end,
                                                           List<String> uris,
                                                           Boolean unique) {

        ResponseEntity<List<RequestOutputDto>> listResponseEntity = webClient.get()
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
                .toEntityList(RequestOutputDto.class)
                .block();
        return listResponseEntity;
    }

    public ResponseEntity<List<RequestOutputDto>> getStatsByIp(String start,
                                                           String end,
                                                           List<String> uris,
                                                           Boolean unique,
                                                           String ip) {

        ResponseEntity<List<RequestOutputDto>> listResponseEntity = webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/statsByIp")
                            .queryParam("start", start)
                            .queryParam("end", end)
                            .queryParam("ip", ip);
                    if (uris != null)
                        uriBuilder.queryParam("uris", String.join(",", uris));
                    if (unique != null)
                        uriBuilder.queryParam("unique", unique);
                    return uriBuilder.build();
                })
                .retrieve()
                .toEntityList(RequestOutputDto.class)
                .block();
        return listResponseEntity;
    }
}
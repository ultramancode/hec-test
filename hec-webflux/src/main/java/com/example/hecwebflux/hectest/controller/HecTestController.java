package com.example.hecwebflux.hectest.controller;

import com.example.hecwebflux.hectest.dto.response.DateResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;


@RestController
@RequestMapping("/webflux-api/v1/hec-test")
public class HecTestController {
    private final WebClient webClient;
    private static final String BASE_URL = "http://date.jsontest.com";

    public HecTestController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
    }

    // 단건 요청 API
    @GetMapping("/date")
    public Mono<ResponseEntity<DateResponseDto>> getDate() {
        return webClient.get()
                .retrieve()
                .bodyToMono(DateResponseDto.class)
                .map(dateResponse -> ResponseEntity.ok().body(dateResponse))
                .onErrorResume(WebClientResponseException.class, ex -> {
                    if (ex.getStatusCode().is4xxClientError()) {
                        return Mono.just(ResponseEntity.status(ex.getStatusCode()).build());
                    } else {
                        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                    }
                })
                .timeout(Duration.ofSeconds(5)); // 시간 제한 설정
    }

    // 다건요청 : 에러 발생 시 전체 flux 중단되니, Mono.empty() 반환해서 flux 비워 주는 방식으로 대체

    // flatMap 방식의 다건 요청
    @GetMapping("/dates/flat-map")
    public Flux<String> getDatesByFlatMap() {
        return Flux.range(1, 3)
                .flatMap(i -> webClient.get().retrieve().bodyToMono(String.class))
                .onErrorResume(throwable -> {
                    System.err.println("실패" + throwable.getMessage());
                    return Mono.empty();
                });
    }
    // concatMap 방식의 다건 요청
    @GetMapping("/dates/concat-map")
    public Flux<String> getDatesByConcatMap() {
        return Flux.range(1, 3)
                .concatMap(i -> webClient.get().retrieve().bodyToMono(String.class))
                .onErrorResume(throwable -> {
                    System.err.println("실패: " + throwable.getMessage());
                    return Mono.empty();
                });
    }

}

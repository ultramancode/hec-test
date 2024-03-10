package hectest;

import com.example.hecwebflux.hectest.dto.response.DateResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(classes = {com.example.hecwebflux.HecWebfluxApplication.class})
@AutoConfigureWebTestClient
public class HecTestControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testGetDate() {
        webTestClient.get()
                .uri("/webflux-api/v1/hec-test/date")
                .exchange()
                .expectStatus().isOk()
                .expectBody(DateResponseDto.class);
    }

    @Test
    public void testGetDatesByFlatMap() {
        webTestClient.get()
                .uri("/webflux-api/v1/hec-test/dates/flat-map")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(String.class);
    }

    @Test
    public void testGetDatesByConcatMap() {
        webTestClient.get()
                .uri("/webflux-api/v1/hec-test/dates/concat-map")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(String.class);
    }
}

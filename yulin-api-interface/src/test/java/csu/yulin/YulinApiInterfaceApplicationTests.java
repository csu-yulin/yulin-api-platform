package csu.yulin;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneId;

@SpringBootTest
class YulinApiInterfaceApplicationTests {

    @Test
    void contextLoads() {

        LocalDateTime localTime = LocalDateTime.now(ZoneId.of("a"));

    }

}

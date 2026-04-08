package com.tadiwanashe.wms;

import com.tadiwanashe.wms.config.TestCacheConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestCacheConfig.class)
class WmsOrderOrchestratorApplicationTests {
	@Test
	void contextLoads() {
	}
}

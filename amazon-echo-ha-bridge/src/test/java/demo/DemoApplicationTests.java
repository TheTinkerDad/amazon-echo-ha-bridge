package demo;

import com.armzilla.ha.SpringbootEntry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringbootEntry.class)
@TestPropertySource(locations="classpath:test.properties")
@WebAppConfiguration
public class DemoApplicationTests {

	@Test
	public void contextLoads() {
		
	}
}

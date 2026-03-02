package site.shazan.ecommerce.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.route.RouteLocator;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class GatewayApplicationTests {

	@Autowired
	private RouteLocator routeLocator;

	@Test
	void contextLoads() {
	}

	@Test
	void testRoutesAreLoaded() {
		assertNotNull(routeLocator, "RouteLocator should be loaded");
		// Routes should be populated
		routeLocator.getRoutes().subscribe(route ->
			System.out.println("Route loaded: " + route.getId())
		);
	}

}

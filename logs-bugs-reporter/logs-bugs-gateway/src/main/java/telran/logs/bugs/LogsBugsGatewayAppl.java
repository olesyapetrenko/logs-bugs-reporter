package telran.logs.bugs;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.webflux.ProxyExchange;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import telran.logs.bugs.dto.AuthData;
import telran.logs.bugs.security.authentication.JwtUtil;
import telran.logs.bugs.security.configuration.SecurityConfiguration;
import telran.logs.bugs.service.ProxyService;
    
@SpringBootApplication
@RestController 
public class LogsBugsGatewayAppl {
	@Autowired
	ProxyService proxyService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	JwtUtil jwtUtil;
	@Autowired
	ConcurrentHashMap<String, UserDetails> users;
static Logger LOG = LoggerFactory.getLogger(LogsBugsGatewayAppl.class);
	public static void main(String[] args) {
		SpringApplication.run(LogsBugsGatewayAppl.class, args);

	}
	@PostMapping(value=SecurityConfiguration.LOGIN)
	Mono<ResponseEntity<String>> login(@RequestBody AuthData authData) {
		UserDetails userDetails = users.get(authData.username);
		if(userDetails == null ||
				!passwordEncoder.matches(authData.password, userDetails.getPassword())) {
			return Mono.just(ResponseEntity.badRequest().body("wrong credentials"));
		}
		return Mono.just(ResponseEntity
				.ok(jwtUtil.generateToken(authData.username,
						userDetails.getAuthorities().stream()
						.map(a -> a.getAuthority()).toArray(String[]::new))));
	}
	
	@PostMapping("/**")
	public Mono<ResponseEntity<byte[]>> postRequestsProxy(ProxyExchange<byte[]> proxy,
			ServerHttpRequest request) {
		
		return proxyService.proxyRun(proxy, request, HttpMethod.POST);

	}
	@GetMapping("/**")
	public Mono<ResponseEntity<byte[]>> getRequestsProxy(ProxyExchange<byte[]> proxy,
			ServerHttpRequest request) {
		
		return proxyService.proxyRun(proxy, request, HttpMethod.GET);

	}
	
	@PutMapping("/**")
	public Mono<ResponseEntity<byte[]>> putRequestsProxy(ProxyExchange<byte[]> proxy,
			ServerHttpRequest request) {

		return proxyService.proxyRun(proxy, request, HttpMethod.PUT);

	}
	@DeleteMapping("/**")
	public Mono<ResponseEntity<byte[]>> deleteRequestsProxy(ProxyExchange<byte[]> proxy,
			ServerHttpRequest request) {

		return proxyService.proxyRun(proxy, request, HttpMethod.DELETE);
	

	}
}

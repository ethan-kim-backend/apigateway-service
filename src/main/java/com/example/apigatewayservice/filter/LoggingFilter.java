package com.example.apigatewayservice.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {
	public LoggingFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		//Custom Pre Filter

		//exchange, chain은 매개변수
		// return (exchange, chain) -> {
		// 	ServerHttpRequest request = exchange.getRequest();
		// 	ServerHttpResponse response = exchange.getResponse();
		//
		// 	log.info("Global filter baseMessage: {}", config.getBaseMessage());
		//
		// 	if (config.isPreLogger()) {
		// 		log.info("Global Filter Start: request id -> {}", request.getId());
		// 	}
		//
		// 	//Custome Post Filter
		// 	return chain.filter(exchange).then(Mono.fromRunnable(()->{
		//
		// 		if (config.isPostLogger()) {
		// 			log.info("Global Filter End: response code -> {}", response.getStatusCode());
		// 		}
		// 	}));
		// };
		GatewayFilter filter = new OrderedGatewayFilter((exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			ServerHttpResponse response = exchange.getResponse();

			log.info("Logging filter baseMessage: {}", config.getBaseMessage());

			if (config.isPreLogger()) {
				log.info("Logging Pre filter: request id -> {}", request.getId());
			}

			//Custome Post Filter
			return chain.filter(exchange).then(Mono.fromRunnable(() -> {
				if (config.isPostLogger()) {
					log.info("Logging Post filter: response code -> {}", response.getStatusCode());
				}
			}));
		}, Ordered.LOWEST_PRECEDENCE);

		return filter;
	}

	@Data
	public static class Config {
		//put the configuration properties
		private String baseMessage;
		private boolean preLogger;
		private boolean postLogger;
	}

}

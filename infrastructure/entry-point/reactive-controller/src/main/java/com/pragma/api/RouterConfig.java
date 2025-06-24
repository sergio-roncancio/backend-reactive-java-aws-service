package com.pragma.api;

import com.pragma.api.transfers.TransferController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterConfig {

    @Bean
    RouterFunction<ServerResponse> routerFunction(TransferController transferController){
        return route(POST("/transfer"), transferController::makeTransfer)
                .andRoute(POST("/movement"), transferController::getMovements);
    }

}

package com.pragma.app.beans;

import com.pragma.usecase.account.ValidationAccount;
import com.pragma.usecase.transfer.factory.TransferFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigApplicationBeans {

    @Bean
    ValidationAccount validationAccount(){
        return new ValidationAccount();
    }

    @Bean
    TransferFactory transferFactory(ValidationAccount validationAccount){
        return new TransferFactory(validationAccount);
    }

}

package com.pragma.domain.account;

import com.pragma.domain.account.enums.State;
import com.pragma.domain.account.enums.Type;
import com.pragma.domain.exceptions.AccountException;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.pragma.domain.exceptions.error.impl.ErrorDefinition.NOT_FOUNDS_ACCOUNT;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Account {

     private final int id;
     private final String number;
     private BigDecimal balance;
     private final Type type;
     private State state;
     private final LocalDateTime creationDate;
     @Setter
     private LocalDateTime modificationDate;

     public boolean isActive(){
         return state.equals(State.ACTIVE);
     }

     public void addMoney(BigDecimal amount){
         this.balance = this.balance.add(amount);
     }

     public void subtractMoney(BigDecimal amount) throws AccountException {
         var newBalance = this.balance.subtract(amount);
         if(newBalance.compareTo(BigDecimal.ZERO) < 0){
             throw new AccountException(NOT_FOUNDS_ACCOUNT);
         }
         this.balance = newBalance;
     }

}

package com.pragma.r2dbc.account.repository;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Table("account")
public class AccountEntity {

    @Id
    @Column("id")
    private int id;
    @Column("number")
    private String number;
    @Column("balance")
    private BigDecimal balance;
    @Column("type")
    private String type;
    @Column("state")
    private String state;
    @Column("creation_date")
    private LocalDateTime creationDate;
    @Column("modification_date")
    private LocalDateTime modificationDate;

}

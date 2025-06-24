package com.pragma.r2dbc.transfer.repository;

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
@Table("transfer")
public class TransferEntity {

    @Id
    @Column("id")
    private int id;
    @Column("amount")
    private BigDecimal amount;
    @Column("account_from")
    private String numberAccountFrom;
    @Column("account_to")
    private String numberAccountTo;
    @Column("date")
    private LocalDateTime date;

}

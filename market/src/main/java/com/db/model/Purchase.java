package com.db.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "purchases")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "seller_id")
    private Integer sellerId;

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "item_id")
    private Integer itemId;

    @Column(name = "purchase_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate purchaseDate;

    @Column(name = "price")
    private BigDecimal price;
}

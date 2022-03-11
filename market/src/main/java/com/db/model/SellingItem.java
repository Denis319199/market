package com.db.model;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "selling_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SellingItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "item_id")
  private Integer itemId;

  @Column(name = "seller_id")
  private Integer sellerId;

  @Column(name = "price")
  private BigDecimal price;
}

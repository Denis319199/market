package com.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users_items")
@IdClass(UsersItemId.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsersItem {
    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Id
    @Column(name = "item_id")
    private Integer itemId;

    @Column(name = "quantity")
    private Integer quantity;
}

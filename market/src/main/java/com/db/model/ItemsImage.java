package com.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "items_images")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemsImage {
    @Id
    @Column(name = "item_id")
    private Integer itemId;

    @Type(type = "org.hibernate.type.BinaryType")
    @Column(name = "image")
    private byte[] image;
}

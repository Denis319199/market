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
@Table(name = "users_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsersImage {
    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Type(type="org.hibernate.type.BinaryType")
    @Column(name = "image")
    byte[] image;
}

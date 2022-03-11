package com.db.model;

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
import org.hibernate.annotations.Type;

@Entity
@Table(name = "games_images")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GamesImage {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "game_id")
  private Integer gameId;

  @Type(type = "org.hibernate.type.BinaryType")
  @Column(name = "image")
  private byte[] image;
}

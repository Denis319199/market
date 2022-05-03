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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "games")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Game {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "developer_id")
  private Integer developerId;

  @Column(name = "release_date")
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate releaseDate;

  @Column(name = "price")
  private BigDecimal price;

  @Column(name = "total_image_count")
  private Integer totalImageCount;
}

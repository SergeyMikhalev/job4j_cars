package ru.job4j.cars.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "price_history")
public class PriceHistoryUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private long before;
    private long after;
    private LocalDateTime created;
}

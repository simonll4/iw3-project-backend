package ar.edu.iw3.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Entity
@Table(name = "details")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Detail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_order", nullable = false)
    private Order order;

    @Column(nullable = false)
    private Date timeStamp;

    @Column(nullable = false)
    private float accumulatedMass;

    @Column(nullable = false)
    private float density;

    @Column(nullable = false)
    private float temperature;

    @Column(nullable = false)
    private float flowRate;

}

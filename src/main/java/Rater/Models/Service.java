package Rater.Models;

import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "services")
public class Service {
    @Id
    private UUID id;
    @ManyToOne
    @JoinColumn(name="app_id", nullable=false)
    private App app;

    /*@OneToMany(mappedBy = "api")
    private Set<API> apis;*/
}

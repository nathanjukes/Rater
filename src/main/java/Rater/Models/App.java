package Rater.Models;

import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table
public class App {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;

    @OneToMany(mappedBy = "app")
    private Set<Service> services;

    @ManyToOne
    @JoinColumn(name="org_id", nullable=false)
    private Org org;
}

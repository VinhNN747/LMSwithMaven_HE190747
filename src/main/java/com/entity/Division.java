/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Division")
public class Division {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DivisionID")
    private Integer divisionId;

    @NotNull
    @Size(max = 50)
    @Column(name = "DivisionName", nullable = false)
    private String divisionName;

    @Size(max = 10)
    @Column(name = "DivisionDirector")
    private String divisionDirector;

    // Navigation property for the director
    @ManyToOne
    @JoinColumn(name = "DivisionDirector", referencedColumnName = "UserID", insertable = false, updatable = false)
    private User director;

    // Navigation property for users in this division
    @OneToMany(mappedBy = "division", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> users = new ArrayList<>();

    // Getters and Setters
    public Integer getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(Integer divisionId) {
        this.divisionId = divisionId;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public String getDivisionDirector() {
        return divisionDirector;
    }

    public void setDivisionDirector(String divisionDirector) {
        this.divisionDirector = divisionDirector;
    }

    public User getDirector() {
        return director;
    }

    public void setDirector(User director) {
        this.director = director;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}

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

    @Column(name = "DivisionHead")
    private Integer divisionHead;

    // Navigation property for the directorz
    @ManyToOne
    @JoinColumn(name = "DivisionHead", referencedColumnName = "UserID", insertable = false, updatable = false)
    private User head;

    // Navigation property for users in this division
    @OneToMany(mappedBy = "division")
    private List<User> users;

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

    public Integer getDivisionHead() {
        return divisionHead;
    }

    public void setDivisionHead(Integer divisionHead) {
        this.divisionHead = divisionHead;
    }

    public User getHead() {
        return head;
    }

    public void setHead(User head) {
        this.head = head;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}

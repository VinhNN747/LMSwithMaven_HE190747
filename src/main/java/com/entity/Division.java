/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Division")
public class Division {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int divisionID;

    @Column(nullable = false, length = 50)
    private String divisionName;

    @Column(length = 10)
    private String divisionDirector;

    // Getters and setters
    public int getDivisionID() {
        return divisionID;
    }

    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
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
}

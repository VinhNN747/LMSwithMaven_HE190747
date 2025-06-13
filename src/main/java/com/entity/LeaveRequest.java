/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "LeaveRequest")
public class LeaveRequest implements Serializable {

    @Id
    @Column(length = 20)
    private String leaveRqID;

    @ManyToOne
    @JoinColumn(name = "senderID")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "approverID")
    private User approver;

    @Lob
    private String reason;

    @Column(length = 20)
    private String status;

    // Getters and Setters

    public String getLeaveRqID() {
        return leaveRqID;
    }

    public void setLeaveRqID(String leaveRqID) {
        this.leaveRqID = leaveRqID;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getApprover() {
        return approver;
    }

    public void setApprover(User approver) {
        this.approver = approver;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}

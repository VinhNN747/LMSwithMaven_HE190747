/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "LeaveRequest")
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LeaveRequestID")
    private Integer leaveRequestId;

    @Column(name = "SenderID")
    private Integer senderId;

    @Column(name = "ApproverID")
    private Integer approverId;

    @NotNull
    @Size(max = 50)
    @Column(name = "Status", nullable = false)
    private String status = "Pending";

    // Navigation property for the sender
    @ManyToOne
    @JoinColumn(name = "SenderID", referencedColumnName = "UserID", insertable = false, updatable = false)
    private User sender;

    // Navigation property for the approver
    @ManyToOne
    @JoinColumn(name = "ApproverID", referencedColumnName = "UserID", insertable = false, updatable = false)
    private User approver;

    // Getters and Setters
    public Integer getLeaveRequestId() {
        return leaveRequestId;
    }

    public void setLeaveRequestId(Integer leaveRequestId) {
        this.leaveRequestId = leaveRequestId;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getApproverId() {
        return approverId;
    }

    public void setApproverId(Integer approverId) {
        this.approverId = approverId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}

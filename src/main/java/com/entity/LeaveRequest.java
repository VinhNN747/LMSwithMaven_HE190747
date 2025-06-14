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
    @Size(max = 20)
    @Column(name = "LeaveRqID")
    private String leaveRqId;

    @Size(max = 10)
    @Column(name = "SenderID")
    private String senderId;

    @Size(max = 10)
    @Column(name = "ApproverID")
    private String approverId;

    @Column(name = "Reason", columnDefinition = "NVARCHAR(MAX)")
    private String reason;

    @NotNull
    @Size(max = 20)
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
    public String getLeaveRqId() {
        return leaveRqId;
    }

    public void setLeaveRqId(String leaveRqId) {
        this.leaveRqId = leaveRqId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getApproverId() {
        return approverId;
    }

    public void setApproverId(String approverId) {
        this.approverId = approverId;
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

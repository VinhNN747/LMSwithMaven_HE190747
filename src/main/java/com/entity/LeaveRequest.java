/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "LeaveRequest")
public class LeaveRequest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LeaveRequestID")
    private Integer leaveRequestId;

    // Navigation attribute for sender (User)
    @ManyToOne
    @JoinColumn(name = "SenderID", referencedColumnName = "UserID", insertable = false, updatable = false)
    private User sender;

    // Navigation attribute for reviewer (User)
    @ManyToOne
    @JoinColumn(name = "ReviewerID", referencedColumnName = "UserID", insertable = false, updatable = false)
    private User reviewer;

    @Column(name = "SenderID", nullable = false)
    private Integer senderId;

    @Column(name = "ReviewerID")
    private Integer reviewerId;

    @Column(name = "Status", length = 50)
    private String status;

    @Column(name = "Reason", columnDefinition = "nvarchar(max)")
    private String reason;

    @Column(name = "StartDate", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "EndDate", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Column(name = "Title", columnDefinition = "nvarchar(max)")
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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

    public Integer getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(Integer reviewerId) {
        this.reviewerId = reviewerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReviewer() {
        return reviewer;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }
}

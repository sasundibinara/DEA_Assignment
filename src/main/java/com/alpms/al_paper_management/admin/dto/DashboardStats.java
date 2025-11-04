package com.alpms.al_paper_management.admin.dto;


public class DashboardStats {

    private long totalPapers;
    private long activeSubjects;
    private long upcomingExams;
    private long totalUsers;

    public DashboardStats(long totalPapers, long activeSubjects, long upcomingExams, long totalUsers) {
        this.totalPapers = totalPapers;
        this.activeSubjects = activeSubjects;
        this.upcomingExams = upcomingExams;
        this.totalUsers = totalUsers;
    }

    public long getTotalPapers() {
        return totalPapers;
    }

    public long getActiveSubjects() {
        return activeSubjects;
    }

    public long getUpcomingExams() {
        return upcomingExams;
    }

    public long getTotalUsers() {
        return totalUsers;
    }
}

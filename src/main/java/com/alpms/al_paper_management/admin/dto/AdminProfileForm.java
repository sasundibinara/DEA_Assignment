package com.alpms.al_paper_management.admin.dto;

public class AdminProfileForm {

    private String fullName;
    private String phone;
    private String email; // or any extra field you want

    // --- getters & setters ---
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}



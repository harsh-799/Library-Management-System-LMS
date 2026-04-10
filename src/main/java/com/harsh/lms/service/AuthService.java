package com.harsh.lms.service;

import com.harsh.lms.model.Admin;
import com.harsh.lms.model.MemberCredentials;
import com.harsh.lms.repository.AdminRepository;
import com.harsh.lms.repository.MemberCredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class AuthService {

    private AdminRepository adminRepo;
    private MemberCredentialsRepository memberRepo;

    @Autowired
    public AuthService(AdminRepository adminRepo, MemberCredentialsRepository memberRepo) {
        this.adminRepo = adminRepo;
        this.memberRepo = memberRepo;
    }

    public boolean hasAdminRecords() {
        return (!adminRepo.findAll().isEmpty());
    }

    public void setAdminDetails(Admin admin){
        adminRepo.save(admin);

    }

    public Object authenticateUser(String username, String password){
        // Checking the Admin Credentials
        Optional<Admin> adminRecords = adminRepo.findById(username);

        if (adminRecords.isPresent() && adminRecords.get().getUsername().equals(username) && adminRecords.get().getPassword().equals(password)){
            return adminRecords.get();
        }

        // Checking the Member Credentials
        Optional<MemberCredentials> memberRecords = memberRepo.findById(username);

        if (memberRecords.isPresent() && memberRecords.get().getUsername().equals(username) && memberRecords.get().getPassword().equals(password)) {
            return memberRecords.get().getMember();
        }

        return null;
    }
}
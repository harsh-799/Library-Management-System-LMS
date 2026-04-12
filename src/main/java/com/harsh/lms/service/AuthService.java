package com.harsh.lms.service;

import com.harsh.lms.model.Admin;
import com.harsh.lms.model.MemberCredentials;
import com.harsh.lms.repository.AdminRepository;
import com.harsh.lms.repository.MemberCredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;
import com.harsh.lms.dto.LoginResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private AdminRepository adminRepo;
    private MemberCredentialsRepository memberRepo;

    @Autowired
    public AuthService(AdminRepository adminRepo, MemberCredentialsRepository memberRepo) {
        this.adminRepo = adminRepo;
        this.memberRepo = memberRepo;
    }

    public void setAdminDetails(Admin admin){
        adminRepo.save(admin);

    }

    public LoginResponse authenticateUser(String username, String password){
        // Checking the Admin Credentials

        Optional<Admin> adminRecords = adminRepo.findByUsername(username);

        if (adminRecords.isPresent() && adminRecords.get().getPassword().equals(password)){
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setSuccess(true);
            loginResponse.setUserId(adminRecords.get().getAdminId());
            loginResponse.setUsername(adminRecords.get().getUsername());
            loginResponse.setRole(adminRecords.get().getRole());
            return loginResponse;
        }

        // Checking the Member Credentials
        Optional<MemberCredentials> memberRecords = memberRepo.findById(username);

        if (memberRecords.isPresent() && memberRecords.get().getPassword().equals(password)) {
            LoginResponse loginResponse = new LoginResponse();
            System.out.println("Entering member");
            loginResponse.setSuccess(true);
            loginResponse.setUserId(memberRecords.get().getMember().getMemberId());
            loginResponse.setUsername(memberRecords.get().getUsername());
            loginResponse.setRole(memberRecords.get().getMember().getRole());
            return loginResponse;
        }

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setSuccess(false);
        loginResponse.setMessage("Invalid Credentials");
        return loginResponse;
    }
}
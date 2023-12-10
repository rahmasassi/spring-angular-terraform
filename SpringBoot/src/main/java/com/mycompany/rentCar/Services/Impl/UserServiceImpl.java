package com.mycompany.rentCar.Services.Impl;

import com.mycompany.rentCar.Entities.Agency;
import com.mycompany.rentCar.Entities.Role;
import com.mycompany.rentCar.Entities.AppUser;
import com.mycompany.rentCar.Repositories.AgencyRepository;
import com.mycompany.rentCar.Repositories.CarsRepository;
import com.mycompany.rentCar.Repositories.UserRepository;
import com.mycompany.rentCar.Services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AgencyRepository agencyRepository;


    @Override
    public AppUser saveUser(AppUser user) {
        return userRepository.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        return null;
    }

    @Override
    public Role findRoleByName(String name) {
        return null;
    }

    @Override
    public Long getUserIdByUsername(String username) {
        Optional<AppUser> userOptional = userRepository.findByUsername(username);
        return userOptional.map(AppUser::getId).orElse(null);
    }
    @Override
    public Collection<Role> getRolesByUserId(Long userId) {
        Optional<AppUser> userOptional = userRepository.findById(userId);

        return userOptional.map(AppUser::getRoles).orElse(Collections.emptyList());
    }

    @Override
    public Collection<Role> getRolesByAgencyId(Long agencyId) {
        Optional<Agency> userOptional = agencyRepository.findById(agencyId);
        return userOptional.map(Agency::getRoles).orElse(Collections.emptyList());
    }

    @Override
    public Long getAgencyIdByUsername(String username) {
        Optional<Agency> userOptional = agencyRepository.findByUsername(username);
        return userOptional.map(Agency::getId).orElse(null);
    }

    @Transactional(readOnly = true)
    public boolean checkUsernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional(readOnly = true)
    public boolean checkUsernameExistsAgency(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public void deleteAgency(Long agencyId) {
        Agency agency = agencyRepository.findById(agencyId).orElse(null);
        if (agency != null) {
            agency.getRoles().clear();
            agencyRepository.deleteById(agencyId);
        }
    }

    @Override
    public void deleteUser(Long userId) {
        AppUser user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.getRoles().clear();
            userRepository.deleteById(userId);
        }
    }

    @Override
    public List<Agency> getAllAgencies() {
        return agencyRepository.findAll();
    }

    @Override
    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Collection<String> getRoleNamesById(Long id) {
        if (id != null) {
            Collection<Role> userRoles = getRolesByUserId(id);
            Collection<Role> agencyRoles = getRolesByAgencyId(id);

            Collection<Role> allRoles = new ArrayList<>(userRoles);
            allRoles.addAll(agencyRoles);

            return allRoles.stream().map(Role::getName).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }
}



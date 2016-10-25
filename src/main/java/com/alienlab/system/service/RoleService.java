package com.alienlab.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alienlab.system.repositories.inter.RoleRepository;

@Service
public class RoleService {
	@Autowired
	RoleRepository repository;
	
	
}

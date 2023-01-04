package com.bookstore.admin.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bookstore.admin.entity.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

}

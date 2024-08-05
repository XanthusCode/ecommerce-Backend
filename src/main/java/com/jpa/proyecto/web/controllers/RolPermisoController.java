package com.jpa.proyecto.web.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jpa.proyecto.domain.services.rolPermiso.RolPermisoService;
import com.jpa.proyecto.persistence.entities.RolPermiso;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/rolpermisos")
public class RolPermisoController {


    @Autowired
    private RolPermisoService service;

    @GetMapping
    @PreAuthorize("hasAuthority('READ')")
    public List<RolPermiso> listRolePermission(){
        return this.service.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ')")
    public ResponseEntity<RolPermiso> view(@PathVariable Long id){
        Optional<RolPermiso> optionalRolePermission  = service.findById(id);
        if (optionalRolePermission.isPresent()){
            return ResponseEntity.ok(optionalRolePermission.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE')")
    public ResponseEntity<?> create(@Valid @RequestBody RolPermiso role_permission, BindingResult result){
        if (result.hasFieldErrors()) {
            return validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(role_permission));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE')")
    public ResponseEntity<RolPermiso> update(@PathVariable Long id, @Valid @RequestBody RolPermiso role_permission){
        Optional<RolPermiso> role_permissionOptional = this.service.update(id, role_permission);
        if (role_permissionOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(role_permissionOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE')")
    public ResponseEntity<RolPermiso> delete(@PathVariable Long id){
        //RolPermiso role_permission = new RolPermiso();
        //role_permission.setId(id);
        Optional<RolPermiso> optionalRolePermission = this.service.delete(id);
        if (optionalRolePermission.isPresent()){
            return ResponseEntity.ok(optionalRolePermission.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

}

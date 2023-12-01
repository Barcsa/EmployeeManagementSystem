package com.barcsa.ems.controller;

import com.barcsa.ems.dto.DepartmentDTO;
import com.barcsa.ems.entity.Department;
import com.barcsa.ems.service.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private DepartmentService departmentService;
    @PostMapping
    public ResponseEntity<DepartmentDTO> createDepartment (@RequestBody DepartmentDTO departmentDTO){
        DepartmentDTO savedDepartment = departmentService.createDepartment(departmentDTO);
        return new ResponseEntity<>(savedDepartment, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<DepartmentDTO> getDepartmentById(@PathVariable("id") Long departmentId){
        DepartmentDTO departmentDTO = departmentService.getDepartmentById(departmentId);
        return ResponseEntity.ok(departmentDTO);
    }
    @GetMapping
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments(){
        List<DepartmentDTO> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }

    @PutMapping("{id}")
    public ResponseEntity<DepartmentDTO> updateDepartment(@PathVariable ("id") Long departmentId, @RequestBody DepartmentDTO updatedDepartment){
        DepartmentDTO departmentDTO = departmentService.updateDepartment(departmentId, updatedDepartment);
        return ResponseEntity.ok(departmentDTO);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteDepartment(@PathVariable Long id){
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok("Department deleted successfully");
    }

}

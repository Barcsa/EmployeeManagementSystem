package com.barcsa.ems.controller;

import com.barcsa.ems.dto.EmployeeDTO;
import com.barcsa.ems.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/employees")
@CrossOrigin(origins = "http://localhost:3000")
public class EmployeeController {

    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee (@RequestBody EmployeeDTO employeeDTO){
        EmployeeDTO savedEmployee = employeeService.createEmployee(employeeDTO);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById (@PathVariable("id") Long employeeId){
        EmployeeDTO employeeDTO = employeeService.getEmployeeById(employeeId);
        return ResponseEntity.ok(employeeDTO);
    }
    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees(){
        List<EmployeeDTO> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable("id") Long employeeId, @RequestBody EmployeeDTO updatedEmployee){
        EmployeeDTO employeeDTO = employeeService.updateEmployee(employeeId, updatedEmployee);
        return ResponseEntity.ok(employeeDTO);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") Long employeeId){
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.ok("Employee deleted successfully");
    }
}

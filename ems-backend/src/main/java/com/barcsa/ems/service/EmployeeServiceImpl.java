package com.barcsa.ems.service;

import com.barcsa.ems.dto.EmployeeDTO;
import com.barcsa.ems.entity.Department;
import com.barcsa.ems.entity.Employee;
import com.barcsa.ems.exception.ResourceNotFoundException;
import com.barcsa.ems.mapper.EmployeeMapper;
import com.barcsa.ems.repository.DepartmentRepository;
import com.barcsa.ems.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService{

    private EmployeeRepository employeeRepository;
    private DepartmentRepository departmentRepository;

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = EmployeeMapper.mapToEmployee(employeeDTO);
        Department department = departmentRepository.findById(employeeDTO.getDepartmentId()).orElseThrow(
                () -> new ResourceNotFoundException("Department does not exist widh id" + employeeDTO.getDepartmentId()));
        employee.setDepartment(department);
        Employee savedEmployee = employeeRepository.save(employee);
        return EmployeeMapper.mapToEmployeeDTO(savedEmployee);
    }

    @Override
    public EmployeeDTO getEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new ResourceNotFoundException("Employee does not exist with given id" + employeeId));

        return EmployeeMapper.mapToEmployeeDTO(employee);

    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream().map((employee -> EmployeeMapper.mapToEmployeeDTO(employee))).collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO updateEmployee(Long employeeId, EmployeeDTO updatedEmployee) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new ResourceNotFoundException("Employee does not exist with id" + employeeId));

        employee.setFirstName(updatedEmployee.getFirstName());
        employee.setLastName(updatedEmployee.getLastName());
        employee.setEmail(updatedEmployee.getEmail());
        Department department = departmentRepository.findById(updatedEmployee.getDepartmentId()).orElseThrow(
                () -> new ResourceNotFoundException("Department does not exist widh id" + updatedEmployee.getDepartmentId()));
        employee.setDepartment(department);
        Employee updatedEmployeeObj = employeeRepository.save(employee);
        return EmployeeMapper.mapToEmployeeDTO(updatedEmployeeObj);
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new ResourceNotFoundException("Employee does not wxist with id" + employeeId));
        employeeRepository.deleteById(employeeId);
    }


}

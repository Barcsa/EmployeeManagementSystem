package com.barcsa.ems.service;

import com.barcsa.ems.dto.DepartmentDTO;
import com.barcsa.ems.entity.Department;
import com.barcsa.ems.exception.ResourceNotFoundException;
import com.barcsa.ems.mapper.DepartmentMapper;
import com.barcsa.ems.repository.DepartmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService{

    private DepartmentRepository departmentRepository;
    @Override
    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {
        Department department = DepartmentMapper.mapToDepartment(departmentDTO);
        Department savedDepartment = departmentRepository.save(department);
        return DepartmentMapper.mapToDepartmentDTO(savedDepartment);
    }

    @Override
    public DepartmentDTO getDepartmentById(Long departmentId) {
        Department department = departmentRepository.findById(departmentId).orElseThrow(
                () -> new ResourceNotFoundException("Department does not exist with id " + departmentId));

        return DepartmentMapper.mapToDepartmentDTO(department);
    }

    @Override
    public List<DepartmentDTO> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return departments.stream().map((department -> DepartmentMapper.mapToDepartmentDTO(department))).collect(Collectors.toList());
    }

    @Override
    public DepartmentDTO updateDepartment(Long departmentId, DepartmentDTO updatedDepartment) {
        Department department = departmentRepository.findById(departmentId).orElseThrow(
                () -> new ResourceNotFoundException("Department does not exist with id " + departmentId));

        department.setDepartmentName(updatedDepartment.getDepartmentName());
        department.setDepartmentDescription(updatedDepartment.getDepartmentDescription());

        Department updatedDepartmentObj = departmentRepository.save(department);
        return DepartmentMapper.mapToDepartmentDTO(updatedDepartmentObj);

    }

    @Override
    public void deleteDepartment(Long departmentId) {
        Department department = departmentRepository.findById(departmentId).orElseThrow(
                () -> new ResourceNotFoundException("Department does not exist with id" + departmentId));
        departmentRepository.deleteById(departmentId);
    }
}

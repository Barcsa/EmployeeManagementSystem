package com.barcsa.ems.mapper;

import com.barcsa.ems.dto.DepartmentDTO;
import com.barcsa.ems.entity.Department;

public class DepartmentMapper {

    public static DepartmentDTO mapToDepartmentDTO(Department department){
        return new DepartmentDTO(department.getId(),
                    department.getDepartmentName(),
                    department.getDepartmentDescription());
    }

    public static Department mapToDepartment(DepartmentDTO departmentDTO){
        return new Department(departmentDTO.getId(),
                departmentDTO.getDepartmentName(),
                departmentDTO.getDepartmentDescription());
    }
}

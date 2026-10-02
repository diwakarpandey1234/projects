package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.DepartmentRequestDTO;
import com.example.employeemanagement.dto.DepartmentResponseDTO;
import com.example.employeemanagement.entity.Departments;
import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.repository.DepartmentRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentsService {
    private final DepartmentRepository departmentRepository;


    DepartmentsService(DepartmentRepository departmentRepository){
        this.departmentRepository=departmentRepository;
    }

    public String addDepartment( DepartmentRequestDTO departments) {
        Departments departments1=new Departments();
        departments1.setLocation(departments.getLocation());
        departments1.setName(departments.getName());
        departmentRepository.save(departments1);
        return "Department Saved Successfully";
    }



    public String updateDepartments(Long id,DepartmentRequestDTO departments) {

        Departments existing=departmentRepository.
                findById(id).
                orElseThrow(()-> new ResourceNotFoundException("Department Not Found"));

        existing.setName(departments.getName());
        existing.setLocation(departments.getLocation());
        departmentRepository.save(existing);
        return "Department is Updated";
    }



    public String deleteDepartment(Long id) {

        Departments department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found"));

        departmentRepository.delete(department);
        return "Department is Deleted";
    }



    public List<DepartmentResponseDTO> getAllDepartments() {
        List<Departments> departments=departmentRepository.findAll();
        return departments.stream().map(departments1 -> {
            DepartmentResponseDTO departmentResponseDTO=new DepartmentResponseDTO();
            departmentResponseDTO.setName(departments1.getName());
            departmentResponseDTO.setId(departments1.getId());
            departmentResponseDTO.setLocation(departments1.getLocation());
            return departmentResponseDTO;
                }

        ).toList();
    }
}

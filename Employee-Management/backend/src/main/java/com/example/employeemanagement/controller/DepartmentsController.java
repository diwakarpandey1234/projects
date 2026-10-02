package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.DepartmentRequestDTO;
import com.example.employeemanagement.dto.DepartmentResponseDTO;
import com.example.employeemanagement.entity.Departments;
import com.example.employeemanagement.service.DepartmentsService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentsController {

    private final DepartmentsService departmentsService;

    public DepartmentsController(DepartmentsService departmentsService){
        this.departmentsService=departmentsService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('DEPARTMENT_WRITE')")
    public String addDepartment(@Valid @RequestBody DepartmentRequestDTO departments){
        return departmentsService.addDepartment(departments);
    }



    @PutMapping({"update/{id}", "update/"})
    @PreAuthorize("hasAuthority('DEPARTMENT_WRITE')")
    public String updateDepartment(@PathVariable Long id,@RequestBody Long ID, @Valid @RequestBody DepartmentRequestDTO departments){
        Long target=(id==null)?ID:id;
        return departmentsService.updateDepartments(target,departments);
    }



    @DeleteMapping({"/delete/{id}", "/delete/"})
    @PreAuthorize("hasAuthority('DEPARTMENT_DELETE')")
    public String deleteDepartment(@PathVariable Long id,
                                   @RequestBody Long ID){
        Long target=(id==null)?ID:id;
        return departmentsService.deleteDepartment(target);
    }



    @GetMapping("/get/all")
    @PreAuthorize("hasAuthority('DEPARTMENT_READ')")
    public List<DepartmentResponseDTO> getAllDepartments(){
        return departmentsService.getAllDepartments();
    }
}

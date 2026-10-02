package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.SalaryRequestDTO;
import com.example.employeemanagement.dto.SalaryResponseDTO;
import com.example.employeemanagement.entity.Salary;
import com.example.employeemanagement.repository.SalaryRepository;
import com.example.employeemanagement.service.SalaryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/salary")
public class SalaryController {
    private final SalaryService salaryService;

    SalaryController(SalaryService salaryService){
        this.salaryService=salaryService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('SALARY_WRITE')")
    public String addSalary(@Valid @RequestBody SalaryRequestDTO salary){
        return salaryService.addSalary(salary);
    }


    @PutMapping({"update/{id}", "update/"})
    @PreAuthorize("hasAuthority('SALARY_WRITE')")
    public Salary updateSalary(@PathVariable Long id,
                               @RequestBody Long ID,
                               @RequestBody Salary salary) {

        Long target=(id==null)? ID:id;
        return salaryService.updateSalary(target, salary);
    }


    @DeleteMapping({"delete/{id}", "delete/id/"})
    @PreAuthorize("hasAuthority('SALARY_DELETE')")
    public String deleteSalary(@PathVariable Long id,
                               @RequestBody Long ID) {

        Long target=(id==null)?ID:id;

        salaryService.deleteSalary(target);
        return "Salary deleted successfully";
    }

    @GetMapping({"get/{id}", "get/"})
    @PreAuthorize("hasAuthority('SALARY_READ')")
    public SalaryResponseDTO getSalary(@PathVariable Long id,
                                       @RequestBody Long ID) {

        Long target=(id==null)?ID:id;
        return salaryService.getSalary(target);


    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAuthority('SALARY_READ')")
    public List<SalaryResponseDTO> getEmployeeSalaries(
            @PathVariable Long employeeId) {

        return salaryService.getEmployeeSalaries(employeeId);
    }

    @GetMapping("/get/All")
    @PreAuthorize("hasAuthority('SALARY_READ')")
    public List<SalaryResponseDTO>getSalaryAll(){
        return salaryService.getSalaryAll();
    }

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('SALARY_READ')")
    public List<SalaryResponseDTO> getMySalaries() {

        return salaryService.getMySalaries();
    }

}

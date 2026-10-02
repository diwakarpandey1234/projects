package com.example.employeemanagement.service;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.employeemanagement.dto.SalaryRequestDTO;
import com.example.employeemanagement.dto.SalaryResponseDTO;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.entity.Salary;
import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.repository.SalaryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SalaryService {

    private final EmployeeRepository employeeRepository;
    private final SalaryRepository salaryRepository;
    private final AuditService auditService;



    public SalaryService(EmployeeRepository employeeRepository,
                         SalaryRepository salaryRepository,
                         AuditService auditService){
        this.employeeRepository = employeeRepository;
        this.salaryRepository=salaryRepository;
        this.auditService=auditService;
    }



    public String  addSalary( SalaryRequestDTO salary){


        Employee employee = employeeRepository.findById(
                salary.getEmployeeResponseDTO().getEmployeeID()
        ).orElseThrow(() ->
                new ResourceNotFoundException("Employee not found")
        );
        Salary salary1=new Salary();
        salary1.setBasic(salary.getBasic());
        //salary1.setDeduction(salary.getDeduction());
        salary1.setBonus(salary.getBonus());
        //salary1.setEffectiveDate(salary.getEffectiveDate());

        salary1.setNetSalary(
                salary.getBasic()+
                        salary.getBonus()-
                        salary.getDeduction()
        );
        salary1.setEmployee(employee);
        Salary savedSalary = salaryRepository.save(salary1);

        auditService.createAuditLog(
                getCurrentUsername(),
                "SALARY_CREATED",
                "Salary",
                savedSalary.getId(),
                "Salary created for employee: "
                        + employee.getEmployeeID()
        );
        return "Salary added";
    }




    public Salary updateSalary(Long id, Salary salary) {

        Salary existing = salaryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Salary not found"));

        existing.setBasic(salary.getBasic());
        existing.setBonus(salary.getBonus());
       // existing.setDeduction(salary.getDeduction());
       // existing.setEffectiveDate(salary.getEffectiveDate());

        existing.setNetSalary(
                existing.getBasic()
                        + existing.getBonus()
                        );

        // - existing.getDeduction()

        Salary updatedSalary = salaryRepository.save(existing);

        auditService.createAuditLog(
                getCurrentUsername(),
                "SALARY_UPDATED",
                "Salary",
                updatedSalary.getId(),
                "Salary updated for employee: "
                        + updatedSalary.getEmployee().getEmployeeID()
        );

        return updatedSalary;
    }



    public void deleteSalary(Long id) {

        Salary salary = salaryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Salary not found"));

        Long employeeId = salary.getEmployee().getEmployeeID();

        salaryRepository.deleteById(id);

        auditService.createAuditLog(
                getCurrentUsername(),
                "SALARY_DELETED",
                "Salary",
                id,
                "Salary deleted for employee: "
                        + employeeId
        );
    }



    public SalaryResponseDTO getSalary(Long id) {

        Salary salary = salaryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Salary not found with id: " + id
                        ));

        SalaryResponseDTO salary1 = new SalaryResponseDTO();

        salary1.setNetSalary(salary.getNetSalary());
        salary1.setId(salary.getId());

        return salary1;
    }



    public List<SalaryResponseDTO> getSalaryAll() {
        List<Salary> salaries=salaryRepository.findAll();


        return  salaries.stream().map(salary -> {
            SalaryResponseDTO   salary1=new SalaryResponseDTO();
            salary1.setNetSalary(salary.getNetSalary());
            salary1.setId(salary.getId());

            return salary1;
        }).toList();
    }



    private String getCurrentUsername() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            return "SYSTEM";
        }

        return authentication.getName();
    }




    public List<SalaryResponseDTO> getEmployeeSalaries(Long employeeId) {

        // Make sure employee exists
        employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found")
                );

        List<Salary> salaries =
                salaryRepository.findByEmployeeId(employeeId);

        return salaries.stream().map(salary -> {

            SalaryResponseDTO response = new SalaryResponseDTO();

            response.setId(salary.getId());
            response.setNetSalary(salary.getNetSalary());

            return response;

        }).toList();
    }




    public List<SalaryResponseDTO> getMySalaries() {

//        Authentication authentication =
//                SecurityContextHolder
//                        .getContext()
//                        .getAuthentication();

        String username = getCurrentUsername();

        Employee employee = employeeRepository
                .findByUserUserName(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee not found for current user"
                        )
                );

        List<Salary> salaries =
                salaryRepository.findByEmployeeId(employee.getEmployeeID());

        return salaries.stream().map(salary -> {

            SalaryResponseDTO response = new SalaryResponseDTO();

            response.setId(salary.getId());
            response.setNetSalary(salary.getNetSalary());

            return response;

        }).toList();
    }
}

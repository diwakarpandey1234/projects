package com.example.employeemanagement.service;

import com.example.employeemanagement.entity.User;
import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.repository.userDetailsRepository;
import com.example.employeemanagement.service.AuditService;

import com.example.employeemanagement.dto.EmployeeDTO;
import com.example.employeemanagement.dto.EmployeeResponseDTO;
import com.example.employeemanagement.dto.SalaryResponseDTO;
import com.example.employeemanagement.entity.Departments;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.repository.DepartmentRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import lombok.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {


    private final AuditService auditService;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final userDetailsRepository userRepository;
    //private final EmployeeDTO employeeDTO;

    public EmployeeService(EmployeeRepository employeeRepository,
                           DepartmentRepository departmentRepository,
                           AuditService auditService,
                           userDetailsRepository userRepository
    ) {
        this.employeeRepository = employeeRepository;
        this.auditService=auditService;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;

    }



    //@CacheEvict(value = "employees", key = "'all'")
    public String createEmployee(@NonNull EmployeeDTO data) {

        Employee employee = new Employee();

        employee.setFirstName(data.getFirstName());
        employee.setLastName(data.getLastName());
        employee.setEmail(data.getEmail());
        employee.setStatus(data.getStatus());
        employee.setPhone(data.getPhone());
        employee.setDesignation(data.getDesignation());
        employee.setJoiningDate(data.getJoiningDate());

        Departments department =
                departmentRepository.findById(data.getDepartmentId())
                        .orElseThrow(() ->
                                new RuntimeException("Department not found"));

        employee.setDepartment(department);

        User user = userRepository.findById(data.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        employee.setUser(user);

        Employee savedEmployee = employeeRepository.save(employee);

        auditService.createAuditLog(
                getCurrentUsername(),
                "EMPLOYEE_CREATED",
                "Employee",
                savedEmployee.getEmployeeID(),
                "Employee created: " + savedEmployee.getFirstName()
        );



        return "Employee Added Successfully";
    }



    //@Cacheable(value = "employees", key = "'all'")
    public List<EmployeeResponseDTO> getAllEmployees() {

        List<Employee> employees = employeeRepository.findAll();

        return employees.stream()
                .map(employee -> {

                    EmployeeResponseDTO response =
                            new EmployeeResponseDTO();

                    response.setEmployeeID(employee.getEmployeeID());
                    response.setFirstName(employee.getFirstName());
                    response.setLastName(employee.getLastName());
                    response.setEmail(employee.getEmail());
                    response.setDesignation(employee.getDesignation());

                    response.setDepartmentName(
                            employee.getDepartment().getName()
                    );

                    if (employee.getSalaries() != null) {

                        List<SalaryResponseDTO> salaries =
                                employee.getSalaries()
                                        .stream()
                                        .map(salary -> {

                                            SalaryResponseDTO salaryDTO =
                                                    new SalaryResponseDTO();

                                            salaryDTO.setId(salary.getId());
                                            salaryDTO.setNetSalary(
                                                    salary.getNetSalary()
                                            );

                                            return salaryDTO;
                                        })
                                        .toList();

                        response.setSalaries(salaries);
                    }
                    return response;
                })
                .toList();
    }



    //@Cacheable(value = "employeeByEmail", key = "#email")
    public EmployeeResponseDTO findByEmail(String email) {


        Employee e = employeeRepository.getByEmail(email);

        // if(e==null){
        //    throw  UserNotFoundException("No User found for given email");
//        }

        EmployeeResponseDTO response =
                new EmployeeResponseDTO();

        response.setEmployeeID(e.getEmployeeID());
        response.setFirstName(e.getFirstName());
        response.setLastName(e.getLastName());
        response.setEmail(e.getEmail());
        response.setDesignation(e.getDesignation());

        response.setDepartmentName(
                e.getDepartment().getName()
        );

        List<SalaryResponseDTO> salaries;
        if (e.getSalaries() != null) {
            salaries =
                    e.getSalaries()
                            .stream()
                            .map(salary -> {
                                SalaryResponseDTO salaryDTO =
                                        new SalaryResponseDTO();
                                salaryDTO.setId(salary.getId());
                                salaryDTO.setNetSalary(
                                        salary.getNetSalary()
                                );
                                return salaryDTO;
                            })
                            .toList();
            response.setSalaries(salaries);
        }
        return response;
    }



   // @Cacheable(value = "employeeByFirstName", key = "#firstName")
    public List<EmployeeResponseDTO> findByFirstName(String firstName) {
        List<Employee> emp = employeeRepository.getByFirstName(firstName);
        return emp.stream()
                .map(employee -> {

                    EmployeeResponseDTO response =
                            new EmployeeResponseDTO();

                    response.setEmployeeID(employee.getEmployeeID());
                    response.setFirstName(employee.getFirstName());
                    response.setLastName(employee.getLastName());
                    response.setEmail(employee.getEmail());
                    response.setDesignation(employee.getDesignation());

                    response.setDepartmentName(
                            employee.getDepartment().getName()
                    );

                    if (employee.getSalaries() != null) {

                        List<SalaryResponseDTO> salaries =
                                employee.getSalaries()
                                        .stream()
                                        .map(salary -> {

                                            SalaryResponseDTO salaryDTO =
                                                    new SalaryResponseDTO();

                                            salaryDTO.setId(salary.getId());
                                            salaryDTO.setNetSalary(
                                                    salary.getNetSalary()
                                            );

                                            return salaryDTO;
                                        })
                                        .toList();

                        response.setSalaries(salaries);
                    }

                    return response;

                })
                .toList();
    }



   // @Cacheable(value = "employeeByLastName", key = "#lastName")
    public List<EmployeeResponseDTO> findByLastName(String lastName) {
        List<Employee> emp = employeeRepository.getByLastName(lastName);
        return emp.stream()
                .map(employee -> {
                    EmployeeResponseDTO response =
                            new EmployeeResponseDTO();

                    response.setEmployeeID(employee.getEmployeeID());
                    response.setFirstName(employee.getFirstName());
                    response.setLastName(employee.getLastName());
                    response.setEmail(employee.getEmail());
                    response.setDesignation(employee.getDesignation());

                    response.setDepartmentName(
                            employee.getDepartment().getName()
                    );
                    if (employee.getSalaries() != null) {

                        List<SalaryResponseDTO> salaries =
                                employee.getSalaries()
                                        .stream()
                                        .map(salary -> {

                                            SalaryResponseDTO salaryDTO =
                                                    new SalaryResponseDTO();

                                            salaryDTO.setId(salary.getId());
                                            salaryDTO.setNetSalary(
                                                    salary.getNetSalary()
                                            );
                                            return salaryDTO;
                                        })
                                        .toList();
                        response.setSalaries(salaries);
                    }
                    return response;
                })
                .toList();
    }



    public String deleteEmployee(Long id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);

            auditService.createAuditLog(
                    getCurrentUsername(),
                    "EMPLOYEE_DELETED",
                    "Employee",
                    id,
                    "Employee deleted with id: " + id
            );

            return "Employee Deleted With Id ->" + id;
        }
        return "Provide valid Id";
    }



//   PAGINATION

    public Page<EmployeeResponseDTO> getEmployees(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> emp = employeeRepository.findAll(pageable);

        return emp
                .map(employee -> {

                    EmployeeResponseDTO response =
                            new EmployeeResponseDTO();

                    response.setEmployeeID(employee.getEmployeeID());
                    response.setFirstName(employee.getFirstName());
                    response.setLastName(employee.getLastName());
                    response.setEmail(employee.getEmail());
                    response.setDesignation(employee.getDesignation());

                    response.setDepartmentName(
                            employee.getDepartment().getName()
                    );
                    if (employee.getSalaries() != null) {
                        List<SalaryResponseDTO> salaries =
                                employee.getSalaries()
                                        .stream()
                                        .map(salary -> {

                                            SalaryResponseDTO salaryDTO =
                                                    new SalaryResponseDTO();

                                            salaryDTO.setId(salary.getId());
                                            salaryDTO.setNetSalary(
                                                    salary.getNetSalary()
                                            );
                                            return salaryDTO;
                                        })
                                        .toList();
                        response.setSalaries(salaries);
                    }
                    return response;
                });
    }


// SORTING
// SORTING + PAGINATION

    public List<EmployeeResponseDTO> sortByNameAsc(int page, int size) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("firstName").ascending()
        );

        List<Employee> emp = employeeRepository.findAll(pageable).getContent();
        return emp.stream()
                .map(employee -> {

                    EmployeeResponseDTO response =
                            new EmployeeResponseDTO();

                    response.setEmployeeID(employee.getEmployeeID());
                    response.setFirstName(employee.getFirstName());
                    response.setLastName(employee.getLastName());
                    response.setEmail(employee.getEmail());
                    response.setDesignation(employee.getDesignation());

                    response.setDepartmentName(
                            employee.getDepartment().getName()
                    );
                    if (employee.getSalaries() != null) {

                        List<SalaryResponseDTO> salaries =
                                employee.getSalaries()
                                        .stream()
                                        .map(salary -> {

                                            SalaryResponseDTO salaryDTO =
                                                    new SalaryResponseDTO();

                                            salaryDTO.setId(salary.getId());
                                            salaryDTO.setNetSalary(
                                                    salary.getNetSalary()
                                            );
                                            return salaryDTO;
                                        })
                                        .toList();
                        response.setSalaries(salaries);
                    }
                    return response;
                })
                .toList();
    }


//    public List<Employee> sortByNameAce() {
//
//        return employeeRepository.findAll(
//                Sort.by("firstName").ascending());
//    }

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

}



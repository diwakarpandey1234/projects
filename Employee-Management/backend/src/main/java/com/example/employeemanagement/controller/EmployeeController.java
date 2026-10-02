package com.example.employeemanagement.controller;


import com.example.employeemanagement.dto.EmployeeDTO;
import com.example.employeemanagement.dto.EmployeeResponseDTO;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Employee")
public class EmployeeController {

    private final EmployeeService employeeService;
    public EmployeeController(EmployeeService employeeService){
        this.employeeService=employeeService;
    }


    @PostMapping("/create")
    @PreAuthorize("hasAuthority('EMPLOYEE_WRITE')")
    public String createEmployee(@Valid  @RequestBody EmployeeDTO data) {
        return employeeService.createEmployee(data);
    }


    @DeleteMapping({"/delete/id", "/delete/{id}"})
    @PreAuthorize("hasAuthority('EMPLOYEE_DELETE')")
    public String deleteEmployee(@PathVariable Long id,
                                 @RequestBody Long ID) {
        Long target=(id==null)?ID:id;
        if(target==null) throw new RuntimeException("Please Enter valid id to delete Employee");

        return employeeService.deleteEmployee(target);
    }



    @GetMapping("/get/Allemployees")
    @PreAuthorize("hasAuthority('EMPLOYEE_READ')")
    public List<EmployeeResponseDTO> ViewAllEmployees(){
           return employeeService.getAllEmployees();
    }



    @GetMapping({"/get/ByEmail", "/get/ByEmail/{email}"})
    @PreAuthorize("hasAuthority('EMPLOYEE_READ')")
    public EmployeeResponseDTO searchByEmail(@PathVariable(required = false) String email,
                                             @RequestParam(required = false) String emailParam) {
        String target = (email != null) ? email : emailParam;
        return employeeService.findByEmail(target);
    }



    @GetMapping({"/get/ByFirstName", "/get/{ByFirstName}"})
    @PreAuthorize("hasAuthority('EMPLOYEE_READ')")
    public List<EmployeeResponseDTO> findByFirstName(@RequestBody String firstName,
                                                     @PathVariable String F_name){
        String target=(firstName==null)?F_name:firstName;
        return employeeService.findByFirstName(target);
    }




    @GetMapping({"/get/ByLastName", "/get/{ByLastName}"})
    @PreAuthorize("hasAuthority('EMPLOYEE_READ')")
    public List<EmployeeResponseDTO> findByLastName(@RequestBody String lastName,
                                                    @PathVariable String L_name){

        String target=(lastName==null)?L_name:lastName;
        return employeeService.findByLastName(target);
    }




    //Pagination

    @GetMapping("/pages")
    @PreAuthorize("hasAuthority('EMPLOYEE_READ')")
    public Page<EmployeeResponseDTO> getEmployees(
            @RequestParam int page,
            @RequestParam int size) {

        return employeeService.getEmployees(page, size);
    }

       //     Sorting
}

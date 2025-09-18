package com.luv2code.springboot.cruddemo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.luv2code.springboot.cruddemo.entity.Employee;
import com.luv2code.springboot.cruddemo.service.EmployeeService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/employees") // base mapping for all methods in this class
public class EmployeeController {

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService theEmployeeService) { // Since we only have one constructor, Spring will automatically wire the dependency. No need for @Autowired
        employeeService = theEmployeeService;
    }

    // add mapping for "/list"
    @GetMapping("/list")
    public String listEmployees(Model theModel) {

        // get employees from db
        List<Employee> theEmployees = employeeService.findAll();

        // add to the spring model
        theModel.addAttribute("employees", theEmployees);

        // return the view
        return "list-employees";
    }
    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model theModel) {
        // create model attribute to bind form data
        Employee theEmployee = new Employee();

        theModel.addAttribute("employee", theEmployee);

        return "add-employee";
    }
    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("id") int theId, Model theModel) {
        // get the employee from the service
        Employee theEmployee = employeeService.findById(theId);

        // set employee as a model attribute to pre-populate the form
        theModel.addAttribute("employee", theEmployee);

        // send over to our form
        return "update-employee";            
    }
    @GetMapping("/delete")
    public String delete(@RequestParam("id") int theId) {
        // delete the employee
        employeeService.deleteById(theId);

        return "redirect:/employees/list";
    }

    @PostMapping("/validateForm")
    public String saveEmployee(@Valid @ModelAttribute("employee") Employee theEmployee, BindingResult theBindingResult) {
        System.out.println("Binding result: " + theBindingResult);
        if (theBindingResult.hasErrors()) {
            return "add-employee";
        }
        else {
            employeeService.save(theEmployee);
            return "redirect:/employees/list"; // redirect to prevent duplicate submissions
        }
    }
}

package com.prady.sample.mapstruct;

import com.prady.sample.mapstruct.domain.Employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class StreamGroupBy {

    public static void main(String[] args) {
        List<Employee> employeesList = new ArrayList<>();
        employeesList.add(new Employee("John", "Doe", "john.doe@sample.com", "111111111", new ArrayList<>()));
        employeesList.add(new Employee("John", "Doe1", "john.doe@sample.com", "111111111", new ArrayList<>()));
        employeesList.add(new Employee("Jane", "Doe", "jane.doe@sample.com", "222222222", new ArrayList<>()));

        Map<String, Map<String, Map<String, List<Employee>>>> collect = employeesList.stream().collect(Collectors.groupingBy(Employee::getFirstName, Collectors.groupingBy(Employee::getLastName, Collectors.groupingBy(Employee::getPhone))));

        System.out.println(collect);


        employeesList.add(new Employee("Jane", "Doe", "jane.doe3@sample.com", "222222222", new ArrayList<>()));
        record FirstLastPhone(String firstName, String lastName, String phoneNo) {}
        Map<FirstLastPhone, List<Employee>> collect1 = employeesList.stream().collect(Collectors.groupingBy(e -> new FirstLastPhone(e.getFirstName(), e.getLastName(), e.getPhone()), Collectors.mapping((Employee e) -> e, toList())));
        System.out.println(collect1);
    }
}

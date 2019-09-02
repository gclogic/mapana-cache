package org.galileoframework.cache;

import java.util.LinkedList;
import java.util.List;

public class GalileoCacheApplicationTests {

    public void contextLoads() {
    }

    public static void main(String[] args) {
        List<Employee> list = new LinkedList<>();
        list.add(new Employee("guido"));
        list.add(new Employee("antonio"));
        list.add(new Employee("valentina"));
        list.add(new Employee("lorena"));
        list.add(new Employee("rosa"));
        list.add(new Employee("piedad"));
        list.add(new Employee("benicia"));
        list.add(new Employee("agar"));
        list.add(new Employee("juana"));
        list.add(new Employee("tarcy"));
        list.add(new Employee("laudina"));
        list.add(new Employee("yulieth"));
        list.add(new Employee("Jesus"));
        list.add(new Employee("Rafael"));
        list.add(new Employee("Yesmin"));
        list.add(new Employee("Patricia"));
        list.add(new Employee("Richard"));
        list.add(new Employee("Manuel"));
        list.add(new Employee("Francisco"));
        list.add(new Employee("Deiner"));
        list.add(new Employee("Kismy"));
        list.add(new Employee("Angie"));
        list.add(new Employee("Arelis"));
        long initTime = System.currentTimeMillis();
        EmployeeCache employeeCache = new EmployeeCache();

        for (Employee employee : list) {
            employeeCache.put(employee);
        }

        System.out.println("ExecTime: " + (System.currentTimeMillis() - initTime));
    }

}

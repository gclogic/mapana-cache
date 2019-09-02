/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.galileoframework.cache;

import org.galileoframework.cache.implement.GalileoCache;

/**
 *
 * @author Guido A. Cafiel Vellojin
 */
public class EmployeeCache extends GalileoCache<Employee> {

    @Override
    public String getQueueName() {
        return "employeeQueue";
    }

    @Override
    public void batchProcess(Employee element) {
        System.out.println("Emplo Name: " + element.getName());
    }

}

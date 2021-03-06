package part1.exercise;

import data.Employee;
import data.JobHistoryEntry;
import data.Person;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static data.Generator.generateEmployeeList;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

public class StreamsExercise1 {
    // https://youtu.be/kxgo7Y4cdA8 Сергей Куксенко и Алексей Шипилёв — Через тернии к лямбдам, часть 1
    // https://youtu.be/JRBWBJ6S4aU Сергей Куксенко и Алексей Шипилёв — Через тернии к лямбдам, часть 2

    // https://youtu.be/O8oN4KSZEXE Сергей Куксенко — Stream API, часть 1
    // https://youtu.be/i0Jr2l3jrDA Сергей Куксенко — Stream API, часть 2

    @Test
    public void getAllEpamEmployees() {
        List<Employee> employees = generateEmployeeList();

        List<Person> expected = new ArrayList<>();

        for (Employee e : employees) {
            boolean epamer = false;
            for (JobHistoryEntry j : e.getJobHistory()) {
                if (j.getEmployer().equals("epam")) {
                    epamer = true;
                }
            }
            if (epamer){
                expected.add(e.getPerson());
            }
        }

        List<Person> epamEmployees = employees
                .stream()
                .filter(StreamsExercise1::hasEpamExp)
                .map(Employee::getPerson)
                .collect(Collectors.toList());
        System.out.println(epamEmployees);// TODO all persons with experience in epam

        Assert.assertEquals(expected,epamEmployees);
//        throw new UnsupportedOperationException();
    }

    private static boolean hasEpamExp (Employee employee) {
        return employee
                .getJobHistory()
                .stream()
                .anyMatch(entry -> entry.getEmployer().equals("epam"));
    }

    @Test
    public void getEmployeesStartedFromEpam() {

        List<Employee> employees = generateEmployeeList();

        List<Person> expected = new ArrayList<>();
        for (Employee e : employees) {
            JobHistoryEntry j = e.getJobHistory().get(0);
            if (j.getEmployer().equals("epam")){
                expected.add(e.getPerson());
            }
        }

        List<Person> epamEmployees = employees
                .stream()
                .filter(employee -> employee.getJobHistory().stream()
                        .findFirst()
                        .filter(entry -> entry.getEmployer().equals("epam"))
                        .isPresent())
                .map(Employee::getPerson)
                .collect(Collectors.toList());// TODO all persons with first experience in epam

//        throw new UnsupportedOperationException();
        Assert.assertEquals(expected,epamEmployees);
    }

    @Test
    public void sumEpamDurations() {
        final List<Employee> employees = generateEmployeeList();

        int expected = 0;

        for (Employee e : employees) {
            for (JobHistoryEntry j : e.getJobHistory()) {
                if (j.getEmployer().equals("epam")) {
                    expected += j.getDuration();
                }
            }
        }

        // TODO
//        throw new UnsupportedOperationException();

        // int result = ???
        int result = employees.stream()
                .flatMap(employee -> employee.getJobHistory().stream())
                .filter(jobHistoryEntry -> jobHistoryEntry.getEmployer().equals("epam"))
                .mapToInt(JobHistoryEntry::getDuration)
                .sum();

         assertEquals(expected, result);
    }

}

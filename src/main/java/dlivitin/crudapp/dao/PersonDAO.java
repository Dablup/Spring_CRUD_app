package dlivitin.crudapp.dao;

import dlivitin.crudapp.models.Person;
import org.postgresql.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;


    // Inject bean JdbcTemplate to the DAO to use it while working with database
    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static int PEOPLE_COUNT;

    // !!!!
    // NOW we do not need to do all the stuff which is commented below because of having jdbcTemplate
    // !!!!

    // It is bad practice defining these constants here, but it is shown just for educational purposes
    //    private static final String URL = "jdbc:postgresql://localhost:5432/db_for_CRUD";
    //    private static final String USERNAME = "postgres";
    //    private static final String PASSWORD = "root";
    //
    //    private static Connection connection;
    //
    //
    //    // The problem with JBDC is that we only can receive SQLException during some error
    //    // It means that we can not understand in which part of the database work we have problem
    //    static {
    //        try{
    //            Class.forName("org.postgresql.Driver");
    //        } catch (ClassNotFoundException e){
    //            e.printStackTrace();
    //        }
    //
    //        try {
    //            connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
    //        } catch (SQLException e) {
    //            e.printStackTrace();
    //        }
    //    }


    // BeanPropertyRowMapper is used to create objects of the person with the data returned from the database
    // We can use it because we have the same fields in the class Person as in the database
    public List<Person> index() {
        return jdbcTemplate.query("SELECT * FROM Person", new BeanPropertyRowMapper<>(Person.class));
    }

    // Using lambda: if there was any person on the list returned by the query than we return it
    // Else we return null
    public Person show(int id) {
        return jdbcTemplate.query("SELECT * FROM PERSON WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(null);
    }

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO Person VALUES (1,?,?,?)", person.getName(), person.getAge(),
                person.getEmail());
    }

    public void update(int id, Person updatedPerson) {
        jdbcTemplate.update("UPDATE Person SET name=?, age=?, email=? WHERE id = ?", updatedPerson.getName(), updatedPerson.getAge(),
                updatedPerson.getEmail(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Person WHERE id = ?",id);
    }
}

package dlivitin.crudapp.dao;

import dlivitin.crudapp.models.Person;
import org.postgresql.Driver;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO {
    private static int PEOPLE_COUNT;

    // It is bad practice defining these constants here, but it is shown just for educational purposes
    private static final String URL = "jdbc:postgresql://localhost:5432/db_for_CRUD";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "root";

    private static Connection connection;


    // The problem with JBDC is that we only can receive SQLException during some error
    // It means that we can not understand in which part of the database work we have problem
    static {
        try{
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public List<Person> index() {
        List<Person> people = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM Person";
            ResultSet resultSet = statement.executeQuery(SQL);

            while(resultSet.next()) {
                Person person = new Person();

                person.setId(resultSet.getInt("id"));
                person.setName(resultSet.getString("name"));
                person.setEmail(resultSet.getString("email"));
                person.setAge(resultSet.getInt("age"));

                people.add(person);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return people;
    }

    public Person show(int id) {
        Person person = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT *  FROM Person WHERE id = ?");
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            person = new Person();
            person.setId(resultSet.getInt("id"));
            person.setName(resultSet.getString("name"));
            person.setEmail(resultSet.getString("email"));
            person.setAge(resultSet.getInt("age"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return person;
    }

    public void save(Person person) {
        try {
            //            Statement statement = connection.createStatement();

            // IT IS BAD PRACTICE BECAUSE OF SQL INJECTIONS
            // For example if we will insert: test@mail.ru'); DROP TABLE Person; -- into email field in the form
            // then we will delete table Person from database
            //            String SQL = "INSERT INTO Person VALUES(" + 1 + ",'" + person.getName() +
            //                    "'," + person.getAge() + ",'" + person.getEmail() + "')";

            // When we get data from users, instead of concatenating strings we should use PreparedStatement

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Person VALUES(1,?,?,?)");
            preparedStatement.setString(1, person.getName());
            preparedStatement.setInt(2,person.getAge());
            preparedStatement.setString(3, person.getEmail());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void update(int id, Person updatedPerson) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Person SET name=?, age=?, email=? WHERE id = ?");
            preparedStatement.setString(1,updatedPerson.getName());
            preparedStatement.setInt(2,updatedPerson.getAge());
            preparedStatement.setString(3,updatedPerson.getEmail());
            preparedStatement.setInt(4,id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void delete(int id) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM Person WHERE id = ?");
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}

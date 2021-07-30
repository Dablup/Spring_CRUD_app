package dlivitin.crudapp.controllers;

import dlivitin.crudapp.dao.PersonDAO;
import dlivitin.crudapp.models.Person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonDAO personDAO;

    @Autowired
    public PeopleController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String index(Model model){
        model.addAttribute("people",personDAO.index());
        // We will get all the people from DAO and pass this people to the corresponding view
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model){
        // Annotation @PathVariable will take id from the URL request
        model.addAttribute("person",personDAO.show(id));

        // We will get only 1 person from DAO using id and pass it to the view
        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(Model model){
        model.addAttribute("person", new Person());
        return "people/new";
    }

    // PostMapping will work only with POST requests ( when we want to save smth on the server/db ...)
    // To validate a data entered by user we need to specify rules in class Person and use annotation @Valid before object in
    // parameters
    // If there is some error the object of BindingResult will be created

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {
        // Annotation @ModelAttribute will create an object automatically with specified fields in html form
        // It does the same as:

        // create(@RequestParam("name) String name, .....)
        // Person person = new Person();
        // person.setName(name);
        // model.addAttribute("person", person);

        // If user entered something with error then we need to return a page with form to create a new person again
        // We will return a page people/new with errors colored in red
        // We pass invalid object 'Person' to the page "people/new"
        // On page ../new we have thymeleaf tag which check if object has errors
        // If object has errors then we print red messages (see html form in "people/new")
        // The same happening with PATCH request
        if (bindingResult.hasErrors())
            return "people/new";

        personDAO.save(person);
        return "redirect:/people";
    }


    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id){
        model.addAttribute("person",personDAO.show(id));
        return "people/edit";
    }

    // Method which will deal with PATCH requests
    // PATCH request will be sent on localhost:8080/people/{id} by form in edit.html
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "people/edit";

        personDAO.update(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        personDAO.delete(id);
        return "redirect:/people";
    }
}

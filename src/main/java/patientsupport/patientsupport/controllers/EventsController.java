package patientsupport.patientsupport.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import patientsupport.patientsupport.helpers.Translator;
import patientsupport.patientsupport.models.events.Event;
import patientsupport.patientsupport.repository.EventRepository;
import patientsupport.patientsupport.repository.EventTypeRepository;
import patientsupport.patientsupport.services.UserService;

/**
 * EventsController
 */
@Controller
@RequestMapping("events")
public class EventsController {

    private String pathView = "events/events";
    private final EventRepository _repository;
    private EventTypeRepository eventTypeRepository;
    private UserService userService;

    /**
     * Constructor DI
     * @param _repository
     * @param eventTypeRepository
     * @param userService
     * @param messageSource
     */
    @Autowired
    public EventsController (
        EventRepository _repository, 
        EventTypeRepository eventTypeRepository,
        UserService userService) {
        this._repository = _repository;
        this.eventTypeRepository = eventTypeRepository;
        this.userService = userService;
    }

    /***
     * List all events
     * @return all items
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView list() {

        ModelAndView view = new ModelAndView();
        view.addObject("Events", _repository.findAll());
        view.addObject("eventTypes", eventTypeRepository.findAll());
        view.addObject("colors", getColors());
        view.setViewName(pathView + "/index");
        return view;
    }

    @RequestMapping(value = "/getEvents", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Iterable<Event> getEvents() {
		try {
			return _repository.findAll();

		} catch (Exception ex) {
			return null;
		}
	}

    /***
     * Method insert data in DB
     * @param itemToCreate Class
     * @param result Result for process
     * @param model Object to send view
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody String store(@RequestBody @Valid Event itemToCreate, HttpServletRequest request) {

        String message = "";
        try {
            itemToCreate.setCreatedAt(new Date());
            itemToCreate.setCreatedBy(userService.getAuthUser().getEmail());
            _repository.save(itemToCreate);
            message =  "{\"Status\":\"200\",\"Message\":\"Registro creado correctamente\"}";
            
        } catch (DataAccessException ex) {
            if(ex.getClass().getSimpleName().equals("DataIntegrityViolationException")) {
                message = "{\"Status\":\"400\",\"Error\":"+Translator.toLocale("label.dataIntegrityViolationException") + "}";
            } else {
                message = "{\"Status\":\"400\",\"Error\":\""+ex.getMessage()+"\"}";
            }
        }

        return message;
    }

    /**
     * Request edit, get data by object filter by id.
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable("id") Integer id,Model model) {
    
        ModelAndView view = new ModelAndView();
        Event itemToEdit = findById(id);
        view.addObject("event", itemToEdit);
        view.addObject("eventType", eventTypeRepository.findAll());
        view.setViewName(pathView + "/edit");
        return view;

    }

    /**
     * Update data from id event.
     * @param id
     * @param itemToEdit
     * @param result
     * @param model
     * @return
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public ModelAndView update(@PathVariable("id") Integer id,@Valid Event itemToEdit, BindingResult result,Model model) {

        ModelAndView view = new ModelAndView();

        if(result.hasErrors()) {
            view.setViewName(pathView + "/edit");
            return view;
        }
        try {
            itemToEdit.setLastModifiedAt(new Date());
            itemToEdit.setLastModifiedBy(userService.getAuthUser().getEmail());
            _repository.save(itemToEdit);
            view.setViewName("redirect:/events");
            return view;
        } catch (DataAccessException ex) {
            if(ex.getClass().getSimpleName().equals("DataIntegrityViolationException")) {
                model.addAttribute("error", Translator.toLocale("label.dataIntegrityViolationException"));
            } else {
                model.addAttribute("error",ex.getMessage());
            }
            view.setViewName(pathView + "/edit");
            return view;
            
        }
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody String delete(@PathVariable("id") Integer id, Model model) {
        try {
            Event itemToDelete = findById(id);
            _repository.delete(itemToDelete);
            return "{\"Status\":\"200\",\"Message\":\"Registro eliminado correctamente\"}";
        } catch (Exception e) {
            return "{\"Status\":\"400\",\"Error\":\"Error al eliminar el registro\"}";
        }

    }

    /**
     * Method find by id model
     */
    private Event findById(int id) {
        return _repository.findById(id).orElseThrow(() -> 
                    new IllegalArgumentException("Invalid item Id:" + id));
    }

    private List<Color> getColors()
    {
        List<Color> list = new ArrayList<Color>();

        Color color1 = new Color("Green", "Verde");
        Color color2 = new Color("Red", "Rojo");
        Color color3 = new Color("Yellow", "Amarillo");
        Color color4 = new Color("Aqua", "Aqua");
        Color color5 = new Color("Blue", "Azul");
        Color color6 = new Color("Fuchsia", "Fuchsia");

        list.add(color1);
        list.add(color2);
        list.add(color3);
        list.add(color4);
        list.add(color5);
        list.add(color6);

        return list;
    }
    
}

class Color {

    public String value;
    public String text;

    Color(String value, String text) {
        this.value = value;
        this.text = text;
    }
    
}
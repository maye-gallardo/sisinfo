package com.ucbcba.proyecto.controllers;

import com.ucbcba.proyecto.entities.Category;
import com.ucbcba.proyecto.entities.History;
import com.ucbcba.proyecto.entities.Users;
import com.ucbcba.proyecto.services.CategoryService;
import com.ucbcba.proyecto.services.HistoryService;
import com.ucbcba.proyecto.services.UploadFileService;
import com.ucbcba.proyecto.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HistoryController {
    private HistoryService historyService;
    private CategoryService categoryService;
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService){
        this.userService = userService;
    }

    @Autowired
    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    private UploadFileService uploadFileService;

    @RequestMapping(value = "/history/new", method = RequestMethod.GET)
    public String newHistory(Model model) {
        History history = new History();
        java.util.Date date = new java.util.Date();
        java.sql.Date today = new java.sql.Date(date.getTime());
        history.setDate(today);
        Iterable<Category> Categories = categoryService.listAllCategorys();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users user = userService.findUserByEmail(auth.getName());
        model.addAttribute("use",user );
        model.addAttribute("categories", Categories);
        model.addAttribute("history", history);
        return "History/historyForm";
    }
//n
    @RequestMapping(value = "/history", method = RequestMethod.POST)
    public String save(@RequestParam("file")MultipartFile file,@Valid History history, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()){
            return "History/historyForm";
        }

        try {

            uploadFileService.saveFile(file ,history.getTitle());
        } catch (IOException e) {
            e.printStackTrace();
        }
        historyService.saveHistory(history);
        return "redirect:/histories";
    }

    @RequestMapping(value = "/history/{id}", method = RequestMethod.GET)
    public String showHistory(@PathVariable Integer id, Model model) {
        History history = historyService.getHistory(id);
        model.addAttribute("history", history);
        return "History/history";
    }

    @RequestMapping(value = "/history/edit/{id}", method = RequestMethod.GET)
    public String editHistory(@PathVariable Integer id, Model model) {
        model.addAttribute("history", historyService.getHistory(id));
        Iterable<Category> Categories = categoryService.listAllCategorys();
        model.addAttribute("categories", Categories);
        return "History/editHistory";
    }

    @RequestMapping(value = "/history/delete/{id}", method = RequestMethod.GET)
    public String deleteHistory(@PathVariable Integer id, Model model) {
        historyService.deleteHistory(id);
        return "redirect:/histories";
    }

    @RequestMapping(value = "/histories", method = RequestMethod.GET)
    public String list(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users user = userService.findUserByEmail(auth.getName());
        model.addAttribute("histories", user.getHistories());
        model.addAttribute("nameA",user.getUsername() );
        return "History/histories";
    }

    @RequestMapping(value = "/histories/busqueda/{word}", method = RequestMethod.GET)
    public String busqueda(@PathVariable String word,Model model) {
        List<History> histories=(List<History>)historyService.listAllHistories();
        List<History> aux=new ArrayList<>();
        for (History history : histories){
            if (history.getTitle().contains(word)){
                aux.add(history);
            }
        }
        model.addAttribute("histories", aux);
        return "History/histories";
    }
    
    @RequestMapping(value = "/histories/title", method = RequestMethod.GET)
    public String listOrderByTitle(Model model) {
        List<History> histories = (List<History>) historyService.listAllHistories();
        for (int i = 0; i < histories.size(); i++) {
            for (int j = 0; j < histories.size() - 1; j++) {
                if (histories.get(j).getTitle().length() < histories.get(j+1).getTitle().length()){
                    History history = histories.get(j);
                    histories.set(j, histories.get(j+1));
                    histories.set(j+1,history);
                }
            }
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users user = userService.findUserByEmail(auth.getName());
        model.addAttribute("nameA",user.getUsername() );
        model.addAttribute("histories", histories);
        return "History/histories";
    }

    @RequestMapping(value = "/histories/date", method = RequestMethod.GET)
    public String listOrderByDate(Model model) {
        List<History> histories = (List<History>) historyService.listAllHistories();
        for (int i = 0; i < histories.size(); i++) {
            for (int j = 0; j < histories.size() - 1; j++) {
                if (compareTwoDate(histories.get(j).getDate(),histories.get(j+1).getDate())){
                    History history = histories.get(j);
                    histories.set(j, histories.get(j+1));
                    histories.set(j+1, history);
                }
            }
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users user = userService.findUserByEmail(auth.getName());
        model.addAttribute("nameA",user.getUsername() );
        model.addAttribute("histories", histories);
        return "History/histories";
    }

    private boolean compareTwoDate(java.sql.Date date1, java.sql.Date date2 ){
        int day1 = date1.getDay();
        int month1 = date1.getMonth();
        int year1 = date1.getYear();
        int day2 = date2.getDay();
        int month2 = date2.getMonth();
        int year2 = date2.getYear();

        if(year1<year2 || (year1==year2 && month1<month2) || (year1==year2 && month1==month2 && day1<day2)){
            return true;
        }
        return false;
    }
}

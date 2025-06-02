package com.csc340.crud_jpa_demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {


  @GetMapping({ "", "/", "/home", "dashboard", "/students/" })
  public String showDashBoard() {
    return "redirect:/students";
  }

}

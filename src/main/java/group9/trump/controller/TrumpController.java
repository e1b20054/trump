package group9.trump.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

@Controller
public class TrumpController {

  @GetMapping("/trump")
  public String trump() {
    return "trump.html";
  }

}

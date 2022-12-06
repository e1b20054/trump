package group9.trump.controller;

import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;

import group9.trump.model.The_GameMapper;
import group9.trump.model.The_Game;
import group9.trump.model.The_Game_DeckMapper;
import group9.trump.model.The_Game_Deck;
import group9.trump.model.The_Game_TehudaMapper;
import group9.trump.model.The_Game_Tehuda;
import group9.trump.model.The_Game_Field1Mapper;
import group9.trump.model.The_Game_Field1;
import group9.trump.model.The_Game_Field2Mapper;
import group9.trump.model.The_Game_Field2;
import group9.trump.model.The_Game_Field3Mapper;
import group9.trump.model.The_Game_Field3;
import group9.trump.model.The_Game_Field4Mapper;
import group9.trump.model.The_Game_Field4;

@Controller
public class The_GameController {
  @Autowired
  The_GameMapper TGMapper;

  @Autowired
  The_Game_DeckMapper TGDMapper;

  @Autowired
  The_Game_TehudaMapper TGTMapper;

  @Autowired
  The_Game_Field1Mapper TGF1Mapper;

  @Autowired
  The_Game_Field2Mapper TGF2Mapper;

  @Autowired
  The_Game_Field3Mapper TGF3Mapper;

  @Autowired
  The_Game_Field4Mapper TGF4Mapper;

  @GetMapping("/the_game")
  public String trump(ModelMap model) {
    int i = 0;
    int x1 = TGF1Mapper.selectNumber();
    int x2 = TGF2Mapper.selectNumber();
    int x3 = TGF3Mapper.selectNumber();
    int x4 = TGF4Mapper.selectNumber();
    TGDMapper.delete();
    TGTMapper.delete();
    // for (int x = TGF4Mapper.selectMaxId(); x > 1; x--) {
    // TGF1Mapper.deleteField1(x);
    // }
    while (x1 > 1) {
      TGF1Mapper.deleteField1(x1);
      x1 = TGF1Mapper.selectNumber();
    }
    ArrayList<The_Game_Field1> the_game_field1 = TGF1Mapper.selectAll();
    model.addAttribute("the_game_field1", the_game_field1);
    while (x2 > 1) {
      TGF2Mapper.deleteField2(x2);
      x2 = TGF2Mapper.selectNumber();
    }
    ArrayList<The_Game_Field2> the_game_field2 = TGF2Mapper.selectAll();
    model.addAttribute("the_game_field2", the_game_field2);
    while (x3 < 100) {
      TGF3Mapper.deleteField3(x3);
      x3 = TGF3Mapper.selectNumber();
    }
    ArrayList<The_Game_Field3> the_game_field3 = TGF3Mapper.selectAll();
    model.addAttribute("the_game_field3", the_game_field3);
    while (x4 < 100) {
      TGF4Mapper.deleteField4(x4);
      x4 = TGF4Mapper.selectNumber();
    }
    ArrayList<The_Game_Field4> the_game_field4 = TGF4Mapper.selectAll();
    model.addAttribute("the_game_field4", the_game_field4);
    ArrayList<The_Game> tehuda = new ArrayList<>();
    ArrayList<The_Game> the_game = TGMapper.selectAll();
    // Collections.shuffle(the_game);
    // model.addAttribute("the_game", the_game);
    while (8 > tehuda.size()) {
      tehuda.add(the_game.get(i));
      i++;
    }
    model.addAttribute("tehuda", tehuda);
    for (int j = i; j < the_game.size(); j++) {
      TGDMapper.insertDeck(the_game.get(j).getNumber());
    }
    for (int k = 0; k < tehuda.size(); k++) {
      TGTMapper.insertTehuda(tehuda.get(k).getNumber());
    }
    return "the_game.html";
  }

  @PostMapping("/the_game/shouhi")
  public String trump2(ModelMap model, @RequestParam Integer id, @RequestParam Integer te) {
    int a = 6;
    int p10 = te + 10;
    int m10 = te - 10;
    int n1 = TGF1Mapper.selectNumber();
    int n2 = TGF2Mapper.selectNumber();
    int n3 = TGF3Mapper.selectNumber();
    int n4 = TGF4Mapper.selectNumber();
    if (id == 1 && (te > n1 || p10 == n1)) {
      TGTMapper.deleteTehuda(te);
      TGF1Mapper.insertField1(te);
    } else if (id == 2 && (te > n2 || p10 == n2)) {
      TGTMapper.deleteTehuda(te);
      TGF2Mapper.insertField2(te);
    } else if (id == 3 && (te < n3 || m10 == n1)) {
      TGTMapper.deleteTehuda(te);
      TGF3Mapper.insertField3(te);
    } else if (id == 4 && (te < n4 || m10 == n1)) {
      TGTMapper.deleteTehuda(te);
      TGF4Mapper.insertField4(te);
    } else {
      String error = "間違え";
      model.addAttribute("error", error);
    }
    ArrayList<The_Game_Tehuda> tehuda = TGTMapper.selectAll();
    ArrayList<The_Game_Field1> the_game_field1 = TGF1Mapper.selectAll();
    ArrayList<The_Game_Field2> the_game_field2 = TGF2Mapper.selectAll();
    ArrayList<The_Game_Field3> the_game_field3 = TGF3Mapper.selectAll();
    ArrayList<The_Game_Field4> the_game_field4 = TGF4Mapper.selectAll();
    model.addAttribute("the_game_field1", the_game_field1);
    model.addAttribute("the_game_field2", the_game_field2);
    model.addAttribute("the_game_field3", the_game_field3);
    model.addAttribute("the_game_field4", the_game_field4);
    // model.addAttribute("the_game", the_game);
    model.addAttribute("tehuda", tehuda);
    int d = TGDMapper.selectCount();
    if (d == 0) {
      a = 7;
    }
    if (tehuda.size() <= a) {
      String end = "end";
      model.addAttribute("end", end);
    }
    return "the_game.html";
  }

  @PostMapping("/the_game/end")
  public String end(ModelMap model) {
    ArrayList<The_Game_Field1> the_game_field1 = TGF1Mapper.selectAll();
    ArrayList<The_Game_Field2> the_game_field2 = TGF2Mapper.selectAll();
    ArrayList<The_Game_Field3> the_game_field3 = TGF3Mapper.selectAll();
    ArrayList<The_Game_Field4> the_game_field4 = TGF4Mapper.selectAll();
    model.addAttribute("the_game_field1", the_game_field1);
    model.addAttribute("the_game_field2", the_game_field2);
    model.addAttribute("the_game_field3", the_game_field3);
    model.addAttribute("the_game_field4", the_game_field4);
    ArrayList<The_Game_Deck> the_game = TGDMapper.selectAll();
    for (int i = TGTMapper.selectCount(); i < 8; i++) {
      if (TGDMapper.selectCount() == 0) {
        i = 100;
      } else {
        TGTMapper.insertTehuda(the_game.get(0).getNumber());
        TGDMapper.deleteDeck(the_game.get(0).getNumber());
        the_game.remove(0);
      }
    }
    ArrayList<The_Game_Tehuda> tehuda = TGTMapper.selectAll();
    model.addAttribute("tehuda", tehuda);
    return "the_game.html";
  }

  // --------------------------------------
  @PostMapping("/the_game/result")
  public String result(ModelMap model) {
    int d = TGDMapper.selectCount();
    int t = TGTMapper.selectCount();
    String win = "win";
    String lose = "lose";
    if (d + t < 10) {
      model.addAttribute("win", win);
    } else {
      model.addAttribute("lose", lose);
    }
    return "the_gameResult.html";
  }

}

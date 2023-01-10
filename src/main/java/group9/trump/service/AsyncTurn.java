package group9.trump.service;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import group9.trump.model.The_Game_TehudaMapper;
import group9.trump.model.The_Game_Tehuda;
import group9.trump.model.TurnMapper;
import group9.trump.model.Turn;

@Service
public class AsyncTurn {

  boolean mada = false;
  boolean user = false;

  private final Logger logger = LoggerFactory.getLogger(AsyncTurn.class);

  @Autowired
  The_Game_TehudaMapper TGTMapper;

  @Autowired
  TurnMapper TurnMapper;

  @Transactional
  public void syncTurn() {

    this.mada = true;
  }

  @Transactional
  public void syncEnd() {

    this.mada = false;
  }

  public void syncChange(String loguinUser) {
    String user;
    int id = TurnMapper.userid(loguinUser) + 1;
    TurnMapper.update0(loguinUser, TGTMapper.selectCount(loguinUser));
    ArrayList<Integer> idList = TurnMapper.userHantei();
    for (int i = 0; i < idList.size(); i++) {
      if (id == idList.get(i)) {
        user = TurnMapper.user(id);
        TurnMapper.update1(user, TGTMapper.selectCount(user));
      } else if (i == (idList.size() - 1)) {
        id = TurnMapper.selectTop();
        user = TurnMapper.user(id);
        TurnMapper.update1(user, TGTMapper.selectCount(user));
      }
    }
  }

  public ArrayList<Turn> syncShowTurn() {
    return TurnMapper.select();
  }

  @Transactional
  public ArrayList<String> syncUser() {
    ArrayList<String> turn = TurnMapper.selectAllUser();
    return turn;
  }

  @Transactional
  public void syncturnUser(String loginUser) {
    TurnMapper.insert(loginUser, TGTMapper.selectCount(loginUser));
    this.user = true;
  }

  @Async
  public void asyncShowTurn(SseEmitter emitter) {
    try {
      while (true) {
        if (false == mada) {
          TimeUnit.MILLISECONDS.sleep(500);
          continue;
        }
        ArrayList<Turn> turn = this.syncShowTurn();
        emitter.send(turn);
        TimeUnit.MILLISECONDS.sleep(1000);
      }
    } catch (Exception e) {
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    } finally {
      emitter.complete();
    }
    System.out.println("asyncShowTurn complete");
  }

  @Async
  public void asyncShowUser(SseEmitter emitter) {
    try {
      while (true) {
        if (false == user) {
          TimeUnit.MILLISECONDS.sleep(500);
          continue;
        }
        ArrayList<String> turn = this.syncUser();
        emitter.send(turn);
        TimeUnit.MILLISECONDS.sleep(1000);
        user = false;
      }
    } catch (Exception e) {
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    } finally {
      emitter.complete();
    }
    System.out.println("asyncShowUser complete");
  }

}

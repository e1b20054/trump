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
    for(int i=0;i<idList.size();i++){
      if(id==idList.get(i)){
        user = TurnMapper.user(id);
        TurnMapper.update1(user, TGTMapper.selectCount(user));
      } else if(i==(idList.size()-1)){
      id = TurnMapper.selectTop();
      user = TurnMapper.user(id);
      TurnMapper.update1(user, TGTMapper.selectCount(user));
      }
    }
  }

  public ArrayList<Turn> syncShowTurn() {
    return TurnMapper.select();
  }

  @Async
  public void asyncShowTurn(SseEmitter emitter) {
    try {
      while (true) {// 無限ループ
        // DBが更新されていなければ0.5s休み
        if (false == mada) {
          TimeUnit.MILLISECONDS.sleep(500);
          continue;
        }
        // DBが更新されていれば更新後のフルーツリストを取得してsendし，1s休み，dbUpdatedをfalseにする
        ArrayList<Turn> turn = this.syncShowTurn();
        emitter.send(turn);
        TimeUnit.MILLISECONDS.sleep(1000);
      }
    } catch (Exception e) {
      // 例外の名前とメッセージだけ表示する
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    } finally {
      emitter.complete();
    }
    System.out.println("asyncShowTurn complete");
  }

}

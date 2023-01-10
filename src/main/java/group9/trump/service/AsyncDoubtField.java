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

import group9.trump.model.ChamberMapper;
import group9.trump.model.Chamber;
import group9.trump.model.TrumpMapper;
import group9.trump.model.Trump;
import group9.trump.model.DeckMapper;
import group9.trump.model.Tehuda;
import group9.trump.model.Deck;
import group9.trump.model.TehudaMapper;
import group9.trump.model.Tehuda;
import group9.trump.model.FieldMapper;
import group9.trump.model.Field;
import group9.trump.model.DoubtResultMapper;
import group9.trump.model.DoubtResult;

@Service
public class AsyncDoubtField {
  boolean dbUpdated = false;

  private final Logger logger = LoggerFactory.getLogger(AsyncDoubtField.class);

  @Autowired
  ChamberMapper CMapper;

  @Autowired
  TrumpMapper TMapper;

  @Autowired
  DeckMapper DMapper;

  @Autowired
  TehudaMapper TTMapper;

  @Autowired
  FieldMapper FMapper;

  @Autowired
  DoubtResultMapper DRMapper;

  @Transactional
  public void syncDoubtField(int number, String mark, int turn, String name, String nextname, int gameturn) {

    FMapper.insertField(number, mark, turn, name, nextname, gameturn);

    // 非同期でDB更新したことを共有する際に利用する
    this.dbUpdated = true;
  }

  // @Transactional
  // public void syncDoubtResult(String result,String name) {

  // DRMapper.insertResult(result,name);

  // // 非同期でDB更新したことを共有する際に利用する
  // this.dbUpdated = true;
  // }

  public Field syncShowDoubt() {
    return FMapper.selectFieldOne();
  }

  public DoubtResult syncShowResult() {
    return DRMapper.selectOne();
  }

  @Async
  public void asyncShowDoubtField(SseEmitter emitter) {
    dbUpdated = true; // dbUpdatedがtrueなら動く
    try {
      while (true) {// 無限ループ
        // DBが更新されていなければ0.5s休み
        if (false == dbUpdated) {
          TimeUnit.MILLISECONDS.sleep(500);
          continue;
        }
        // DBが更新されていれば更新後のフルーツリストを取得してsendし，1s休み，dbUpdatedをfalseにする
        Field field = this.syncShowDoubt();
        emitter.send(field);

        TimeUnit.MILLISECONDS.sleep(1000);
        dbUpdated = false;
      }
    } catch (Exception e) {
      // 例外の名前とメッセージだけ表示する
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    } finally {
      emitter.complete();
    }
    System.out.println("asyncShowDoubtField complete");
  }
}

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

import group9.trump.model.ShitinarabeMatch;
import group9.trump.model.ShitinarabeMatchMapper;
import group9.trump.model.ChamberMapper;
//import group9.trump.model.Chamber;

@Service
public class AsyncShitinarabe {
  boolean dbUpdated = false;

  private final Logger logger = LoggerFactory.getLogger(AsyncShitinarabe.class);

  @Autowired
  ShitinarabeMatchMapper SMMapper;

  @Autowired
  ChamberMapper CMapper;

  @Transactional
  public void syncInsertShitinarabeChamber(String name) {
    ArrayList<ShitinarabeMatch> SChamber = SMMapper.selectAll();
    if (SChamber.size() == 0) {
      SMMapper.insertSmatch(0, "null", name, "null");
    } else {
      SMMapper.insertSmatch(0, "null", name, "null");
      this.dbUpdated = true;
    }
  }

  @Transactional
  public void syncInsertStartflag() {
    SMMapper.insertSmatch(-1, "null", "1", "null");
    this.dbUpdated = true;
  }

  @Transactional
  public void syncUpdateStartflag(String count) {
    SMMapper.updateStartflag(-1, count);
    this.dbUpdated = true;
  }

  @Transactional
  public void syncInsertSMatch(int number, String mark, String name, String nextname) {
    SMMapper.insertSmatch(number, mark, name, nextname);
    this.dbUpdated = true;
  }

  @Async
  public void asyncWait(SseEmitter emitter) {
    dbUpdated = true;
    try {
      while (true) {// 無限ループ
        if (false == dbUpdated) {
          TimeUnit.MILLISECONDS.sleep(500);
          continue;
        }
        ArrayList<ShitinarabeMatch> SMatch = SMMapper.selectAll();
        emitter.send(SMatch);
        TimeUnit.MILLISECONDS.sleep(1000);
        dbUpdated = false;

      }
    } catch (Exception e) {
      // 例外の名前とメッセージだけ表示する
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    } finally {
      emitter.complete();
    }
  }
}

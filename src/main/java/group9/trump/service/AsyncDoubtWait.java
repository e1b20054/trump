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

import group9.trump.model.DoubtChamber;
import group9.trump.model.DoubtChamberMapper;
import group9.trump.model.ChamberMapper;
import group9.trump.model.Chamber;

@Service
public class AsyncDoubtWait {
  boolean dbUpdated = false;

  private final Logger logger = LoggerFactory.getLogger(AsyncDoubtWait.class);

  @Autowired
  DoubtChamberMapper DCMapper;

  @Autowired
  ChamberMapper CMapper;

  @Transactional
  public void syncInsertDoubtChamber(String name) {
    ArrayList<DoubtChamber> DChamber = DCMapper.selectAll();
    if (DChamber.size() == 0) {
      DCMapper.insertDoubtChamber(name, true);
    } else {
      DCMapper.insertDoubtChamber(name, false);
      this.dbUpdated = true;
    }
  }

  @Async
  public void asyncReloadDoubtMatch(SseEmitter emitter) {
    logger.info("start connection");
    dbUpdated = true;
    try {
      while (true) {
        if (false == dbUpdated) {
          TimeUnit.MILLISECONDS.sleep(500);
          continue;
        }
        ArrayList<DoubtChamber> doubtChamber = DCMapper.selectAll();
        emitter.send(doubtChamber);
        TimeUnit.MILLISECONDS.sleep(1000);
        emitter.send(doubtChamber);
        dbUpdated = false;
      }
    } catch (Exception e) {
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    } finally {
      emitter.complete();
    }
    System.out.println("asyncReloadMemoryMatch complete");
  }
}

package group9.trump.service;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import group9.trump.model.MemoryChamberMapper;
import group9.trump.model.MemoryChamber;


@Service
public class AsyncMemory {
  boolean dbUpdated = false;

  @Autowired
  MemoryChamberMapper MCMapper;

  private final Logger logger = LoggerFactory.getLogger(AsyncMemory.class);

  public void syncInsertMemoryChamber(String name, int roomNo) {
    ArrayList<MemoryChamber> MChamber = MCMapper.selectByRoomNo(roomNo);
    if(MChamber.size()==0){
      MCMapper.insertMemoryChamber(name, roomNo, true);
    } else {
      MCMapper.insertMemoryChamber(name, roomNo, false);
    }
  }
}

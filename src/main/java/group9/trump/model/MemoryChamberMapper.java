package group9.trump.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
//import org.apache.ibatis.annotations.Update;

@Mapper
public interface MemoryChamberMapper {

  @Select("SELECT * from MemoryChamber where roomNo = #{roomNo};")
  ArrayList<MemoryChamber> selectByRoomNo(int roomNo);

  @Select("SELECT * from MemoryChamber where name = #{name};")
  MemoryChamber selectByName(String name);

  @Insert("INSERT INTO MemoryChamber (name, roomNo, oya) VALUES (#{name},#{roomNo},#{oya});")
  void insertMemoryChamber(String name, int roomNo, boolean oya);

}

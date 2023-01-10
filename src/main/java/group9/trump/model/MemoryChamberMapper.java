package group9.trump.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;

@Mapper
public interface MemoryChamberMapper {

  @Select("SELECT * from MemoryChamber")
  ArrayList<MemoryChamber> selectAll();

  @Select("SELECT * from MemoryChamber where name = #{name};")
  MemoryChamber selectByName(String name);

  @Insert("INSERT INTO MemoryChamber (id, name, oya, now, isActive, get) VALUES (#{id}, #{name}, #{oya}, #{now}, #{isActive}, #{get});")
  void insertMemoryChamber(int id, String name, boolean oya, boolean now, boolean isActive, int get);

  @Update("UPDATE MemoryChamber SET oya = #{bool} WHERE id = #{id};")
  void updateByOya(boolean bool, int id);

  @Select("SELECT id from MemoryChamber where oya = true")
  int selectByOyaTrueId();

  @Update("UPDATE MemoryChamber SET now = #{bool} WHERE id = #{id};")
  void updateByNow(boolean bool, int id);

  @Select("SELECT id from MemoryChamber where now = true")
  int selectByNowTrueId();

  @Select("SELECT * from MemoryChamber where now = true")
  MemoryChamber selectByNowTrue();

  @Update("UPDATE MemoryChamber SET isActive = #{bool} WHERE id = #{id};")
  void updateByIsActive(boolean bool, int id);

  @Update("UPDATE MemoryChamber SET get = #{get} WHERE name = #{name};")
  void updateByGet(int get, String name);

  @Select("SELECT * from MemoryChamber order by get desc;")
  ArrayList<MemoryChamber> selectRank();

  @Select("SELECT * from MemoryChamber order by get desc limit 1;")
  MemoryChamber selectTop();

  @Delete("DELETE FROM MemoryChamber;")
  void deleteAll();
}

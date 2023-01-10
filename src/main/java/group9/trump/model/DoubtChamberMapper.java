package group9.trump.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;

@Mapper
public interface DoubtChamberMapper {

  @Select("SELECT * from DoubtChamber")
  ArrayList<DoubtChamber> selectAll();

  @Select("SELECT * from DoubtChamber where name = #{name};")
  DoubtChamber selectByName(String name);

  @Select("SELECT name from DoubtChamber where id = #{id};")
  String selectNameById(int id);

  @Insert("INSERT INTO DoubtChamber (name, oya) VALUES (#{name}, #{oya});")
  void insertDoubtChamber(String name, boolean oya);

  @Update("UPDATE DoubtChamber SET oya = #{bool} WHERE id = #{id};")
  void updateByOya(boolean bool, int id);

  @Select("SELECT id from DoubtChamber where oya = true")
  int selectByOyaTrueId();

  @Delete("DELETE FROM DoubtChamber;")
  void deleteAll();
}

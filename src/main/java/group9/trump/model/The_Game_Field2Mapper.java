package group9.trump.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Delete;

@Mapper
public interface The_Game_Field2Mapper {
  @Select("SELECT max(id) from the_game_field2;")
  int selectMaxId();

  @Select("SELECT number from the_game_field2 where id = (SELECT max(id) from the_game_field2);")
  int selectNumber();

  @Select("SELECT * from the_game_field2;")
  ArrayList<The_Game_Field2> selectAll();

  @Insert("INSERT INTO the_game_field2 (number) VALUES (#{number});")
  void insertField2(int number);

  @Delete("DELETE FROM the_game_field2 WHERE id =#{id}")
  boolean deleteField2(int id);

}

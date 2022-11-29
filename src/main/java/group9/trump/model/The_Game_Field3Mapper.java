package group9.trump.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Delete;

@Mapper
public interface The_Game_Field3Mapper {
  @Select("SELECT max(id) from the_game_field3;")
  int selectMaxId();

  @Select("SELECT number from the_game_field3 where id = (SELECT max(id) from the_game_field3);")
  int selectNumber();

  @Select("SELECT * from the_game_field3;")
  ArrayList<The_Game_Field3> selectAll();

  @Insert("INSERT INTO the_game_field3 (number) VALUES (#{number});")
  void insertField3(int number);

  @Delete("DELETE FROM the_game_field3 WHERE id =#{id}")
  boolean deleteField3(int id);

}

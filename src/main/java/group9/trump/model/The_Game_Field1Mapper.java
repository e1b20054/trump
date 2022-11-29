package group9.trump.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Delete;

@Mapper
public interface The_Game_Field1Mapper {
  @Select("SELECT max(id) from the_game_field1;")
  int selectMaxId();

  @Select("SELECT number from the_game_field1 where id = (SELECT max(id) from the_game_field1);")
  int selectNumber();

  @Select("SELECT * from the_game_field1;")
  ArrayList<The_Game_Field1> selectAll();

  @Insert("INSERT INTO the_game_field1 (number) VALUES (#{number});")
  void insertField1(int number);

  @Delete("DELETE FROM the_game_field1 WHERE id =#{id}")
  boolean deleteField1(int id);

}

package group9.trump.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Delete;

@Mapper
public interface The_Game_TehudaMapper {
  @Select("SELECT * from the_game_tehuda where id = #{id};")
  The_Game_Tehuda selectById(int id);

  @Select("SELECT * from the_game_tehuda;")
  ArrayList<The_Game_Tehuda> selectAll();

  @Insert("INSERT INTO the_game_tehuda (number) VALUES (#{number});")
  void insertTehuda(int number);

  @Delete("DELETE FROM the_game_tehuda WHERE number =#{number}")
  boolean deleteTehuda(int number);

}

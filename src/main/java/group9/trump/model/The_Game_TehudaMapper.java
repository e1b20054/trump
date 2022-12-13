package group9.trump.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Delete;

@Mapper
public interface The_Game_TehudaMapper {
  @Select("SELECT number from the_game_tehuda where name =#{name};")
  ArrayList<Integer> selectAll(String name);

  @Insert("INSERT INTO the_game_tehuda (name, number) VALUES (#{name},#{number});")
  void insertTehuda(String name, int number);

  @Delete("DELETE FROM the_game_tehuda WHERE number =#{number}")
  boolean deleteTehuda(int number);

  @Delete("DELETE FROM the_game_tehuda")
  boolean delete();

  @Select("SELECT count(*) from the_game_tehuda where name =#{name};")
  int selectCount(String name);

  @Select("SELECT count(*) from the_game_tehuda;")
  int selectCountAll();

}

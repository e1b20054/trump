package group9.trump.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Delete;

@Mapper
public interface TehudaMapper {
  @Select("SELECT * from tehuda where id = #{id};")
  Tehuda selectById(int id);

  @Select("SELECT * from tehuda;")
  ArrayList<Tehuda> selectAll();

  @Insert("INSERT INTO tehuda (number,mark) VALUES (#{number},#{mark});")
  void insertTehuda(int number, String mark);

  @Delete("DELETE FROM tehuda WHERE number =#{number}")
  boolean deleteTehuda(int number);

}

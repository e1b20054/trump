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

  @Select("SELECT * from tehuda ORDER BY number;")
  ArrayList<Tehuda> selectAllOrder();

  @Insert("INSERT INTO tehuda (number,mark,name) VALUES (#{number},#{mark},#{name});")
  void insertTehuda(int number, String mark, String name);

  @Delete("DELETE FROM tehuda;")
  boolean deleteTehuda();

  @Delete("DELETE FROM tehuda WHERE number =#{number}")
  boolean deleteTehudaByNum(int number);

  @Delete("DELETE FROM tehuda WHERE id =#{id};")
  boolean deleteTehudaById(int id);

}

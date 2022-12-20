package group9.trump.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
//import org.apache.ibatis.annotations.Delete;

@Mapper
public interface ShitinarabeMatchMapper {

  @Select("SELECT * from shitinarabematch;")
  ArrayList<ShitinarabeMatch> selectAll();

  @Select("SELECT * from shitinarabematch where name = #{name};")
  ShitinarabeMatch selectByName(String name);

  @Insert("INSERT INTO shitinarabematch (number, mark, name, nextname) VALUES (#{number}, #{mark}, #{name},#{nextname})")
  void insertSmatch(int number, String mark, String name, String nextname);

  @Update("UPDATE shitinarabematch SET nextname = #{name} where id = #{id};")
  void updateSmatch(int id, String name);

}

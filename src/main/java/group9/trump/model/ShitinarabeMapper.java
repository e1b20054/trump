package group9.trump.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
//import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Update;
//import org.apache.ibatis.annotations.Options;

@Mapper
public interface ShitinarabeMapper {
  @Insert("INSERT INTO shitinarabe (number, mark, name, state) VALUES (#{number}, #{mark}, #{name}, #{state})")
  void insertShitinarabe(int number, String mark, String name, String state);

  @Select("SELECT * from shitinarabe;")
  ArrayList<Shitinarabe> selectAll();

  @Select("SELECT * from shitinarabe where name = #{name} and state = 'tehuda';")
  ArrayList<Shitinarabe> selectTehuda(String name);

  @Select("SELECT * from shitinarabe where mark = #{mark} and number = #{number};")
  Shitinarabe selectCard(String mark, int number);

  @Select("SELECT mark from shitinarabe where id = #{id};")
  String selectMarkById(int id);

  @Select("SELECT number from shitinarabe where id = #{id};")
  int selectNumberById(int id);

  @Update("UPDATE shitinarabe SET state = 'field' where mark = #{mark} and number = #{number};")
  void updateTehuda(String mark, int number);
}

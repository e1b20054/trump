package group9.trump.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;

@Mapper
public interface ShitinarabeMatchMapper {

  @Select("SELECT * from shitinarabematch;")
  ArrayList<ShitinarabeMatch> selectAll();

  @Select("SELECT * from shitinarabematch where name = #{name};")
  ShitinarabeMatch selectByName(String name);

  @Select("SELECT name from shitinarabematch where number = #{number};")
  String selectFlag(int number);

  @Select("SELECT nextname from shitinarabematch where name = #{name} limit 1;")
  String selectNextnameByName(String name);

  @Select("SELECT name from shitinarabematch where id = #{id};")
  String selectNameById(int id);

  @Select("SELECT id from shitinarabematch order by id desc limit 1;")
  int selectId();

  @Select("SELECT nextName from shitinarabematch order by id desc limit 1;")
  String selectNextname();

  @Select("SELECT name from shitinarabematch order by id desc limit 1;")
  String selectWinner();

  @Insert("INSERT INTO shitinarabematch (number, mark, name, nextname) VALUES (#{number}, #{mark}, #{name},#{nextname})")
  void insertSmatch(int number, String mark, String name, String nextname);

  @Update("Update shitinarabematch SET name = #{name} where number = #{number};")
  void updateStartflag(int number, String name);

  @Update("UPDATE shitinarabematch SET nextname = #{name} where id = #{id};")
  void updateSmatch(int id, String name);

  @Delete("TRUNCATE TABLE ShitinarabeMatch RESTART IDENTITY")
  void deleteShitinarabeMatch();

}

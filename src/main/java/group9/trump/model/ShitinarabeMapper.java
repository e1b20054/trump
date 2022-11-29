package group9.trump.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Delete;
//import org.apache.ibatis.annotations.Options;

@Mapper
public interface ShitinarabeMapper {
  @Insert("INSERT INTO shitinarabe (number, mark, name, state) VALUES (#{number}, #{mark}, #{name}, #{state})")
  void insertShitinarabe(int number, String mark, int name, String state);

}

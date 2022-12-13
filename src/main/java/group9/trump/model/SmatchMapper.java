package group9.trump.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Delete;

@Mapper
public interface SmatchMapper {
  @Insert("INSERT INTO smatch (number, mark, name, nextuser, isActive) VALUES (#{number}, #{mark}, #{name}, #{nextuser}, #{isActive})")
  void insertSmatch(int number, String mark, int name, int nextuser, Boolean isActive);

}

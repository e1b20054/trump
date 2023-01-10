package group9.trump.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface TurnMapper {
  @Insert("INSERT INTO turn (name, tehuda, turn) VALUES (#{user},#{tehuda},1);")
  void insert(String user, int tehuda);

  @Select("SELECT * from turn;")
  ArrayList<Turn> select();

  @Select("select id from turn where name = #{user};")
  int userid(String user);

  @Select("select id from turn;")
  ArrayList<Integer> userHantei();

  @Select("select name from turn where id = #{id};")
  String user(int id);

  @Select("SELECT id from turn order by id limit 1;")
  int selectTop();

  @Select("SELECT name from turn where turn = 1;")
  String selectUser();

  @Update("UPDATE turn SET tehuda=#{tehuda}, turn=0 WHERE name = #{user};")
  void update0(String user, int tehuda);

  @Update("UPDATE turn SET tehuda=#{tehuda}, turn=1 WHERE name = #{user};")
  void update1(String user, int tehuda);

  @Select("SELECT count(*) from turn where name != #{user};")
  int selectCountOther(String user);

  @Select("SELECT name from turn where name != #{user};")
  ArrayList<String> selectOther(String user);

  @Select("SELECT name from turn;")
  ArrayList<String> selectAllUser();

  @Delete("DELETE FROM turn;")
  boolean delete();
}

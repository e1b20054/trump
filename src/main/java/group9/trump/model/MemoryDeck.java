package group9.trump.model;

public class MemoryDeck {
  int id;
  int number;
  String mark;
  boolean open;
  boolean get;
  String getter;
  boolean endMatch;
  String nowUser;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public String getMark() {
    return mark;
  }

  public void setMark(String mark) {
    this.mark = mark;
  }

  public boolean getOpen() {
    return open;
  }

  public void setOpen(boolean open) {
    this.open = open;
  }

  public boolean getGet() {
    return get;
  }

  public void setGet(boolean get) {
    this.get = get;
  }

  public String getGetter() {
    return getter;
  }

  public void setGetter(String getter) {
    this.getter = getter;
  }

  public boolean getEndMatch() {
    return endMatch;
  }

  public void setEndMatch(boolean endMatch) {
    this.endMatch = endMatch;
  }

  public String getNowUser() {
    return nowUser;
  }

  public void setNowUser(String nowUser) {
    this.nowUser = nowUser;
  }
}

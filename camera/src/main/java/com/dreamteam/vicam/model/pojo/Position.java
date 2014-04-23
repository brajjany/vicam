package com.dreamteam.vicam.model.pojo;

import com.dreamteam.vicam.model.interfaces.Identifiable;
import com.dreamteam.vicam.presenter.utility.Utils;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by fsommar on 2014-04-01.
 */
@DatabaseTable(tableName = "position")
public class Position implements Identifiable {

  public static final int LOWER_BOUND = 0x0000, UPPER_BOUND = 0xFFFF, MID = 0x8000;

  @DatabaseField(columnName = "id", generatedId = true)
  private int id = -1;
  @DatabaseField(columnName = "pan")
  private int pan;
  @DatabaseField(columnName = "tilt")
  private int tilt;

  Position() {
    // ORMLite needs a no-arg constructor
  }

  public Position(int id, int pan, int tilt) {
    this(pan, tilt);
    this.id = id;
  }

  public Position(int pan, int tilt) {
    Utils.rangeCheck(pan, LOWER_BOUND, UPPER_BOUND);
    Utils.rangeCheck(tilt, LOWER_BOUND, UPPER_BOUND);
    this.pan = pan;
    this.tilt = tilt;
  }

  public int getId() {
    return id;
  }

  @Override
  public String toString() {
    return "Position{" +
           "id=" + id +
           ", pan=" + pan +
           ", tilt=" + tilt +
           '}';
  }

  public int getPan() {
    return pan;
  }

  public int getTilt() {
    return tilt;
  }
}

package com.jawidmohammadi.nasaapod.model.entity;

  import androidx.annotation.NonNull;
  import androidx.room.ColumnInfo;
  import androidx.room.Entity;
  import androidx.room.ForeignKey;
  import androidx.room.PrimaryKey;
  import java.util.Date;

  @Entity(
    foreignKeys =  @ForeignKey(
        entity = Apod.class,
        parentColumns = "apod_id",
        childColumns = "apod_id",
        onDelete = ForeignKey.CASCADE
    )
)
public class Access {

  @ColumnInfo(name = "access_id")
  @PrimaryKey(autoGenerate = true)
  private long id;

  @ColumnInfo(name = "apod_id", index= true)
  private long apodId;

  @NonNull
  private Date timestap = new Date();


    public long getId() {
      return id;
    }

    public void setId(long id) {
      this.id = id;
    }

    public long getApodId() {
      return apodId;
    }

    public void setApodId(long apodId) {
      this.apodId = apodId;
    }

    public Date getTimestap() {
      return timestap;
    }

    public void setTimestap(Date timestap) {
      this.timestap = timestap;
    }
  }

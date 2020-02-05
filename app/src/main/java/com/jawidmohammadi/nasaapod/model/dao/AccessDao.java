package com.jawidmohammadi.nasaapod.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.jawidmohammadi.nasaapod.model.entity.Access;
import io.reactivex.Single;
import java.util.List;

@Dao
public interface AccessDao {

  @Insert
  Single<Long> insert(Access access);

  @Query("SELECT * FROM Access WHERE apod_id = :apodId ORDER BY timestap DESC")
  Single<List<Access>> select(long apodId);

}

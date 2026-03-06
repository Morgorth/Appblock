package com.focuslock.data.db.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.focuslock.data.db.entities.ScheduleDay;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ScheduleDayDao_Impl implements ScheduleDayDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ScheduleDay> __insertionAdapterOfScheduleDay;

  private final EntityDeletionOrUpdateAdapter<ScheduleDay> __deletionAdapterOfScheduleDay;

  private final EntityDeletionOrUpdateAdapter<ScheduleDay> __updateAdapterOfScheduleDay;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllForProfile;

  public ScheduleDayDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfScheduleDay = new EntityInsertionAdapter<ScheduleDay>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `schedule_days` (`id`,`profileId`,`dayOfWeek`,`isEnabled`,`startHour`,`startMinute`,`endHour`,`endMinute`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ScheduleDay entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getProfileId());
        statement.bindLong(3, entity.getDayOfWeek());
        final int _tmp = entity.isEnabled() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindLong(5, entity.getStartHour());
        statement.bindLong(6, entity.getStartMinute());
        statement.bindLong(7, entity.getEndHour());
        statement.bindLong(8, entity.getEndMinute());
      }
    };
    this.__deletionAdapterOfScheduleDay = new EntityDeletionOrUpdateAdapter<ScheduleDay>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `schedule_days` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ScheduleDay entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfScheduleDay = new EntityDeletionOrUpdateAdapter<ScheduleDay>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `schedule_days` SET `id` = ?,`profileId` = ?,`dayOfWeek` = ?,`isEnabled` = ?,`startHour` = ?,`startMinute` = ?,`endHour` = ?,`endMinute` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ScheduleDay entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getProfileId());
        statement.bindLong(3, entity.getDayOfWeek());
        final int _tmp = entity.isEnabled() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindLong(5, entity.getStartHour());
        statement.bindLong(6, entity.getStartMinute());
        statement.bindLong(7, entity.getEndHour());
        statement.bindLong(8, entity.getEndMinute());
        statement.bindLong(9, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAllForProfile = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM schedule_days WHERE profileId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final ScheduleDay scheduleDay, final Continuation<? super Long> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfScheduleDay.insertAndReturnId(scheduleDay);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object insertAll(final List<ScheduleDay> days, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfScheduleDay.insert(days);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object delete(final ScheduleDay scheduleDay, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfScheduleDay.handle(scheduleDay);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object update(final ScheduleDay scheduleDay, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfScheduleDay.handle(scheduleDay);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object deleteAllForProfile(final long profileId, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllForProfile.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, profileId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAllForProfile.release(_stmt);
        }
      }
    }, arg1);
  }

  @Override
  public Object getScheduleForProfile(final long profileId,
      final Continuation<? super List<ScheduleDay>> arg1) {
    final String _sql = "SELECT * FROM schedule_days WHERE profileId = ? ORDER BY dayOfWeek ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, profileId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ScheduleDay>>() {
      @Override
      @NonNull
      public List<ScheduleDay> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profileId");
          final int _cursorIndexOfDayOfWeek = CursorUtil.getColumnIndexOrThrow(_cursor, "dayOfWeek");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
          final int _cursorIndexOfStartHour = CursorUtil.getColumnIndexOrThrow(_cursor, "startHour");
          final int _cursorIndexOfStartMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "startMinute");
          final int _cursorIndexOfEndHour = CursorUtil.getColumnIndexOrThrow(_cursor, "endHour");
          final int _cursorIndexOfEndMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "endMinute");
          final List<ScheduleDay> _result = new ArrayList<ScheduleDay>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ScheduleDay _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpProfileId;
            _tmpProfileId = _cursor.getLong(_cursorIndexOfProfileId);
            final int _tmpDayOfWeek;
            _tmpDayOfWeek = _cursor.getInt(_cursorIndexOfDayOfWeek);
            final boolean _tmpIsEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp != 0;
            final int _tmpStartHour;
            _tmpStartHour = _cursor.getInt(_cursorIndexOfStartHour);
            final int _tmpStartMinute;
            _tmpStartMinute = _cursor.getInt(_cursorIndexOfStartMinute);
            final int _tmpEndHour;
            _tmpEndHour = _cursor.getInt(_cursorIndexOfEndHour);
            final int _tmpEndMinute;
            _tmpEndMinute = _cursor.getInt(_cursorIndexOfEndMinute);
            _item = new ScheduleDay(_tmpId,_tmpProfileId,_tmpDayOfWeek,_tmpIsEnabled,_tmpStartHour,_tmpStartMinute,_tmpEndHour,_tmpEndMinute);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg1);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

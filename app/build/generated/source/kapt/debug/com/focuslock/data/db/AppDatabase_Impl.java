package com.focuslock.data.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.focuslock.data.db.dao.BlockedAppDao;
import com.focuslock.data.db.dao.BlockedAppDao_Impl;
import com.focuslock.data.db.dao.OverrideLogDao;
import com.focuslock.data.db.dao.OverrideLogDao_Impl;
import com.focuslock.data.db.dao.ProfileDao;
import com.focuslock.data.db.dao.ProfileDao_Impl;
import com.focuslock.data.db.dao.ScheduleDayDao;
import com.focuslock.data.db.dao.ScheduleDayDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile ProfileDao _profileDao;

  private volatile BlockedAppDao _blockedAppDao;

  private volatile ScheduleDayDao _scheduleDayDao;

  private volatile OverrideLogDao _overrideLogDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `profiles` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `isEnabled` INTEGER NOT NULL, `overrideDurationMinutes` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `blocked_apps` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `profileId` INTEGER NOT NULL, `packageName` TEXT NOT NULL, `appName` TEXT NOT NULL, FOREIGN KEY(`profileId`) REFERENCES `profiles`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_blocked_apps_profileId` ON `blocked_apps` (`profileId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `schedule_days` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `profileId` INTEGER NOT NULL, `dayOfWeek` INTEGER NOT NULL, `isEnabled` INTEGER NOT NULL, `startHour` INTEGER NOT NULL, `startMinute` INTEGER NOT NULL, `endHour` INTEGER NOT NULL, `endMinute` INTEGER NOT NULL, FOREIGN KEY(`profileId`) REFERENCES `profiles`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_schedule_days_profileId` ON `schedule_days` (`profileId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `override_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `packageName` TEXT NOT NULL, `appName` TEXT NOT NULL, `profileName` TEXT NOT NULL, `justification` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `durationMinutes` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'c02e8f3bee300a8cf3754023f85a58da')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `profiles`");
        db.execSQL("DROP TABLE IF EXISTS `blocked_apps`");
        db.execSQL("DROP TABLE IF EXISTS `schedule_days`");
        db.execSQL("DROP TABLE IF EXISTS `override_logs`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsProfiles = new HashMap<String, TableInfo.Column>(5);
        _columnsProfiles.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProfiles.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProfiles.put("isEnabled", new TableInfo.Column("isEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProfiles.put("overrideDurationMinutes", new TableInfo.Column("overrideDurationMinutes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProfiles.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysProfiles = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesProfiles = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoProfiles = new TableInfo("profiles", _columnsProfiles, _foreignKeysProfiles, _indicesProfiles);
        final TableInfo _existingProfiles = TableInfo.read(db, "profiles");
        if (!_infoProfiles.equals(_existingProfiles)) {
          return new RoomOpenHelper.ValidationResult(false, "profiles(com.focuslock.data.db.entities.Profile).\n"
                  + " Expected:\n" + _infoProfiles + "\n"
                  + " Found:\n" + _existingProfiles);
        }
        final HashMap<String, TableInfo.Column> _columnsBlockedApps = new HashMap<String, TableInfo.Column>(4);
        _columnsBlockedApps.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBlockedApps.put("profileId", new TableInfo.Column("profileId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBlockedApps.put("packageName", new TableInfo.Column("packageName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBlockedApps.put("appName", new TableInfo.Column("appName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBlockedApps = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysBlockedApps.add(new TableInfo.ForeignKey("profiles", "CASCADE", "NO ACTION", Arrays.asList("profileId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesBlockedApps = new HashSet<TableInfo.Index>(1);
        _indicesBlockedApps.add(new TableInfo.Index("index_blocked_apps_profileId", false, Arrays.asList("profileId"), Arrays.asList("ASC")));
        final TableInfo _infoBlockedApps = new TableInfo("blocked_apps", _columnsBlockedApps, _foreignKeysBlockedApps, _indicesBlockedApps);
        final TableInfo _existingBlockedApps = TableInfo.read(db, "blocked_apps");
        if (!_infoBlockedApps.equals(_existingBlockedApps)) {
          return new RoomOpenHelper.ValidationResult(false, "blocked_apps(com.focuslock.data.db.entities.BlockedApp).\n"
                  + " Expected:\n" + _infoBlockedApps + "\n"
                  + " Found:\n" + _existingBlockedApps);
        }
        final HashMap<String, TableInfo.Column> _columnsScheduleDays = new HashMap<String, TableInfo.Column>(8);
        _columnsScheduleDays.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduleDays.put("profileId", new TableInfo.Column("profileId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduleDays.put("dayOfWeek", new TableInfo.Column("dayOfWeek", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduleDays.put("isEnabled", new TableInfo.Column("isEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduleDays.put("startHour", new TableInfo.Column("startHour", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduleDays.put("startMinute", new TableInfo.Column("startMinute", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduleDays.put("endHour", new TableInfo.Column("endHour", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduleDays.put("endMinute", new TableInfo.Column("endMinute", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysScheduleDays = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysScheduleDays.add(new TableInfo.ForeignKey("profiles", "CASCADE", "NO ACTION", Arrays.asList("profileId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesScheduleDays = new HashSet<TableInfo.Index>(1);
        _indicesScheduleDays.add(new TableInfo.Index("index_schedule_days_profileId", false, Arrays.asList("profileId"), Arrays.asList("ASC")));
        final TableInfo _infoScheduleDays = new TableInfo("schedule_days", _columnsScheduleDays, _foreignKeysScheduleDays, _indicesScheduleDays);
        final TableInfo _existingScheduleDays = TableInfo.read(db, "schedule_days");
        if (!_infoScheduleDays.equals(_existingScheduleDays)) {
          return new RoomOpenHelper.ValidationResult(false, "schedule_days(com.focuslock.data.db.entities.ScheduleDay).\n"
                  + " Expected:\n" + _infoScheduleDays + "\n"
                  + " Found:\n" + _existingScheduleDays);
        }
        final HashMap<String, TableInfo.Column> _columnsOverrideLogs = new HashMap<String, TableInfo.Column>(7);
        _columnsOverrideLogs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOverrideLogs.put("packageName", new TableInfo.Column("packageName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOverrideLogs.put("appName", new TableInfo.Column("appName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOverrideLogs.put("profileName", new TableInfo.Column("profileName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOverrideLogs.put("justification", new TableInfo.Column("justification", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOverrideLogs.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOverrideLogs.put("durationMinutes", new TableInfo.Column("durationMinutes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysOverrideLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesOverrideLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoOverrideLogs = new TableInfo("override_logs", _columnsOverrideLogs, _foreignKeysOverrideLogs, _indicesOverrideLogs);
        final TableInfo _existingOverrideLogs = TableInfo.read(db, "override_logs");
        if (!_infoOverrideLogs.equals(_existingOverrideLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "override_logs(com.focuslock.data.db.entities.OverrideLog).\n"
                  + " Expected:\n" + _infoOverrideLogs + "\n"
                  + " Found:\n" + _existingOverrideLogs);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "c02e8f3bee300a8cf3754023f85a58da", "5a45a870ff3c935de5f2dc01b980af92");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "profiles","blocked_apps","schedule_days","override_logs");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `profiles`");
      _db.execSQL("DELETE FROM `blocked_apps`");
      _db.execSQL("DELETE FROM `schedule_days`");
      _db.execSQL("DELETE FROM `override_logs`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(ProfileDao.class, ProfileDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BlockedAppDao.class, BlockedAppDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ScheduleDayDao.class, ScheduleDayDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(OverrideLogDao.class, OverrideLogDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public ProfileDao profileDao() {
    if (_profileDao != null) {
      return _profileDao;
    } else {
      synchronized(this) {
        if(_profileDao == null) {
          _profileDao = new ProfileDao_Impl(this);
        }
        return _profileDao;
      }
    }
  }

  @Override
  public BlockedAppDao blockedAppDao() {
    if (_blockedAppDao != null) {
      return _blockedAppDao;
    } else {
      synchronized(this) {
        if(_blockedAppDao == null) {
          _blockedAppDao = new BlockedAppDao_Impl(this);
        }
        return _blockedAppDao;
      }
    }
  }

  @Override
  public ScheduleDayDao scheduleDayDao() {
    if (_scheduleDayDao != null) {
      return _scheduleDayDao;
    } else {
      synchronized(this) {
        if(_scheduleDayDao == null) {
          _scheduleDayDao = new ScheduleDayDao_Impl(this);
        }
        return _scheduleDayDao;
      }
    }
  }

  @Override
  public OverrideLogDao overrideLogDao() {
    if (_overrideLogDao != null) {
      return _overrideLogDao;
    } else {
      synchronized(this) {
        if(_overrideLogDao == null) {
          _overrideLogDao = new OverrideLogDao_Impl(this);
        }
        return _overrideLogDao;
      }
    }
  }
}

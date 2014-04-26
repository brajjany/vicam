package com.dreamteam.vicam.model.database.ormlite;

import com.dreamteam.vicam.model.database.PresetDAO;
import com.dreamteam.vicam.model.pojo.CameraState;
import com.dreamteam.vicam.model.pojo.Focus;
import com.dreamteam.vicam.model.pojo.Position;
import com.dreamteam.vicam.model.pojo.Preset;
import com.dreamteam.vicam.presenter.utility.Utils;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

import static com.dreamteam.vicam.presenter.utility.Utils.ORMLite;

/**
 * Created by fsommar on 2014-04-03.
 */
public class PresetDAOImpl implements PresetDAO {

  private final Dao<CameraState, Integer> cameraStateDao;
  private final Dao<Preset, Integer> presetDao;
  private final Dao<Position, Integer> positionDao;
  private final Dao<Focus, Integer> focusDao;
  private final OrmLiteSqliteOpenHelper ormLiteHelper;

  public PresetDAOImpl(OrmLiteSqliteOpenHelper ormLiteHelper) throws SQLException {
    this.ormLiteHelper = ormLiteHelper;
    this.presetDao = ormLiteHelper.getDao(Preset.class);
    this.cameraStateDao = ormLiteHelper.getDao(CameraState.class);
    this.positionDao = ormLiteHelper.getDao(Position.class);
    this.focusDao = ormLiteHelper.getDao(Focus.class);
  }

  @Override
  public int insertPreset(final Preset preset) {
    try {
      return TransactionManager.callInTransaction(
          ormLiteHelper.getConnectionSource(),
          new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
              positionDao.create(preset.getCameraState().getPosition());
              focusDao.create(preset.getCameraState().getFocus());
              cameraStateDao.create(preset.getCameraState());
              presetDao.create(preset);
              return preset.getId();
            }
          }
      );
    } catch (SQLException e) {
      Utils.databaseLog(String.format("Error while inserting preset (%s)", preset), e);
      return -1;
    }
  }

  @Override
  public Preset findPreset(int id) {
    return ORMLite.find(presetDao, id);
  }

  @Override
  public boolean updatePreset(final Preset preset) {
    try {
      TransactionManager.callInTransaction(
          ormLiteHelper.getConnectionSource(),
          new Callable<Object>() {
            @Override
            public Object call() throws Exception {
              positionDao.update(preset.getCameraState().getPosition());
              focusDao.update(preset.getCameraState().getFocus());
              cameraStateDao.update(preset.getCameraState());
              presetDao.update(preset);
              return null;
            }
          }
      );
      return true;
    } catch (SQLException e) {
      Utils.databaseLog(String.format("Error while updating preset (%s)", preset), e);
      return false;
    }
  }

  @Override
  public boolean deletePreset(int id) {
    return ORMLite.delete(presetDao, id);
  }

  @Override
  public List<Preset> getPresets() {
    return ORMLite.getAll(presetDao);
  }
}
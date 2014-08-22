package com.example.britishpetroleum.helper;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.britishpetroleum.model.JobDetails;
import com.google.android.gms.maps.model.LatLng;

public class PostORM {

	DatabaseWrapper databaseWrapper;
	SQLiteDatabase aDatabase;

	private Context mContext;

	private static final String TABLE_NAME = "JobDetails";

	private static final String COMMA_SEP = ", ";

	private static final String COLUMN_ID_TYPE = "TEXT PRIMARY KEY";
	private static final String COLUMN_ID = "job_id";

	private static final String COLUMN_TITLE_TYPE = "TEXT";
	private static final String COLUMN_TITLE = "job_title";

	private static final String COLUMN_SYNOPSIS_TYPE = "TEXT";
	private static final String COLUMN_SYNOPSIS = "role_synopsis";

	private static final String COLUMN_ACCOUNTABILITIES_TYPE = "TEXT";
	private static final String COLUMN_ACCOUNTABILITIES = "key_accountabilities";

	private static final String COLUMN_ESSENTIAL_TYPE = "TEXT";
	private static final String COLUMN_ESSENTIAL = "essential_education";

	private static final String COLUMN_DESIRABLE_TYPE = "TEXT";
	private static final String COLUMN_DESIRABLE = "desirable_qualification";

	private static final String COLUMN_LATITUDE_TYPE = "TEXT";
	private static final String COLUMN_LATITUDE = "latitude";

	private static final String COLUMN_LONGITUDE_TYPE = "TEXT";
	private static final String COLUMN_LONGITUDE = "longitude";

	private static final String COLUMN_ISAVAILABLE_TYPE = "TEXT";
	private static final String COLUMN_ISAVAILABLE = "is_available";

	public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
			+ " (" + COLUMN_ID + " " + COLUMN_ID_TYPE + COMMA_SEP
			+ COLUMN_TITLE + " " + COLUMN_TITLE_TYPE + COMMA_SEP
			+ COLUMN_SYNOPSIS + " " + COLUMN_SYNOPSIS_TYPE + COMMA_SEP
			+ COLUMN_ACCOUNTABILITIES + " " + COLUMN_ACCOUNTABILITIES_TYPE
			+ COMMA_SEP + COLUMN_ESSENTIAL + " " + COLUMN_ESSENTIAL_TYPE
			+ COMMA_SEP + COLUMN_DESIRABLE + " " + COLUMN_DESIRABLE_TYPE
			+ COMMA_SEP + COLUMN_LATITUDE + " " + COLUMN_LATITUDE_TYPE
			+ COMMA_SEP + COLUMN_LONGITUDE + " " + COLUMN_LONGITUDE_TYPE
			+ COMMA_SEP + COLUMN_ISAVAILABLE + " " + COLUMN_ISAVAILABLE_TYPE
			+ ")";

	// public JobDetails(String job_id, String job_title,
	// String role_synopsis, String key_accountabilities,
	// String essential_education, String desirable_qualification,
	// String latitude, String longitude, String is_available) {
	String[] DETAIL_COLUMN = new String[] { COLUMN_ID, COLUMN_TITLE,
			COLUMN_SYNOPSIS, COLUMN_ACCOUNTABILITIES, COLUMN_ESSENTIAL,
			COLUMN_DESIRABLE, COLUMN_LATITUDE, COLUMN_LONGITUDE,
			COLUMN_ISAVAILABLE };

	public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS "
			+ TABLE_NAME;

	public PostORM(Context iContext) {
		this.mContext = iContext;
	}

	public void insertPost(JobDetails iJobDetails) {
		databaseWrapper = new DatabaseWrapper(mContext);
		aDatabase = databaseWrapper.getWritableDatabase();

		ContentValues values = postToContentValues(iJobDetails);
		try {
			long postId = aDatabase.insert(PostORM.TABLE_NAME, "null", values);
			Log.i("PostORM", "Inserted new Post with ID: " + postId);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		if (aDatabase.isOpen()) {
			aDatabase.close();
		}

	}

	public void insertPosts(ArrayList<JobDetails> iJobDetailsArray) {
		databaseWrapper = new DatabaseWrapper(mContext);
		aDatabase = databaseWrapper.getWritableDatabase();
		aDatabase.beginTransaction();
		try {
			for (JobDetails iJobDetails : iJobDetailsArray) {
				ContentValues values = new ContentValues();
				values.put(PostORM.COLUMN_ID, iJobDetails.getJob_id());
				values.put(PostORM.COLUMN_TITLE, iJobDetails.getJob_title());
				values.put(PostORM.COLUMN_SYNOPSIS,
						iJobDetails.getRole_synopsis());
				values.put(PostORM.COLUMN_ACCOUNTABILITIES,
						iJobDetails.getKey_accountabilities());
				values.put(PostORM.COLUMN_ESSENTIAL,
						iJobDetails.getEssential_education());
				values.put(PostORM.COLUMN_DESIRABLE,
						iJobDetails.getDesirable_qualification());
				values.put(PostORM.COLUMN_LATITUDE, iJobDetails.getLatitude());
				values.put(PostORM.COLUMN_LONGITUDE, iJobDetails.getLongitude());
				values.put(PostORM.COLUMN_ISAVAILABLE,
						iJobDetails.getIs_available());
				aDatabase.insert(PostORM.TABLE_NAME, "null", values);
			}
			aDatabase.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			aDatabase.endTransaction();
			aDatabase.close();

		}

	}

	/**
	 * Packs a Post object into a ContentValues map for use with SQL inserts.
	 */
	private static ContentValues postToContentValues(JobDetails iJobDetails) {
		ContentValues values = new ContentValues();
		values.put(PostORM.COLUMN_ID, iJobDetails.getJob_id());
		values.put(PostORM.COLUMN_TITLE, iJobDetails.getJob_title());
		values.put(PostORM.COLUMN_SYNOPSIS, iJobDetails.getRole_synopsis());
		values.put(PostORM.COLUMN_ACCOUNTABILITIES,
				iJobDetails.getKey_accountabilities());
		values.put(PostORM.COLUMN_ESSENTIAL,
				iJobDetails.getEssential_education());
		values.put(PostORM.COLUMN_DESIRABLE,
				iJobDetails.getDesirable_qualification());
		values.put(PostORM.COLUMN_LATITUDE, iJobDetails.getLatitude());
		values.put(PostORM.COLUMN_LONGITUDE, iJobDetails.getLongitude());
		values.put(PostORM.COLUMN_ISAVAILABLE, iJobDetails.getIs_available());

		return values;
	}

	public ArrayList<JobDetails> getPosts() {
		databaseWrapper = new DatabaseWrapper(mContext);
		aDatabase = databaseWrapper.getWritableDatabase();
		Cursor aCursor = aDatabase.query(PostORM.TABLE_NAME, DETAIL_COLUMN,
				null, null, null, null, null);
		ArrayList<JobDetails> aPostArrayList = new ArrayList<JobDetails>();

		if (aCursor.getCount() > 0) {
			if (aCursor.moveToFirst()) {
				do {
					JobDetails aJobDetails = new JobDetails(
							aCursor.getString(0), aCursor.getString(1),
							aCursor.getString(2), aCursor.getString(3),
							aCursor.getString(4), aCursor.getString(5),
							aCursor.getString(6), aCursor.getString(7),
							aCursor.getString(8));
					aPostArrayList.add(aJobDetails);
				} while (aCursor.moveToNext());
			}

		}
		aCursor.close();
		if (aDatabase.isOpen()) {
			aDatabase.close();
		}
		return aPostArrayList;

	}

	// private static JobDetails cursorToPost(Cursor aCursor) {
	// JobDetails aJobDetails = new JobDetails(aCursor.getString(aCursor
	// .getColumnIndex(COLUMN_ID)), aCursor.getString(aCursor
	// .getColumnIndex(COLUMN_TITLE)), aCursor.getString(aCursor
	// .getColumnIndex(COLUMN_SYNOPSIS)), aCursor.getString(aCursor
	// .getColumnIndex(COLUMN_ACCOUNTABILITIES)),
	// aCursor.getString(aCursor.getColumnIndex(COLUMN_ESSENTIAL)),
	// aCursor.getString(aCursor.getColumnIndex(COLUMN_DESIRABLE)),
	// aCursor.getString(aCursor.getColumnIndex(COLUMN_LATITUDE)),
	// aCursor.getString(aCursor.getColumnIndex(COLUMN_LONGITUDE)),
	// aCursor.getString(aCursor.getColumnIndex(COLUMN_ISAVAILABLE)));
	//
	// return aJobDetails;
	//
	// }

	public JobDetails getById(String iJobId) {
		databaseWrapper = new DatabaseWrapper(mContext);
		aDatabase = databaseWrapper.getReadableDatabase();
		JobDetails aJobDetails = null;
		if (aDatabase != null) {
			Cursor aCursor = aDatabase.query(PostORM.TABLE_NAME, DETAIL_COLUMN,
					"job_id = " + iJobId.toString(), null, null, null, null);
			// Cursor aCursor = aDatabase.query(PostORM.TABLE_NAME,
			// DETAIL_COLUMN,
			// null, null, null, null, null);
			if (aCursor.getCount() > 0) {
				if (aCursor.moveToFirst()) {
					do {
						aJobDetails = new JobDetails(aCursor.getString(0),
								aCursor.getString(1), aCursor.getString(2),
								aCursor.getString(3), aCursor.getString(4),
								aCursor.getString(5), aCursor.getString(6),
								aCursor.getString(7), aCursor.getString(8));
					} while (aCursor.moveToNext());
				}

			}
			aCursor.close();

		}
		if (aDatabase.isOpen()) {
			aDatabase.close();
		}
		return aJobDetails;

	}

	public LatLng getLatLngbyJobId(String iJobId) {
		databaseWrapper = new DatabaseWrapper(mContext);
		aDatabase = databaseWrapper.getReadableDatabase();
		LatLng aLatLng = null;
		if (aDatabase != null) {
			Cursor aCursor = aDatabase.query(PostORM.TABLE_NAME, DETAIL_COLUMN,
					"job_id = " + iJobId.toString(), null, null, null, null);
			if (aCursor.getCount() > 0) {
				if (aCursor.moveToFirst()) {
					do {
						aLatLng = new LatLng(Double.parseDouble(aCursor
								.getString(aCursor
										.getColumnIndex(COLUMN_LATITUDE))),
								Double.parseDouble(aCursor.getString(aCursor
										.getColumnIndex(COLUMN_LONGITUDE))));

					} while (aCursor.moveToNext());
				}

			}
			aCursor.close();

		}
		if (aDatabase.isOpen()) {
			aDatabase.close();
		}
		return aLatLng;

	}

	public String getJobTitlebyJobId(String iJobId) {
		databaseWrapper = new DatabaseWrapper(mContext);
		aDatabase = databaseWrapper.getReadableDatabase();
		String aJobTitle = null;
		if (aDatabase != null) {
			Cursor aCursor = aDatabase.query(PostORM.TABLE_NAME, DETAIL_COLUMN,
					"job_id = " + iJobId.toString(), null, null, null, null);
			if (aCursor.getCount() > 0) {
				if (aCursor.moveToFirst()) {
					do {
						aJobTitle = aCursor.getString(aCursor
								.getColumnIndex(COLUMN_TITLE));

					} while (aCursor.moveToNext());
				}

			}
			aCursor.close();

		}
		if (aDatabase.isOpen()) {
			aDatabase.close();
		}
		return aJobTitle;

	}

	public static PostORM mPostORM;

	public static final PostORM getInstance(Context mContext) {
		// TODO Auto-generated constructor stub

		if (mPostORM == null) {
			mPostORM = new PostORM(mContext);

		}

		return mPostORM;
	}

}

package ir.hotelairport.androidapp.SQLiteDB;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import ir.hotelairport.androidapp.Constants;
import ir.hotelairport.androidapp.Models.HotelSetting;
import ir.hotelairport.androidapp.Models.Language;
import ir.hotelairport.androidapp.Models.Position;
import ir.hotelairport.androidapp.Models.Translation;
import ir.hotelairport.androidapp.R;

/**
 * Created by Mohammad on 10/15/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    SharedPreferences user_detail;
    Context context;
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "HOTELSYS";

    //  tables name
    private static final String LANGUAGE_KEYS_TABLE = "language_keys";
    private static final String SUPPORTED_LANGUAGES_TABLE = "supported_languages";
    private static final String TRANSLATIONS_TABLE = "translations";
    private static final String Position_TABLE = "position";
    private static final String SETTINGS_TABLE = "settings";

    // Tables Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_LOCALE = "locale";
    private static final String KEY_LANGUAGE_ID = "language_id";
    private static final String KEY_LANGUAGE_KEY_ID = "key_id";
    private static final String KEY_TRANSLATION = "translation";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TIME_STAMP = "created_at";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LNG = "lng";
    private static final String KEY_LOGO = "hotel_logo";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
        user_detail=context.getSharedPreferences(Constants.USER_DETAIL, Context.MODE_PRIVATE);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SUPPORTED_LANGUAGES_TABLE = "CREATE TABLE IF NOT EXISTS " + SUPPORTED_LANGUAGES_TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_LOCALE + " TEXT,"
                + KEY_TIME_STAMP + " TIMESTAMP DEFAULT (datetime('now','localtime')) " + ")";
        String CREATE_LANGUAGE_KEYS_TABLE = "CREATE TABLE IF NOT EXISTS " + LANGUAGE_KEYS_TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_TIME_STAMP + " TIMESTAMP DEFAULT (datetime('now','localtime')) " + ")";
        String CREATE_TRANSLATIONS_TABLE = "CREATE TABLE IF NOT EXISTS " + TRANSLATIONS_TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_LANGUAGE_KEY_ID + " INTEGER,"
                + KEY_LANGUAGE_ID + " INTEGER,"
                + KEY_TRANSLATION + " TEXT,"
                + KEY_TIME_STAMP + " TIMESTAMP DEFAULT (datetime('now','localtime')),"
                + "FOREIGN KEY(" + KEY_LANGUAGE_KEY_ID + ") REFERENCES " + LANGUAGE_KEYS_TABLE + "(" + KEY_ID + "),"
                + "FOREIGN KEY(" + KEY_LANGUAGE_ID + ") REFERENCES " + SUPPORTED_LANGUAGES_TABLE + "(" + KEY_ID + ")"
                + ")";
        String CREATE_POSITION_TABLE = "CREATE TABLE IF NOT EXISTS " + Position_TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_LAT + " DOUBLE,"
                + KEY_LNG + " DOUBLE,"
                + KEY_TIME_STAMP + " TIMESTAMP DEFAULT (datetime('now','localtime')) " + ")";
        String CREATE_SETTINGS_TABLE = "CREATE TABLE IF NOT EXISTS " + SETTINGS_TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_LANGUAGE_ID + " INTEGER,"
                + KEY_NAME + " TEXT,"
                + KEY_LOGO + " TEXT,"
                + KEY_TIME_STAMP + " TIMESTAMP DEFAULT (datetime('now','localtime')),"
                + "FOREIGN KEY(" + KEY_LANGUAGE_ID + ") REFERENCES " + SUPPORTED_LANGUAGES_TABLE + "(" + KEY_ID + ")"
                + ")";
        db.execSQL(CREATE_SUPPORTED_LANGUAGES_TABLE);
        db.execSQL(CREATE_LANGUAGE_KEYS_TABLE);
        db.execSQL(CREATE_TRANSLATIONS_TABLE);
        db.execSQL(CREATE_POSITION_TABLE);
        db.execSQL(CREATE_SETTINGS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TRANSLATIONS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + LANGUAGE_KEYS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SUPPORTED_LANGUAGES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Position_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SETTINGS_TABLE);
        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
    public void addToSupportedLanguages(Language language) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, language.getId());
        values.put(KEY_NAME, language.getName());
        values.put(KEY_LOCALE, language.getLocale());
        // Inserting Row
        db.insert(SUPPORTED_LANGUAGES_TABLE, null, values);
        db.close(); // Closing database connection
    }

    public ArrayList<Language> getSupportedLanguages() {
        ArrayList<Language> LanguageL = new ArrayList<Language>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + SUPPORTED_LANGUAGES_TABLE + " ORDER BY " + KEY_ID + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Language language = new Language();
                language.setId(Integer.parseInt(cursor.getString(0)));
                language.setName(cursor.getString(1));
                language.setLocale(cursor.getString(2));
                language.setCreated_at(cursor.getString(3));
                LanguageL.add(language);
            } while (cursor.moveToNext());
        }

        // return language list
        cursor.close();
        db.close();
        return LanguageL;
    }

    public void addToSettingsTable(HotelSetting setting)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LANGUAGE_ID, setting.getLanguage_id());
        values.put(KEY_NAME, setting.getHotel_name());
        values.put(KEY_LOGO, setting.getHotel_logo());
        // Inserting Row
        db.insert(SETTINGS_TABLE, null, values);
        db.close(); // Closing database connection
    }
    public HotelSetting getHOtelInfoForLang(int lang_id) {
        HotelSetting hotelSetting= new HotelSetting();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(SETTINGS_TABLE, new String[]{KEY_ID,
                        KEY_LANGUAGE_ID,KEY_NAME, KEY_LOGO}, KEY_LANGUAGE_ID + "=?",
                new String[]{String.valueOf(lang_id)}, null, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() != 0) {
                cursor.moveToFirst();
                hotelSetting.setLanguage_id(cursor.getInt(1));
                hotelSetting.setHotel_name(cursor.getString(2));
                hotelSetting.setHotel_logo(cursor.getString(3));
            }
            cursor.close();
        }
        db.close();
        return hotelSetting;

    }
    public void addToPositionTable(Position position)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LAT, position.getLat());
        values.put(KEY_LNG, position.getLng());
        // Inserting Row
        db.insert(Position_TABLE, null, values);
        db.close(); // Closing database connection
    }
    public Position getHotelPosition()
    {
        ArrayList<Position> positionArrayList = new ArrayList<Position>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + Position_TABLE + " ORDER BY " + KEY_ID + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Position position = new Position();
                position.setLat(cursor.getDouble(1));
                position.setLng(cursor.getDouble(2));
                positionArrayList.add(position);
            } while (cursor.moveToNext());
        }

        // return language list
        cursor.close();
        db.close();
        return positionArrayList.get(0);
    }
    public void addToLanguageKeys(int id, String title) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_TITLE, title);
        // Inserting Row
        db.insert(LANGUAGE_KEYS_TABLE, null, values);
        db.close(); // Closing database connection
    }

    public String getTranslationForLanguage(int lang_id, String key) {
        if (user_detail.getInt(Constants.APP_SERVER_INIT, 0) == 0) {
         return   getFromLocalStringResource(key);
        } else {
            int language_key_id;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(LANGUAGE_KEYS_TABLE, new String[]{KEY_ID,
                            KEY_TITLE}, KEY_TITLE + "=?",
                    new String[]{String.valueOf(key)}, null, null, null, null);
            if (cursor != null) {
                if (cursor.getCount() != 0) {
                    cursor.moveToFirst();
                    language_key_id = cursor.getInt(0);
                    Log.i("lang_key_id", String.valueOf(language_key_id));
                    Cursor cursor2 = db.query(TRANSLATIONS_TABLE, new String[]{KEY_ID,
                                    KEY_LANGUAGE_KEY_ID, KEY_LANGUAGE_ID, KEY_TRANSLATION}, KEY_LANGUAGE_KEY_ID + "=? and " + KEY_LANGUAGE_ID + "=? ",
                            new String[]{String.valueOf(language_key_id), String.valueOf(lang_id)}, null, null, null, null);
                    if (cursor2 != null) {
                        if (cursor2.getCount() != 0) {
                            cursor2.moveToFirst();
                            if (cursor2.getString(3) != null) {
                                String translation=cursor2.getString(3);
                                db.close();
                                cursor2.close();
                                cursor.close();
                                return translation;
                            } else {
                                db.close();
                                cursor2.close();
                                cursor.close();
                                if (lang_id!=2)
                                    return getTranslationForLanguage(2, key);
                                else
                                    return getFromLocalStringResource(key);
                            }
                        } else {
                            db.close();
                            cursor2.close();
                            cursor.close();
                            if (lang_id!=2)
                                return getTranslationForLanguage(2, key);
                            else
                                return getFromLocalStringResource(key);
                        }

                    } else {
                        db.close();
                        cursor.close();
                        if (lang_id!=2)
                            return getTranslationForLanguage(2, key);
                        else
                            return getFromLocalStringResource(key);
                    }

                } else {
                    db.close();
                    cursor.close();
                    return getFromLocalStringResource(key);
                }
            } else {
                return getFromLocalStringResource(key);
            }

        }

    }

    private String getFromLocalStringResource(String key)
    {
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(key, "string", packageName);
        return context.getResources().getString(resId);
    }
    public void addNewTranslation(Translation translation) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, translation.getId());
        values.put(KEY_LANGUAGE_KEY_ID, translation.getKey_id());
        values.put(KEY_LANGUAGE_ID, translation.getLanguage_id());
        values.put(KEY_TRANSLATION, translation.getTranslation());
        // Inserting Row
        db.insert(TRANSLATIONS_TABLE, null, values);
        db.close(); // Closing database connection

    }
}
package ic2.utils;

import ic2.adapters.MainListAdapter;
import ic2.items.CL;

import java.io.File;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;
import android.widget.ListView;

public class CONS {

	public static class Prefs {
		////////////////////////////////

		// Commons

		////////////////////////////////
		public static long dflt_LongExtra_value = -1;
		
		public static int dflt_IntExtra_value = -1;
		
		////////////////////////////////

		// MainActv

		////////////////////////////////
		public static String pname_IC = "pref_ic";
		
		public static String pkey_GenreId = "genre_id";
		
		public static int pkey_GenreId_IntValue = -1;
		
		public static String pkey_LastVisiblePosition_MainActv
							= "pkey_LastVisiblePosition_MainActv";
		
	}
	
	public static class DB {

		public static class AdminLog {
			
			public static String dName_ExternalStorage =
							"/mnt/sdcard-ext";
			
			public static String folName_Data =
					"IC_DATA";
			
			public static String folName_Logs =
					"IC_LOGS";
			
			public static String fname_Log_Trunk = "log";
			
			public static String ext_Log = ".log";
			
			public static String fname_Log_Full = "log.log";
			
			
			public static long logFile_Limit = 6000;
			

			
		}//static class Logs
		
//		public static String dbName = "ic.db";
////		public static String dbName = "ic";
//		
		public static String dbFileExt = ".db";
//		
//		public static String dirName_ExternalStorage = "/mnt/sdcard-ext";
//
////		public static String dirPath_db = "/data/data/shoppinglist.main/databases";
//		public static String dirPath_db = "/data/data/ic.main/databases";
//		
//		public static String fileName_db_backup_trunk = "ic_backup";
//
//		public static String fileName_db_backup_ext = ".bk";
//
//		public static String dirPath_db_backup = 
//						dirName_ExternalStorage + "/IC_backup";

		/*********************************
		 * Columns
		 *********************************/
		
		
//		public static String[] cols_check_lists_FULL
//		= {
//			android.provider.BaseColumns._ID,
//			"created_at", "modified_at",
//			"name",	"genre_id", "yomi"};
		
		public static enum tableNames {
			
			items,
			
		}
		
		////////////////////////////////

		// Paths and names

		////////////////////////////////
		// Backup
		public static String dirPath_db = "/data/data/ic2.main/databases";
		
		public static String dirName_ExternalStorage = "/mnt/sdcard-ext";
		
		public static String dirPath_db_backup = 
								dirName_ExternalStorage + "/ic2_backup";
		
		public static String fileName_db_backup_trunk = "ic2_backup";
		
		public static String fileName_db_backup_ext = ".bk";

		public static String dbName = "ic2.db";
////		public static String dbName = "ic2";
//		
//		public static String dbFileExt = ".db";
//		
//		public static String dirName_ExternalStorage = "/mnt/sdcard-ext";
//
////		public static String dirPath_db = "/data/data/shoppinglist.main/databases";
//		public static String dirPath_db = "/data/data/ic2.main/databases";
//		
//		public static String fileName_db_backup_trunk = "ic2_backup";
//
//		public static String fileName_db_backup_ext = ".bk";
//
//		public static String dirPath_db_backup = 
//						dirName_ExternalStorage + "/ic2_backup";

////		public static String dbName = "ic2.db";
//		
//		public static String dPath_dbFile;
////		public static String dPath_dbFile = "/data/data/ic2.main/databases";
//		
//		public static String dPath_dbFile_backup = "/mnt/sdcard-ext/ic2_backup";
//		
////		public static String dPath_dbFile = 
////							Methods.get_DirPath(new MainActv().getFilesDir().getPath());
//		
//		public static String fname_DB_Backup_Trunk = "ic2_backup";
//		
//		public static String fname_DB_Backup_ext = ".bk";
		
		////////////////////////////////
		
		// Table: check_lists
		
		////////////////////////////////
		public static final String tname_Check_Lists = "check_lists";
		
		public static String[] cols_check_lists = {
			
					"name",	"genre_id", "yomi"	// 0,1,2
					
		};
		
//		public static String[] cols_check_lists =			{"name",	"genre_id"};
		public static String[] cols_check_lists_FULL = {
			
				android.provider.BaseColumns._ID,	// 0
				"created_at", "modified_at",		// 1,2
				"name",	"genre_id", "yomi"			// 3,4,5
		};
		
		public static String[] col_types_Check_Lists = 	{
					
				"TEXT", 	"INTEGER"
			
		};
		
		////////////////////////////////

		// Table: items

		////////////////////////////////
		public static String tname_items = "items";
		
//		public static String[] cols_items =			{"text", "serial_num",	"list_id"};
		
		// Array  0		1				2		3
		// Total  3		4				5		6
		public static String[] col_name_Items = {
			
					"text", "serial_num",			// 0,1
					"list_id", "status"				// 2,3
					
		};
		
		public static String[] col_types_Items = {
			
					"TEXT", "INTEGER",		// 0,1
					"INTEGER", "INTEGER"	// 2,3
			
		};
		
		// Array  0		1				2		3
		// Total  3		4				5		6
		public static String[] col_name_Items_full = {
					android.provider.BaseColumns._ID,	// 0
					"created_at", "modified_at",		// 1, 2

					"text", "serial_num",				// 3,4
					"list_id", "status"					// 5,6
		};
		
		public static String[] col_types_Items_full = {
			
					"INTEGER", 				// 0
					"INTEGER", "INTEGER",	// 1,2
					
					"TEXT", "INTEGER",		// 3,4
					"INTEGER", "INTEGER"	// 5,6
					
		};
		

		////////////////////////////////

		// Table: genres

		////////////////////////////////
		public static String tableName_genres = "genres";
		
		public static String[] cols_genres =		{"name"};
		
		public static String[] col_types_genres = 	{"TEXT"};

		
		/*********************************
		 * SQLite
		 *********************************/
		public static enum SQLiteDataTypes {
			
			TEXT,
			
		}
		
		/*********************************
		 * Others
		 *********************************/
		public static final int GetYomi_ChunkNum	= 5;
		
	}//public static class DBAdmin

	public static class RetVal {
		/*********************************
		 * Successful
		 *********************************/
		public static final int DB_BACKUP_SUCCESSFUL	= 10;
		
		public static final int DB_UPDATE_SUCCESSFUL	= 11;
		
		
		/*********************************
		 * Errors: General
		 *********************************/
		public static final int EXCEPTION_IO			= -50;
		public static final int EXCEPTION_FileNotFound	= -51;
		
		/*********************************
		 * Errors: DB
		 *********************************/
		public static final int DB_DOESNT_EXIST	= -10;
		
		public static final int DB_FILE_COPY_EXCEPTION	= -11;
		
		public static final int DB_CANT_CREATE_FOLDER	= -12;
		
		public static final int GETYOMI_NO_ENTRY		= -13;
		
		public static final int EXCEPTION_SQL			= -14;
		
		public static final int GetWordList_Failed		= -15;
		
		/*********************************
		 * Errors: FTP
		 *********************************/
		public static final int FTP_Exception_Socket	= -40;
		
		public static final int FTP_LOGIN_FAILED		= -41;
		
		/*********************************
		 * Others: > 0, <= -90
		 *********************************/
		public static final int OK				= 1;
		
		public static final int NOP				= -90;
		
		public static final int FAILED			= -91;
		
		public static final int MAGINITUDE_ONE	= 1000;
		
	}//public static class RetVal
	
	public static class Admin {
		
		public static SortTypes sortType;
		
		public static enum SortTypes {
			SortBy_Yomi,
			SortBy_CreatedAt,
			
		}
		
		public static final int vib_Long	= 100;

		public static final String appName	= "ic";
		
		public static final String separatorColon	= ":";
		
		public static enum IntentKeys {
			
			LogFilePath,
			
		}
		
		public static class Miscs {
			
			public static final int DialogTitleLength = 10;
			
		}
		
		public static int dflt_ChildCount = 4;
//		public static final int dflt_ChildCount = 4;
		
	}//public static class Admin
	
	public static class FTPData {
		
		public static final String serverName
						= "ftp.benfranklin.chips.jp";
		
		public static final String userName
						= "chips.jp-benfranklin";
		
		public static final String passWord
						= "9x9jh4";
		
		public static final String dpath_Remote_Db
						= "android_app_data/IC/db";
		
		class RetVal {
			
			
		}
	}
	
	public static class MainActv {
		
		public static ListView lvMain;
		
		public static List<CL> CLList;
		
		public static MainListAdapter mlAdp;
		
	}

	
}//public class CONS

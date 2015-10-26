package com.cmcm.miuinetworkpemissiongetter;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Utility class for changing APN state
 */
public class APNUtils {
	private static final String SUFFIX_APN = "_cmcm"; // type 1

	private static final String MMS = "mms";

	private static final String COLUMN_ID = BaseColumns._ID;
	private static final String COLUMN_APN = "apn";
	private static final String COLUMN_TYPE = "type";
	// private static final String COLUMN_APN_ID = "apn_id"; // for preferred
	// APN

	private static final String[] PROJECTION = new String[] { COLUMN_ID, COLUMN_APN, COLUMN_TYPE };
	private static final Uri CURRENT_APNS = Uri.parse("content://telephony/carriers/current");

	// private static final Uri PREFERRED_APN =
	// Uri.parse("content://telephony/carriers/preferapn");

	/**
	 * Gets the state of APN
	 * 
	 * @param context
	 * @return true if enabled
	 */
	public static boolean getApnState(Context context) {

		ContentResolver resolver = context.getContentResolver();
		int counter = 0;
		Cursor cursor = null;
		try {
			cursor = resolver.query(CURRENT_APNS, PROJECTION, null, null, null);
			int typeIndex = cursor.getColumnIndex(COLUMN_TYPE);
			cursor.moveToNext();
			while (!cursor.isAfterLast()) {
				String type = cursor.getString(typeIndex);

				if (isDisabled(type)) {
					return false; // no need to continue
				}

				if (!isMms(type) || /* this is MMM and we */shouldDisableMms()) {
					counter++;
				}

				cursor.moveToNext();
			}
		}catch(Throwable e){
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return counter != 0;
	}

	public static int getAPNCount(Context context) {
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = null;
		try {
			cursor = resolver.query(CURRENT_APNS, PROJECTION, null, null, null);
			return cursor.getCount();
		} finally {
			if (cursor != null)
				cursor.close();
		}
	}

	private static boolean isMms(String type) {
		return type != null && type.toLowerCase().endsWith(MMS);
	}

	private static boolean shouldDisableMms() {
		return false;
	}

	private static boolean isDisabled(String value) {
		return value != null && value.endsWith(SUFFIX_APN);
	}

	/**
	 * Set APN State
	 * 
	 * @param context
	 * @param enabled
	 */
	public static void setApnState(Context context, boolean enabled) {
		boolean shouldDisableMms = shouldDisableMms();

		ContentResolver resolver = context.getContentResolver();
		ContentValues values = new ContentValues();
		Cursor cursor = null;

		String[] args = new String[1];
		try {
			// COLUMN_ID{0}, COLUMN_APN{1}, COLUMN_TYPE{2}
			cursor = resolver.query(CURRENT_APNS, PROJECTION, null, null, null);
			int idIndex = cursor.getColumnIndex(COLUMN_ID);
			int apnIndex = cursor.getColumnIndex(COLUMN_APN);
			int typeIndex = cursor.getColumnIndex(COLUMN_TYPE);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {

				String typeValue = cursor.getString(typeIndex);
				if (!enabled // we should disable
						&& isMms(typeValue) // and this is MMS
						&& !shouldDisableMms) { // but users disallow us to
												// disable MMS
					// ignore
					cursor.moveToNext();
					continue;
				}

				args[0] = String.valueOf(cursor.getInt(idIndex)); // id
				values.put(COLUMN_APN, getAdaptedValue(cursor.getString(apnIndex), enabled));
				values.put(COLUMN_TYPE, getAdaptedValue(typeValue, enabled));

				resolver.update(CURRENT_APNS, values, COLUMN_ID + "=?", args);

				// move to next
				cursor.moveToNext();
			}

		} catch (Exception e) {
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	private static String getAdaptedValue(String value, boolean enable) {

		final String modifier = SUFFIX_APN;

		// handle null-value
		if (value == null)
			return enable ? value : modifier;

		// remove any modifier so that value becomes enabled in any case
		value = removeModifiers(value);

		if (!enable) { // add required modifier
			value = addModifier(value);
		}

		return value;
	}

	private static String removeModifiers(String value) {
		if (value.endsWith(SUFFIX_APN))
			return value.substring(0, value.length() - SUFFIX_APN.length());
		else
			return value;
	}

	private static String addModifier(String value) {
		return value + SUFFIX_APN;
	}
}

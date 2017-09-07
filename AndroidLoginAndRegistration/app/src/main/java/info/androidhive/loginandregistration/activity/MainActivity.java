package info.androidhive.loginandregistration.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.ActivityCompat;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;


import info.androidhive.loginandregistration.BuildConfig;
import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.helper.SQLiteHandler;
import info.androidhive.loginandregistration.helper.SessionManager;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;

import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {

	private TextView txtName;
	private TextView txtEmail;
	protected Location mLastLocation;
	private TextView txtLatitude;
	private TextView txtLongitude;

	private Button btnLogout;

	private SQLiteHandler db;
	private SessionManager session;


	private static final String TAG = MainActivity.class.getSimpleName();

	private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

	/**
	 * Provides the entry point to the Fused Location Provider API.
	 */
	private FusedLocationProviderClient mFusedLocationClient;


	String latitude="null";
	String longitude = "null";

	//맨처음에 로딩할때 딱 한번 보여주기만 하면됨

	@Override
	protected void onCreate(Bundle savedInstanceState) {



		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		txtName = (TextView) findViewById(R.id.name);
		txtEmail = (TextView) findViewById(R.id.email);
		txtLatitude = (TextView) findViewById(R.id.latitude_location);
		txtLongitude = (TextView) findViewById(R.id.longitude_location);


		btnLogout = (Button) findViewById(R.id.btnLogout);

		// SqLite database handler
		db = new SQLiteHandler(getApplicationContext());

		// session manager
		session = new SessionManager(getApplicationContext());

		if (!session.isLoggedIn()) {
			logoutUser();
		}

		// Fetching user details from SQLite
		HashMap<String, String> user = db.getUserDetails();

		String name = user.get("name");
		String email = user.get("email");

		// Displaying the user details on the screen
		txtName.setText(name);
		txtEmail.setText(email);



		//Get the GPS Info and return to the user
		mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
		txtLatitude.setText("not_detected");
		txtLongitude.setText("not_detected");

		// Logout button click event
		btnLogout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				logoutUser();
			}
		});
	}

	@Override
	public void onStart(){
		super.onStart();
		Log.e(TAG,"check1");

		if(!checkPermissions()){
			requestPermissions();
		} else{
			String[] location = getLastLocation();
			sendLocation(location);
		}
	}



	/**
	 *  send location to the mysql server
	 *
	 *
	 */

	private void sendLocation(String[] loc){


	}




	/**
	 * Provides a simple way of getting a device's location and is well suited for
	 * applications that do not require a fine-grained location and that do not need location
	 * updates. Gets the best and most recent location currently available, which may be null
	 * in rare cases when a location is not available.
	 * <p>
	 * Note: this method should be called after location permission has been granted.
	 */
	@SuppressWarnings("MissingPermission")
	private String[] getLastLocation() {

		final String[] position = new String[2];
		mFusedLocationClient.getLastLocation()
				.addOnCompleteListener(this, new OnCompleteListener<Location>() {
					@Override
					public void onComplete(@NonNull Task<Location> task) {
						if (task.isSuccessful() && task.getResult() != null) {
							mLastLocation = task.getResult();

							position[0] = String.valueOf(mLastLocation.getLatitude());
							position[1] = String.valueOf(mLastLocation.getAltitude());
							txtLatitude.setText(position[0]);
							txtLongitude.setText(position[1]);


						} else {
							Log.w(TAG, "getLastLocation:exception", task.getException());
						}
					}
				});

		return position;
	}


	/**
	 * Shows a {@link Snackbar}.
	 *
	 * @param mainTextStringId The id for the string resource for the Snackbar text.
	 * @param actionStringId   The text of the action item.
	 * @param listener         The listener associated with the Snackbar action.
	 */
	private void showSnackbar(final int mainTextStringId, final int actionStringId,
							  View.OnClickListener listener) {
		Snackbar.make(findViewById(android.R.id.content),
				getString(mainTextStringId),
				Snackbar.LENGTH_INDEFINITE)
				.setAction(getString(actionStringId), listener).show();
	}

	/**
	 * Return the current state of the permissions needed.
	 */
	private boolean checkPermissions() {
		int permissionState = ActivityCompat.checkSelfPermission(this,
				Manifest.permission.ACCESS_COARSE_LOCATION);
		return permissionState == PackageManager.PERMISSION_GRANTED;
	}

	private void startLocationPermissionRequest() {
		ActivityCompat.requestPermissions(MainActivity.this,
				new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
				REQUEST_PERMISSIONS_REQUEST_CODE);
	}

	private void requestPermissions() {
		boolean shouldProvideRationale =
				ActivityCompat.shouldShowRequestPermissionRationale(this,
						Manifest.permission.ACCESS_COARSE_LOCATION);

		// Provide an additional rationale to the user. This would happen if the user denied the
		// request previously, but didn't check the "Don't ask again" checkbox.
		if (shouldProvideRationale) {
			Log.i(TAG, "Displaying permission rationale to provide additional context.");

			showSnackbar(R.string.permission_relationale, android.R.string.ok,
					new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							// Request permission
							startLocationPermissionRequest();
						}
					});

		} else {
			Log.i(TAG, "Requesting permission");
			// Request permission. It's possible this can be auto answered if device policy
			// sets the permission in a given state or the user denied the permission
			// previously and checked "Never ask again".
			startLocationPermissionRequest();
		}
	}

	/**
	 * Callback received when a permissions request has been completed.
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
										   @NonNull int[] grantResults) {
		Log.i(TAG, "onRequestPermissionResult");
		if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
			if (grantResults.length <= 0) {
				// If user interaction was interrupted, the permission request is cancelled and you
				// receive empty arrays.
				Log.i(TAG, "User interaction was cancelled.");
			} else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				// Permission granted.
				getLastLocation();
			} else {
				// Permission denied.

				// Notify the user via a SnackBar that they have rejected a core permission for the
				// app, which makes the Activity useless. In a real app, core permissions would
				// typically be best requested during a welcome-screen flow.

				// Additionally, it is important to remember that a permission might have been
				// rejected without asking the user for permission (device policy or "Never ask
				// again" prompts). Therefore, a user interface affordance is typically implemented
				// when permissions are denied. Otherwise, your app could appear unresponsive to
				// touches or interactions which have required permissions.
				showSnackbar(R.string.permission_denied_explanation, R.string.settings,
						new View.OnClickListener() {
							@Override
							public void onClick(View view) {
								// Build intent that displays the App settings screen.
								Intent intent = new Intent();
								intent.setAction(
										Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
								Uri uri = Uri.fromParts("package",
										BuildConfig.APPLICATION_ID, null);
								intent.setData(uri);
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent);
							}
						});
			}
		}
	}



	/**
	 * Logging out the user. Will set isLoggedIn flag to false in shared
	 * preferences Clears the user data from sqlite users table
	 * */
	private void logoutUser() {
		session.setLogin(false);

		db.deleteUsers();

		// Launching the login activity
		Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
	}



}

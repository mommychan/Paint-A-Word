package com.jeffreychan.paintaword;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener, OnItemSelectedListener {

	ImageButton wordbtn, undobtn, clearbtn, eraserbtn;
	Spinner widthMenu;
	TextView textView1;
	RelativeLayout rel1;
	private static int counter = 0;
	ArrayList<String[]> RowData = new ArrayList<>();
	Random ran = new Random();
	private Drawing customCanvas;
	Handler handler;
	boolean ready = true;
	ImageButton blackbtn, graybtn, redbtn, orangebtn, yellowbtn, greenbtn, bluebtn, purplebtn;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);

		customCanvas = (Drawing) findViewById(R.id.signature_canvas);
		customCanvas.setBackgroundColor(Color.WHITE);


		wordbtn = (ImageButton) findViewById(R.id.wordbtn);
		undobtn = (ImageButton) findViewById(R.id.undobtn);
		clearbtn = (ImageButton) findViewById(R.id.clearbtn);
		eraserbtn = (ImageButton) findViewById(R.id.eraserbtn);

		wordbtn.setBackgroundResource(android.R.drawable.btn_default);
		undobtn.setBackgroundResource(android.R.drawable.btn_default);
		clearbtn.setBackgroundResource(android.R.drawable.btn_default);
		eraserbtn.setBackgroundResource(android.R.drawable.btn_default);

		textView1 = (TextView) findViewById(R.id.textView1);
		rel1 = (RelativeLayout) findViewById(R.id.RelativeLayout1);
		handler = new Handler();


		blackbtn = (ImageButton) findViewById(R.id.blackbtn);
		graybtn = (ImageButton) findViewById(R.id.graybtn);
		redbtn = (ImageButton) findViewById(R.id.redbtn);
		orangebtn = (ImageButton) findViewById(R.id.orangebtn);
		yellowbtn = (ImageButton) findViewById(R.id.yellowbtn);
		greenbtn = (ImageButton) findViewById(R.id.greenbtn);
		bluebtn = (ImageButton) findViewById(R.id.bluebtn);
		purplebtn = (ImageButton) findViewById(R.id.purplebtn);

		blackbtn.setBackgroundColor(Color.YELLOW);
		graybtn.setBackgroundColor(Color.GRAY);
		redbtn.setBackgroundColor(Color.GRAY);
		orangebtn.setBackgroundColor(Color.GRAY);
		yellowbtn.setBackgroundColor(Color.GRAY);
		greenbtn.setBackgroundColor(Color.GRAY);
		bluebtn.setBackgroundColor(Color.GRAY);
		purplebtn.setBackgroundColor(Color.GRAY);

		blackbtn.setImageResource(R.drawable.black);
		graybtn.setImageResource(R.drawable.gray);
		redbtn.setImageResource(R.drawable.red);
		orangebtn.setImageResource(R.drawable.orange);
		yellowbtn.setImageResource(R.drawable.yellow);
		greenbtn.setImageResource(R.drawable.green);
		bluebtn.setImageResource(R.drawable.blue);
		purplebtn.setImageResource(R.drawable.purple);


		wordbtn.setImageResource(R.drawable.word);
		undobtn.setImageResource(R.drawable.undo);
		clearbtn.setImageResource(R.drawable.clear);
		eraserbtn.setImageResource(R.drawable.eraser);
		wordbtn.setOnClickListener(this);
		undobtn.setOnClickListener(this);
		clearbtn.setOnClickListener(this);
		eraserbtn.setOnClickListener(this);

		blackbtn.setOnClickListener(this);
		graybtn.setOnClickListener(this);
		redbtn.setOnClickListener(this);
		orangebtn.setOnClickListener(this);
		yellowbtn.setOnClickListener(this);
		greenbtn.setOnClickListener(this);
		bluebtn.setOnClickListener(this);
		purplebtn.setOnClickListener(this);

		widthMenu = (Spinner) findViewById(R.id.widthMenu);

		SimpleImageArrayAdapter adapter = new SimpleImageArrayAdapter(this,
				new Integer[]{R.drawable.width1, R.drawable.width2, R.drawable.width4, R.drawable.width8, R.drawable.width16, R.drawable.width32, R.drawable.width64, R.drawable.width128, R.drawable.width256});
		widthMenu.setAdapter(adapter);
		widthMenu.setOnItemSelectedListener(this);
		widthMenu.setSelection(2);


		try {
			AssetManager am = getAssets();
			InputStream is = am.open("wordslist.csv");
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));

			String line;

			while ((line = reader.readLine()) != null) {
				String[] row = line.split(",");
				RowData.add(row);
				counter += 1;
			}

			is.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		if (id == R.id.save_image) {
			AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
			saveDialog.setTitle("Save drawing");
			saveDialog.setMessage("Save drawing to device Gallery?");
			saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {

					customCanvas.setDrawingCacheEnabled(true);
					String imgSaved = null;
					try {
						imgSaved = MediaStore.Images.Media.insertImage(
								getContentResolver(), customCanvas.getDrawingCache(),
								UUID.randomUUID().toString() + ".png", "drawing");
					} catch (Exception e) {
						// Error saving image
					} finally {
						if (imgSaved != null) {
							Toast savedToast = Toast.makeText(getApplicationContext(),
									"Drawing saved to Gallery!", Toast.LENGTH_SHORT);
							savedToast.show();
						} else {
							Toast unsavedToast = Toast.makeText(getApplicationContext(),
									"Oops! Image could not be saved.", Toast.LENGTH_SHORT);
							unsavedToast.show();
						}

						customCanvas.destroyDrawingCache();
					}
				}
			});
			saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			saveDialog.show();
			return true;
		} else if (id == R.id.playstore) {
			Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://play.google.com/store/apps/details?id=com.jeffreychan.paintaword&hl=en"));
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.wordbtn) {
			customCanvas.clearCanvas();
			int num = ran.nextInt(counter);
			String[] test = RowData.get(num);
			textView1.setText(test[0]);
			new Thread(new Task()).start();


		} else if (v.getId() == R.id.clearbtn) {
			customCanvas.clearCanvas();
			textView1.setText("");
		} else if (v.getId() == R.id.undobtn) {
			customCanvas.undoCanvas();
		}

		if (v.getId() == R.id.blackbtn) {
			customCanvas.setColor("black");
			blackbtn.setBackgroundColor(Color.YELLOW);
			graybtn.setBackgroundColor(Color.GRAY);
			redbtn.setBackgroundColor(Color.GRAY);
			orangebtn.setBackgroundColor(Color.GRAY);
			yellowbtn.setBackgroundColor(Color.GRAY);
			greenbtn.setBackgroundColor(Color.GRAY);
			bluebtn.setBackgroundColor(Color.GRAY);
			purplebtn.setBackgroundColor(Color.GRAY);
			eraserbtn.setBackgroundResource(android.R.drawable.btn_default);

		} else if (v.getId() == R.id.redbtn) {
			customCanvas.setColor("red");
			blackbtn.setBackgroundColor(Color.GRAY);
			graybtn.setBackgroundColor(Color.GRAY);
			redbtn.setBackgroundColor(Color.YELLOW);
			orangebtn.setBackgroundColor(Color.GRAY);
			yellowbtn.setBackgroundColor(Color.GRAY);
			greenbtn.setBackgroundColor(Color.GRAY);
			bluebtn.setBackgroundColor(Color.GRAY);
			purplebtn.setBackgroundColor(Color.GRAY);
			eraserbtn.setBackgroundResource(android.R.drawable.btn_default);
		} else if (v.getId() == R.id.greenbtn) {
			customCanvas.setColor("green");
			blackbtn.setBackgroundColor(Color.GRAY);
			graybtn.setBackgroundColor(Color.GRAY);
			redbtn.setBackgroundColor(Color.GRAY);
			orangebtn.setBackgroundColor(Color.GRAY);
			yellowbtn.setBackgroundColor(Color.GRAY);
			greenbtn.setBackgroundColor(Color.YELLOW);
			bluebtn.setBackgroundColor(Color.GRAY);
			purplebtn.setBackgroundColor(Color.GRAY);
			eraserbtn.setBackgroundResource(android.R.drawable.btn_default);
		} else if (v.getId() == R.id.bluebtn) {
			customCanvas.setColor("blue");
			blackbtn.setBackgroundColor(Color.GRAY);
			graybtn.setBackgroundColor(Color.GRAY);
			redbtn.setBackgroundColor(Color.GRAY);
			orangebtn.setBackgroundColor(Color.GRAY);
			yellowbtn.setBackgroundColor(Color.GRAY);
			greenbtn.setBackgroundColor(Color.GRAY);
			bluebtn.setBackgroundColor(Color.YELLOW);
			purplebtn.setBackgroundColor(Color.GRAY);
			eraserbtn.setBackgroundResource(android.R.drawable.btn_default);
		} else if (v.getId() == R.id.orangebtn) {
			customCanvas.setColor("orange");
			blackbtn.setBackgroundColor(Color.GRAY);
			graybtn.setBackgroundColor(Color.GRAY);
			redbtn.setBackgroundColor(Color.GRAY);
			orangebtn.setBackgroundColor(Color.YELLOW);
			yellowbtn.setBackgroundColor(Color.GRAY);
			greenbtn.setBackgroundColor(Color.GRAY);
			bluebtn.setBackgroundColor(Color.GRAY);
			purplebtn.setBackgroundColor(Color.GRAY);
			eraserbtn.setBackgroundResource(android.R.drawable.btn_default);
		} else if (v.getId() == R.id.purplebtn) {
			customCanvas.setColor("purple");
			blackbtn.setBackgroundColor(Color.GRAY);
			graybtn.setBackgroundColor(Color.GRAY);
			redbtn.setBackgroundColor(Color.GRAY);
			orangebtn.setBackgroundColor(Color.GRAY);
			yellowbtn.setBackgroundColor(Color.GRAY);
			greenbtn.setBackgroundColor(Color.GRAY);
			bluebtn.setBackgroundColor(Color.GRAY);
			purplebtn.setBackgroundColor(Color.YELLOW);
			eraserbtn.setBackgroundResource(android.R.drawable.btn_default);
		} else if (v.getId() == R.id.yellowbtn) {
			customCanvas.setColor("yellow");
			blackbtn.setBackgroundColor(Color.GRAY);
			graybtn.setBackgroundColor(Color.GRAY);
			redbtn.setBackgroundColor(Color.GRAY);
			orangebtn.setBackgroundColor(Color.GRAY);
			yellowbtn.setBackgroundColor(Color.YELLOW);
			greenbtn.setBackgroundColor(Color.GRAY);
			bluebtn.setBackgroundColor(Color.GRAY);
			purplebtn.setBackgroundColor(Color.GRAY);
			eraserbtn.setBackgroundResource(android.R.drawable.btn_default);
		} else if (v.getId() == R.id.graybtn) {
			customCanvas.setColor("gray");
			blackbtn.setBackgroundColor(Color.GRAY);
			graybtn.setBackgroundColor(Color.YELLOW);
			redbtn.setBackgroundColor(Color.GRAY);
			orangebtn.setBackgroundColor(Color.GRAY);
			yellowbtn.setBackgroundColor(Color.GRAY);
			greenbtn.setBackgroundColor(Color.GRAY);
			bluebtn.setBackgroundColor(Color.GRAY);
			purplebtn.setBackgroundColor(Color.GRAY);
			eraserbtn.setBackgroundResource(android.R.drawable.btn_default);
		} else if (v.getId() == R.id.eraserbtn) {
			customCanvas.setColor("white");
			blackbtn.setBackgroundColor(Color.GRAY);
			graybtn.setBackgroundColor(Color.GRAY);
			redbtn.setBackgroundColor(Color.GRAY);
			orangebtn.setBackgroundColor(Color.GRAY);
			yellowbtn.setBackgroundColor(Color.GRAY);
			greenbtn.setBackgroundColor(Color.GRAY);
			bluebtn.setBackgroundColor(Color.GRAY);
			purplebtn.setBackgroundColor(Color.GRAY);
			eraserbtn.setBackgroundColor(Color.YELLOW);

		}
	}

	public class Task implements Runnable {
		@Override
		public void run() {
			if (ready) {
				ready = false;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				handler.post(new Runnable() {
					@Override
					public void run() {
						textView1.setText("");
						ready = true;
					}
				});
			}
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void onItemSelected(AdapterView parent, View view, int position, long id) {

		Integer iPos = 1 << position;
		customCanvas.setWidth(iPos.toString());


	}

	@SuppressWarnings("rawtypes")
	@Override
	public void onNothingSelected(AdapterView parent) {
		// Do nothing
	}


}

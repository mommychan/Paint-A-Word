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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener, OnItemSelectedListener {

	ImageView wordbtn, undobtn, clearbtn, eraserbtn;
	Spinner widthMenu;
	TextView word;
	private static int counter = 0;
	ArrayList<String[]> RowData = new ArrayList<>();
	Random ran = new Random();
	private Drawing customCanvas;
	Handler handler;
	boolean ready = true;
	ImageView blackbtn, graybtn, redbtn, orangebtn, yellowbtn, greenbtn, bluebtn, purplebtn;
	ImageView current;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);

		customCanvas = (Drawing) findViewById(R.id.signature_canvas);
		customCanvas.setBackgroundColor(Color.WHITE);

		wordbtn = (ImageView) findViewById(R.id.wordbtn);
		undobtn = (ImageView) findViewById(R.id.undobtn);
		clearbtn = (ImageView) findViewById(R.id.clearbtn);
		eraserbtn = (ImageView) findViewById(R.id.eraserbtn);
		current = (ImageView) findViewById(R.id.current);
		word = (TextView) findViewById(R.id.word);

		handler = new Handler();

		blackbtn = (ImageView) findViewById(R.id.blackbtn);
		graybtn = (ImageView) findViewById(R.id.graybtn);
		redbtn = (ImageView) findViewById(R.id.redbtn);
		orangebtn = (ImageView) findViewById(R.id.orangebtn);
		yellowbtn = (ImageView) findViewById(R.id.yellowbtn);
		greenbtn = (ImageView) findViewById(R.id.greenbtn);
		bluebtn = (ImageView) findViewById(R.id.bluebtn);
		purplebtn = (ImageView) findViewById(R.id.purplebtn);

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
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

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
			word.setText(test[0]);
			new Thread(new Task()).start();
		} else if (v.getId() == R.id.clearbtn) {
			customCanvas.clearCanvas();
			word.setText("");
		} else if (v.getId() == R.id.undobtn) {
			customCanvas.undoCanvas();
		} else if (v.getId() == R.id.blackbtn) {
			customCanvas.setColor("black");
			current.setBackgroundResource(R.drawable.black);
		} else if (v.getId() == R.id.redbtn) {
			customCanvas.setColor("red");
			current.setBackgroundResource(R.drawable.red);
		} else if (v.getId() == R.id.greenbtn) {
			customCanvas.setColor("green");
			current.setBackgroundResource(R.drawable.green);
		} else if (v.getId() == R.id.bluebtn) {
			customCanvas.setColor("blue");
			current.setBackgroundResource(R.drawable.blue);
		} else if (v.getId() == R.id.orangebtn) {
			customCanvas.setColor("orange");
			current.setBackgroundResource(R.drawable.orange);
		} else if (v.getId() == R.id.purplebtn) {
			customCanvas.setColor("purple");
			current.setBackgroundResource(R.drawable.purple);
		} else if (v.getId() == R.id.yellowbtn) {
			customCanvas.setColor("yellow");
			current.setBackgroundResource(R.drawable.yellow);
		} else if (v.getId() == R.id.graybtn) {
			customCanvas.setColor("gray");
			current.setBackgroundResource(R.drawable.gray);
		} else if (v.getId() == R.id.eraserbtn) {
			customCanvas.setColor("white");
			current.setBackgroundResource(R.drawable.eraser);
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
						word.setText("");
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

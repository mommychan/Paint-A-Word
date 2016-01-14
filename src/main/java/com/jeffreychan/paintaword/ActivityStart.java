package com.jeffreychan.paintaword;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class ActivityStart extends Activity implements OnClickListener {

	Button button3;
	ImageView imgAssets;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_start);

		imgAssets = (ImageView) findViewById(R.id.imageView1);
		button3 = (Button) findViewById(R.id.button3);
		button3.setBackgroundColor(Color.BLACK);
		button3.setOnClickListener((android.view.View.OnClickListener) this);
		AssetManager assetManager = getAssets();


		try {
			// get input stream
			InputStream is = assetManager.open("drawing_title.png");

			// create drawable from stream
			Drawable d = Drawable.createFromStream(is, null);

			// set the drawable to imageview
			imgAssets.setImageDrawable(d);
		} catch (IOException e) {
			return;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem menuItem = menu.findItem(R.id.save_image);
		menuItem.setVisible(false);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		startActivity(new Intent(this, MainActivity.class));
	}


}

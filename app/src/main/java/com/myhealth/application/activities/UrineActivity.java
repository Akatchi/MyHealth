package com.myhealth.application.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.myhealth.application.R;
import com.myhealth.application.config.SessionManager;
import com.myhealth.application.config.Variables;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class UrineActivity extends Activity
{
    private static final int CAMERA_PIC_REQUEST = 1, QR_PIC_REQUEST = 2;
    private String mCurrentPhotoPath;
    private String mFilePath;

    private TextView mTv2, mTv3, mTv4, mTv5, mTv6, mTv7;
    private EditText mNaam, mKliniek, mTelnr, mEmail, mKlachten;
    private ImageView mUrinePhoto, mQRPhoto;
    private Button mSend;

    private SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urine);

        session = new SessionManager(getApplicationContext());

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(android.R.color.transparent);

        mTv2        = (TextView) findViewById(R.id.textView2);
        mTv3        = (TextView) findViewById(R.id.textView3);
        mTv4        = (TextView) findViewById(R.id.textView4);
        mTv5        = (TextView) findViewById(R.id.textView5);
        mTv6        = (TextView) findViewById(R.id.textView6);
        mTv7        = (TextView) findViewById(R.id.textView7);

        mNaam       = (EditText) findViewById(R.id.doctorName);
        mKliniek    = (EditText) findViewById(R.id.doctorKliniek);
        mTelnr      = (EditText) findViewById(R.id.doctorTelnr);
        mEmail      = (EditText) findViewById(R.id.doctorEmail);
        mKlachten   = (EditText) findViewById(R.id.textKlachten);

        mSend = (Button) findViewById(R.id.sendUrineTest);

        mQRPhoto = (ImageView) findViewById(R.id.scanQRCode);
        mUrinePhoto = (ImageView) findViewById(R.id.image_urine);

        //deactiveer de velden die nog niet ingevuld hoeven te worden ( die pas als het resultaat negatief is zichtbaar zijn )
        mTv2.setVisibility(View.GONE);
        mTv3.setVisibility(View.GONE);
        mTv4.setVisibility(View.GONE);
        mTv5.setVisibility(View.GONE);
        mTv6.setVisibility(View.GONE);
        mTv7.setVisibility(View.GONE);

        mNaam.setVisibility(View.GONE);
        mKliniek.setVisibility(View.GONE);
        mTelnr.setVisibility(View.GONE);
        mEmail.setVisibility(View.GONE);
        mKlachten.setVisibility(View.GONE);

        mSend.setVisibility(View.GONE);

        mQRPhoto.setVisibility(View.GONE);

        mUrinePhoto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dispatchTakePictureIntent();
            }
        });

        mQRPhoto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {

                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes

                    startActivityForResult(intent, QR_PIC_REQUEST);

                }
                catch (Exception e)
                {

                    Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
                    startActivity(marketIntent);

                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK)
        {
            mUrinePhoto.setImageBitmap(getScaledBitmap(mFilePath, 128, 128));

            if(analyzePhoto()) // bij true moet je naar de doctor dus geef de velden weer
            {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.negatief_resultaat), Toast.LENGTH_LONG).show();
                mTv2.setVisibility(View.VISIBLE);
                mTv3.setVisibility(View.VISIBLE);
                mTv4.setVisibility(View.VISIBLE);
                mTv5.setVisibility(View.VISIBLE);
                mTv6.setVisibility(View.VISIBLE);
                mTv7.setVisibility(View.VISIBLE);

                mNaam.setVisibility(View.VISIBLE);
                mKliniek.setVisibility(View.VISIBLE);
                mTelnr.setVisibility(View.VISIBLE);
                mEmail.setVisibility(View.VISIBLE);
                mKlachten.setVisibility(View.VISIBLE);

                mSend.setVisibility(View.VISIBLE);

                mQRPhoto.setVisibility(View.VISIBLE);
            }
            else
            {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.positief_resultaat), Toast.LENGTH_LONG).show();
            }

        }

        if( requestCode == QR_PIC_REQUEST && resultCode == RESULT_OK )
        {
            //TODO
            //QR Code gevonden vul nu de data van de huisarts in in de velden
            String contents = data.getStringExtra("SCAN_RESULT");
            Log.d("Content", contents);

            Toast.makeText(getApplicationContext(), contents, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean analyzePhoto()
    {
        //Todo: Test result bereken / randomen

        Random r = new Random();
        int i = r.nextInt(10);

        if( i > 5 ) { return false; }
        else        { return true;  }
    }

    public void sendUrineTest(View v)
    {
        String klachtenText = "";

        //Haal het doctor id op
        //Haal de user id/email op
        String userEmail = session.getUsername();
        //Haal de urine foto op
        File urinePhoto = new File(mFilePath);
        sendPhotoToServer(urinePhoto);
        //Haal de klachten op als die er zijn
        if( mKlachten.getText().toString().equals(getResources().getString(R.string.field_klachten)) )
        {
            klachtenText = mKlachten.getText().toString();
        }
        //Stuur de test door naar de webserver
    }

    private void sendPhotoToServer(File f)
    {
        String url = Variables.URINEPHOTOUPLOADURL;

        try
        {
            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost(url);

            InputStreamEntity reqEntity = new InputStreamEntity(new FileInputStream(f), -1);

            reqEntity.setContentType("binary/octet-stream");
            reqEntity.setChunked(true); // Send in multiple parts if needed
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);

            Toast.makeText(getApplicationContext(), "Status: " + response.getStatusLine(), Toast.LENGTH_SHORT).show();
            //Do something with response...

        } catch (Exception e) {
            // show error
        }
    }

    private File createImageFile() throws IOException
    {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "UT_" + timeStamp;
        File storageDir = new File(Environment.getExternalStorageDirectory()+File.separator + "MyHealth" + File.separator + "Urine Test");
        if( !storageDir.exists() ) { storageDir.mkdirs(); }

        File image = File.createTempFile
                (
                        imageFileName,  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir      /* directory */
                );

        mFilePath = image.getAbsolutePath();

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            // Create the File where the photo should go
            File photoFile = null;
            try
            {
                photoFile = createImageFile();
            }
            catch (IOException ex) { ex.printStackTrace(); }
            // Continue only if the File was successfully created
            if (photoFile != null)
            {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, CAMERA_PIC_REQUEST);
            }
        }
    }

    private Bitmap getScaledBitmap(String picturePath, int width, int height)
    {
        BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
        sizeOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, sizeOptions);

        int inSampleSize = calculateInSampleSize(sizeOptions, width, height);

        sizeOptions.inJustDecodeBounds = false;
        sizeOptions.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(picturePath, sizeOptions);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth)
        {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.urine, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch( item.getItemId() )
        {
            case android.R.id.home:
                Intent i = new Intent(this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}

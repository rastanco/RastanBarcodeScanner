package ir.rastanco.rastanbarcodescanner.presenter.BarcodeReading;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ir.rastanco.rastanbarcodescanner.R;
import ir.rastanco.rastanbarcodescanner.dataModel.Barcode;
import ir.rastanco.rastanbarcodescanner.dataModel.DataBaseHandler;
import ir.rastanco.rastanbarcodescanner.dataModel.FileInfo;
import ir.rastanco.rastanbarcodescanner.presenter.FilesManagment.MainActivity;

public class ChooseName extends AppCompatActivity implements OnItemSelectedListener {
    private ImageButton btnSave;
    private ListView fileNamesListView;
    private AutoCompleteTextView activeFileName;
    private DataBaseHandler dbHandler;
    private String fileContent;
    private ImageButton camera_btn;
    private Typeface font;
//    private FileInfo fileInfoSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_name);
        dbHandler=new DataBaseHandler(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        this.setToolbar();
        this.createPage();
        this.addClickedListViewItemFileNameToTextView();
    }

    private void addClickedListViewItemFileNameToTextView() {
         //textViewFileName = (AutoCompleteTextView)findViewById(R.id.actv_fileName);
         //textViewFileName.setText();
    }

    private void setToolbar() {
        camera_btn = (ImageButton)findViewById(R.id.choose_name_activity_camera);
        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseName.this,BarcodeReadingActivity.class));
            }
        });
        btnSave = (ImageButton) findViewById(R.id.appbar_barcode_displayer_check_btn);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChooseName.this);
                TextView title = new TextView(ChooseName.this);
                title.setText(getResources().getString(R.string.file_saved));
                title.setPadding(10, 10, 10, 10);
                title.setGravity(Gravity.CENTER);
                title.setTextColor(getResources().getColor(R.color.toolbar));
                title.setTextSize(23);
                font  = Typeface.createFromAsset(getAssets(), "yekan_font.ttf");
                title.setTypeface(font);
                builder.setItems(new CharSequence[]
                                {getResources().getString(R.string.confirm), getResources().getString(R.string.display_current_file),
                                        getResources().getString(R.string.share_current_file)},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        startActivity(new Intent(ChooseName.this, MainActivity.class));
                                        break;
                                    case 1:
                                        startActivity(new Intent(ChooseName.this, BarcodeDisplayer.class));
                                        break;
                                    case 2: {
                                        //TODO share current file
                                    }
                                    break;

                                }
                            }
                        });
                builder.setCustomTitle(title);
                builder.setCancelable(true);
                builder.create().show();
            }
        });

    }
    private void createPage() {
        fileNamesListView =(ListView) findViewById(R.id.lstv_fileName);
        activeFileName =(AutoCompleteTextView)findViewById(R.id.actv_fileName);

        this.addSpinner();
        ArrayList<String> fileNamesList = this.getAllFileNamesFromDB();
        this.handleListView(fileNamesList);
        this.setDefaultNameForActiveFileName();

    }

    private void setDefaultNameForActiveFileName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
        final String currentDateAndTime = sdf.format(new Date());
        activeFileName.setHint(currentDateAndTime);
    }

    private void handleListView(ArrayList<String> fileNamesList) {
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fileNamesList);
        this.fileNamesListView.setAdapter(listAdapter);

        activeFileName.setAdapter(listAdapter);
        activeFileName.setThreshold(1);

        this.fileNamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                AlertDialog.Builder builder = new AlertDialog.Builder(ChooseName.this);
                builder.setTitle(getResources().getString(R.string.file_exist));
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setItems(new CharSequence[]
                                {getResources().getString(R.string.replace),
                                        getResources().getString(R.string.add),
                                },
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0: {

                                    }


                                    break;
                                    case 1:{

                                    }

                                        break;


                                }
                            }
                        });
                builder.create().show();

            }
        });
    }

    private ArrayList<String> getAllFileNamesFromDB() {
        ArrayList<FileInfo> allFilesInfoList = dbHandler.selectAllFileInfo();
        final ArrayList<String> fileNamesList = new ArrayList<String>();
        for (int i = 0; i < allFilesInfoList.size(); i++)
            fileNamesList.add(allFilesInfoList.get(i).getFileName() + allFilesInfoList.get(i).getFileType());

        return fileNamesList;

    }

    private void addSpinner() {
        final Spinner spinner = (Spinner) findViewById(R.id.spin_fileType);
        spinner.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<String>();
        categories.add(".txt");
        categories.add(".xls");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

    }


//
//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (activeFileName.getText().toString().length()==0)
//                    activeFileName.setText(currentDateAndTime);
//                fileInfoSave = new FileInfo(activeFileName.getText().toString(),
//                        spinner.getSelectedItem().toString(),
//                        currentDateAndTime);
//                dbHandler.insertAFileInfo(fileInfoSave);
//                activeFileName.setText("");
//
//                //what is the filename?
//                //what is the fileContent?
//                //TODO for Shaiste:
//                //Please use this method to save your file on the external Storage of the phone!
//
//             //   this.saveToFile(fileName, fileContent);
//
//
//            }
//        });
    //}

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(ChooseName.this,MainActivity.class));
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void saveToFile(String name, String fileContent) {

        File file = null;
        File root = Environment.getExternalStorageDirectory();
        if (root.canWrite()) {
            File dir = new File(root.getAbsolutePath() + "/BarcodeScanner");
            dir.mkdirs();
            String fileName = name;
            file = new File(dir, fileName);
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                out.write(fileContent.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setAllBarcodesList(ArrayList<Barcode> allBarcode) {
        for(int i=0; i<allBarcode.size(); i++) {
            this.fileContent = allBarcode.get(i) + "\n";

        }
        //Log.v("all barcodes", fileContent);
    }

}

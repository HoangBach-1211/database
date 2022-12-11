package com.Nhom10.BTL;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.Nhom10.BTL.CustomAdapter.AdapterInHoaDon;
import com.Nhom10.BTL.DTO.ThanhToanDTO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;


public class inHoaDon extends Activity implements Runnable {


    protected static final String TAG = "TAG";
    List<ThanhToanDTO> thanhToanDTOList;
    String ngaygoi;
    long tongtien;
    GridView gridView;
    TextView twtt;
    AdapterInHoaDon adapterInHoaDon;
    List<ThanhToanDTO> listBA = new ArrayList<ThanhToanDTO>();
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    Button mScan, mPrint, mDisc;


    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;


    TextView stat;
    int printstat;

    LinearLayout layout;


    private ProgressDialog loading;


    AlertDialog.Builder builder;

    EditText customer_dtl,order_detail,total_price, agent_detail;


    TextView tvid;


    public void getthanhtoan(int magm){
        listBA.clear();
        String URL_GET_PRODUCT=config.domain +"android/getthanhtoan.php?magoimon="+magm;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL_GET_PRODUCT, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject item = response.getJSONObject(i);
                                ThanhToanDTO product = new ThanhToanDTO();
                                product.setHinhAnh(item.getString("hinhanh"));
                                product.setMaGoiMon(item.getInt("magoimon"));
                                product.setMaMonAn(item.getInt("mamonan"));
                                product.setTenMonAn(item.getString("tenmon"));
                                product.setSoLuong(item.getInt("soluong"));
                                product.setGiaTien(item.getInt("giatien"));
                                listBA.add(product);
                            }
                            thanhToanDTOList=listBA;
                            HienThiThanhToan();



                        } catch (Exception ex) {
                            Toast.makeText(inHoaDon.this, " x"+ ex.toString(), Toast.LENGTH_SHORT).show();
                        }
                        //                  loading.dismiss();

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(inHoaDon.this, ""+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }
    private void HienThiThanhToan(){
        tongtien=0;
        {


            for (int i=0; i < thanhToanDTOList.size() ; i++){
                int soluong = thanhToanDTOList.get(i).getSoLuong();
                int giatien = thanhToanDTOList.get(i).getGiaTien();

                tongtien += (soluong*giatien); // tongtien = tongtien + (soluong*giatien)
            }

            NumberFormat currentLocale = NumberFormat.getInstance();
            Locale localeEN = new Locale("en", "EN");
            NumberFormat en = NumberFormat.getInstance(localeEN);
            long longNumber = tongtien;
            String str1 = en.format(longNumber);
            twtt.setText(getResources().getString(R.string.tongcong) + str1 + " VND");
        }


        adapterInHoaDon = new AdapterInHoaDon(this,R.layout.inhoadon_icon,listBA);
        gridView.setAdapter(adapterInHoaDon);
        adapterInHoaDon.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inhoadon);
        gridView= findViewById(R.id.gvinhoadon);
        twtt=findViewById(R.id.txttongt);

        DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        dateFormatter.setLenient(false);
        Date today = new Date();
        ngaygoi = dateFormatter.format(today);

        Intent xx = getIntent();
        String magm = xx.getStringExtra("mgmon");
        //Toast.makeText(this, "mg"+ magm, Toast.LENGTH_SHORT).show();
        getthanhtoan(Integer.parseInt(magm));

        //Calligrapher calligrapher = new Calligrapher(this);
        //calligrapher.setFont(this,"fonts/abel-regular.ttf", true );
        stat = (TextView)findViewById(R.id.bpstatus);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        layout = (LinearLayout)findViewById(R.id.layout);

        customer_dtl = (EditText)findViewById(R.id.et_customer_details);

        mScan = (Button)findViewById(R.id.Scan);
        mScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mView) {

                if(mScan.getText().equals("Connect"))
                {
                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter == null) {
                        Toast.makeText(inHoaDon.this, "Phải kết nối máy in mới in được ", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!mBluetoothAdapter.isEnabled()) {
                            Intent enableBtIntent = new Intent(
                                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            startActivityForResult(enableBtIntent,
                                    REQUEST_ENABLE_BT);
                        } else {
                            ListPairedDevices();
                            Intent connectIntent = new Intent(inHoaDon.this,
                                    DeviceListActivity.class);
                            startActivityForResult(connectIntent,
                                    REQUEST_CONNECT_DEVICE);

                        }
                    }

                }
                else if(mScan.getText().equals("Disconnect"))
                {
                    if (mBluetoothAdapter != null)
                        mBluetoothAdapter.disable();
                    stat.setText("");
                    stat.setText("Disconnected");
                    stat.setTextColor(Color.rgb(199, 59, 59));
                    mPrint.setEnabled(false);
                    mScan.setEnabled(true);
                    mScan.setText("Connect");
                }
            }
        });






        mPrint = findViewById(R.id.mPrint);
        //mPrint.setTypeface(custom);
        mPrint.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {


                p1();

                int TIME = 10000; //5000 ms (5 Seconds)

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        p2(); //call function!

                        printstat = 1;
                    }
                }, TIME);


            }
        });

        /*mDisc = (Button) findViewById(R.id.dis);
        mDisc.setTypeface(custom);
        mDisc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {
                //connectDisconnect(mView);
                *//*if (mBluetoothAdapter != null)
                    mBluetoothAdapter.disable();
                stat.setText("");
                stat.setText("Disconnected");
                stat.setTextColor(Color.rgb(199, 59, 59));
                mPrint.setEnabled(false);
                mDisc.setEnabled(false);
                mDisc.setBackgroundColor(Color.rgb(161, 161, 161));
                mPrint.setBackgroundColor(Color.rgb(161, 161, 161));
                mScan.setBackgroundColor(Color.rgb(0,0,0));
                mScan.setEnabled(true);*//*


            }
        });*/

       /* final ScrollView sv = (ScrollView)findViewById(R.id.sv);
        final LinearLayout l1 = (LinearLayout)findViewById(R.id.l1);
        final LinearLayout l2 = (LinearLayout)findViewById(R.id.l2);
        final LinearLayout l3 = (LinearLayout)findViewById(R.id.l3);

        sv.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (sv != null) {
                    if (sv.getChildAt(0).getBottom() <= (sv.getHeight() + sv.getScrollY())) {

                        l1.setVisibility(View.VISIBLE);
                        l2.setVisibility(View.VISIBLE);
                        l3.setVisibility(View.VISIBLE);

                    }
                    else {
                        l1.setVisibility(View.GONE);
                        l2.setVisibility(View.GONE);
                        l3.setVisibility(View.GONE);


                    }
                }
            }
        });*/
    }//oncreate


    public void connectDisconnect(View view)
    {
        if(mScan.getText().toString() == "Connect")
        {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null) {
                Toast.makeText(inHoaDon.this, "Message1", Toast.LENGTH_SHORT).show();
            } else {
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(
                            BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent,
                            REQUEST_ENABLE_BT);
                } else {
                    ListPairedDevices();
                    Intent connectIntent = new Intent(inHoaDon.this,
                            DeviceListActivity.class);
                    startActivityForResult(connectIntent,
                            REQUEST_CONNECT_DEVICE);
                }
            }


        }else{
            if (mBluetoothAdapter != null)
            {
                mBluetoothAdapter.disable();
            }
            else{
                stat.setText("");
                stat.setText("Disconnected");
                stat.setTextColor(Color.rgb(199, 59, 59));
                mPrint.setEnabled(false);
                /*mDisc.setEnabled(false);
                mDisc.setBackgroundColor(Color.rgb(161, 161, 161));*/
                //mPrint.setBackgroundColor(Color.rgb(161, 161, 161));
                mScan.setBackgroundColor(Color.rgb(0,0,0));
                mScan.setEnabled(true);
                mScan.setText("Disconnect");
            }
        }

    }

    public void p1(){

        Thread t = new Thread() {
            public void run() {
                try {
                    OutputStream os = mBluetoothSocket
                            .getOutputStream();
                    String header = "";
                    String he = "";
                    String header2 = "";
                    String BILL = "";
                    String vio = "";
                    String header3 = "";
                    String mvdtail = "";
                    String header4 = "" ;
                    String offname = "";
                    String copy = "";
                    String checktop_status = "";

                    he = "      HÓA ĐƠN THANH TOÁN\n";
                    he = he +"********************************\n\n";

                    header =  "\n";
                    BILL = customer_dtl.getText().toString()+"\n";
                    BILL = BILL
                            + "================================\n";
                    header2= "Tên món          Đơn giá     SL\n";
                    String ds="";
                    for ( int i=0; i< listBA.size(); i++)
                    {
                        ds+=listBA.get(i).getTenMonAn()+ "        "+ listBA.get(i).getGiaTien()+"  " +listBA.get(i).getSoLuong() + "\n";

                    }
                    vio = ds+"\n";
                    vio = vio
                            + "================================\n";
                    header3 = "Tổng tiền:\n";
                    mvdtail = twtt.getText().toString()+"\n";
                    mvdtail = mvdtail
                            + "================================\n";

                    header4 = "Cảm ơn quý khách! Hẹn gặp lại ^^\n";
                    offname = ngaygoi+"\n";
                    offname = offname
                            + "--------------------------------\n";
                    copy = "\n\n\n\n\n";




                    os.write(he.getBytes());
                    os.write(header.getBytes());
                    os.write(BILL.getBytes());
                    os.write(header2.getBytes());
                    os.write(vio.getBytes());
                    os.write(header3.getBytes());
                    os.write(mvdtail.getBytes());
                    os.write(header4.getBytes());
                    os.write(offname.getBytes());
                    os.write(checktop_status.getBytes());
                    os.write(copy.getBytes());



                    //This is printer specific code you can comment ==== > Start

                    // Setting height
                    int gs = 29;
                    os.write(intToByteArray(gs));
                    int h = 104;
                    os.write(intToByteArray(h));
                    int n = 162;
                    os.write(intToByteArray(n));

                    // Setting Width
                    int gs_width = 29;
                    os.write(intToByteArray(gs_width));
                    int w = 119;
                    os.write(intToByteArray(w));
                    int n_width = 2;
                    os.write(intToByteArray(n_width));


                } catch (Exception e) {

                }
            }
        };
        t.start();
    }

    public void p2(){

        Thread tt = new Thread() {
            public void run() {
                try {
                    OutputStream os = mBluetoothSocket
                            .getOutputStream();
                    String header = "";
                    String he = "";
                    String header2 = "";
                    String BILL = "";
                    String vio = "";
                    String header3 = "";
                    String mvdtail = "";
                    String header4 = "" ;
                    String offname = "";
                    String copy = "";
                    String checktop_status = "";

                    he = "      HÓA ĐƠN THANH TOÁN\n";
                    he = he +"********************************\n\n";

                    header =  "Nhà hàng:\n";
                    BILL = customer_dtl.getText().toString()+"\n";
                    BILL = BILL
                            + "================================\n";
                    header2= "Tên món          Đơn giá     SL\n";
                    String ds1="";
                    for ( int i=0; i< listBA.size(); i++)
                    {
                        ds1+=listBA.get(i).getTenMonAn()+ "        "+ listBA.get(i).getGiaTien()+"  " +listBA.get(i).getSoLuong() + "\n";

                    }
                    vio = ds1+"\n";
                    vio = gridView.getAdapter().toString()+"\n";
                    vio = vio
                            + "================================\n";
                    header3 = "Tổng tiền:\n";
                    mvdtail = twtt.getText().toString()+"\n";
                    mvdtail = mvdtail
                            + "================================\n";

                    header4 = "Cảm ơn quý khách! Hẹn gặp lại\n";
                    offname = ngaygoi+"\n";
                    offname = offname
                            + "--------------------------------\n";
                    copy =  "\n\n\n\n\n";




                    os.write(he.getBytes());
                    os.write(header.getBytes());
                    os.write(BILL.getBytes());
                    os.write(header2.getBytes());
                    os.write(vio.getBytes());
                    os.write(header3.getBytes());
                    os.write(mvdtail.getBytes());
                    os.write(header4.getBytes());
                    os.write(offname.getBytes());
                    os.write(checktop_status.getBytes());
                    os.write(copy.getBytes());



                    //This is printer specific code you can comment ==== > Start

                    // Setting height
                    int gs = 29;
                    os.write(intToByteArray(gs));
                    int h = 104;
                    os.write(intToByteArray(h));
                    int n = 162;
                    os.write(intToByteArray(n));

                    // Setting Width
                    int gs_width = 29;
                    os.write(intToByteArray(gs_width));
                    int w = 119;
                    os.write(intToByteArray(w));
                    int n_width = 2;
                    os.write(intToByteArray(n_width));


                } catch (Exception e) {

                }
            }
        };
        tt.start();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {

        }
    }


    public void onActivityResult(int mRequestCode, int mResultCode,
                                 Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);

        switch (mRequestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (mResultCode == Activity.RESULT_OK) {
                    Bundle mExtra = mDataIntent.getExtras();
                    String mDeviceAddress = mExtra.getString("DeviceAddress");
                    Log.v(TAG, "Coming incoming address " + mDeviceAddress);
                    mBluetoothDevice = mBluetoothAdapter
                            .getRemoteDevice(mDeviceAddress);
                    mBluetoothConnectProgressDialog = ProgressDialog.show(this,
                            "Connecting...", mBluetoothDevice.getName() + " : "
                                    + mBluetoothDevice.getAddress(), true, false);
                    Thread mBlutoothConnectThread = new Thread(this);
                    mBlutoothConnectThread.start();
                    // pairToDevice(mBluetoothDevice); This method is replaced by
                    // progress dialog with thread
                }
                break;

            case REQUEST_ENABLE_BT:
                if (mResultCode == Activity.RESULT_OK) {
                    ListPairedDevices();
                    Intent connectIntent = new Intent(inHoaDon.this,
                            DeviceListActivity.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    Toast.makeText(inHoaDon.this, "Phải cho phép chứ! sao lại từ chối?", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void ListPairedDevices() {
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter
                .getBondedDevices();
        if (mPairedDevices.size() > 0) {
            for (BluetoothDevice mDevice : mPairedDevices) {
                Log.v(TAG, "Thiết bị đã ghép nối: " + mDevice.getName() + "  "
                        + mDevice.getAddress());
            }
        }
    }

    public void run() {
        try {
            mBluetoothSocket = mBluetoothDevice
                    .createRfcommSocketToServiceRecord(applicationUUID);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();
            mHandler.sendEmptyMessage(0);
        } catch (IOException eConnectException) {
            Log.d(TAG, "Không thể kết nối tới Socket", eConnectException);
            closeSocket(mBluetoothSocket);
            return;
        }
    }

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.d(TAG, "SocketClosed");
        } catch (IOException ex) {
            Log.d(TAG, "CouldNotCloseSocket");
        }
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBluetoothConnectProgressDialog.dismiss();

            // Snackbar snackbar = Snackbar.make(layout, "Bluetooth Printer is Connected!", Snackbar.LENGTH_LONG);
            // snackbar.show();
            stat.setText("");
            stat.setText("Connected");
            stat.setTextColor(Color.rgb(97, 170, 74));
            mPrint.setEnabled(true);
            mScan.setText("Disconnect");
            //mDisc.setEnabled(true);
            //mDisc.setBackgroundColor(Color.rgb(0, 0, 0));
            //mScan.setEnabled(false);
            //mScan.setBackgroundColor(Color.rgb(161, 161, 161));

        }
    };

    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();

        for (int k = 0; k < b.length; k++) {
            System.out.println("Selva  [" + k + "] = " + "0x"
                    + UnicodeFormatter.byteToHex(b[k]));
        }

        return b[3];
    }

    public byte[] sel(int val) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putInt(val);
        buffer.flip();
        return buffer.array();
    }




   
}

package com.keshavoneml.oneml_customer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class OrderDetails extends AppCompatActivity {

    TextView storesChosen;
    TextView addressDetail;
    TextView deliveryMode;

    ImageView prescription;

    public static String msg = "Registering your orders, please wait";


    String phone, quantity, name, address, pin, email;
    String vendorList, vendorName, addressCustomer;

    public String path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        addressDetail = findViewById(R.id.addressDetails);
        deliveryMode = findViewById(R.id.deliveryMode);
        storesChosen = findViewById(R.id.storeList);
        prescription = findViewById(R.id.prescriptionPreview);


        Intent i = getIntent();

        phone = i.getStringExtra("mobileNumber");
        path = i.getStringExtra("prescription_path");
        quantity = i.getStringExtra("quantityDetails");
        name = i.getStringExtra("name");
        address = i.getStringExtra("address");
        pin = i.getStringExtra("pinCode");
        email = i.getStringExtra("email");

        prescription.setImageBitmap(BitmapHelper.getInstance().getBitmap());

        vendorList = i.getStringExtra("perception_vendor"); // consisting of indices
        vendorName = i.getStringExtra("vendorName");
        addressCustomer = i.getStringExtra("perception_address_id");


        addressDetail.setText("NAME : " + name);
        addressDetail.append("\nMobile : " + phone);
        addressDetail.append("\nADDRESS : " + address);
        addressDetail.append("\nPIN : " + pin);
        addressDetail.append("\nEMAIL : " + email);
        addressDetail.append("\n\nBOOKED FROM : " + addressCustomer + "\n");

        deliveryMode.setText(quantity);
        deliveryMode.append("\n");


        storesChosen.setText(vendorName);

    }


    public void confirmOrder(View obj) {

        new AlertDialog.Builder(this)
                .setTitle("Order Confirmation")
                .setMessage("Do you want to confirm the orders ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        try {

                            GMailSender sender = new GMailSender("from", "password");
                            sender.sendMail("New Order for Medicines", "", "from",
                                    "to", path, phone, quantity, name, address, pin, email, vendorName, addressCustomer);
                            Toast.makeText(getApplicationContext(), "Order Booked !", Toast.LENGTH_SHORT).show();

                            // upload thing to DB here only
                            Intent i = new Intent(OrderDetails.this, ConfirmOrder.class);
                            startActivity(i);

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error while ordering details, try again", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();


    }
}


// gmailsender


class GMailSender extends javax.mail.Authenticator {


    private String mailhost = "smtp.gmail.com";
    //    private String mailhost = "smtp.zoho.com";
    private String user;
    private String password;
    private Session session;


    static {
        Security.addProvider(new com.provider.JSSEProvider());
    }

    public GMailSender(final String user, final String password) {
        this.user = user;
        this.password = password;

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", mailhost);
        props.setProperty("mail.smtp.ssl.enable", "true");

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "false");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }

    public synchronized void sendMail(String subject, String body, String sender, String recipients, String path,
                                      String phone, String quantity, String name, String address, String pin, String email,
                                      String vendors, String addressCustomer){
        try {

            int unique_id= (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
            System.out.println("1ML"+unique_id);
            String order_id = "1ML"+unique_id;


            MimeMessage message = new MimeMessage(session);
//            DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
//            message.setSender(new InternetAddress(sender));
//            message.setSubject(subject);
//            message.setDataHandler(handler);
//
//            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
//
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//
//            Transport.send(message);


            Multipart multipart = new MimeMultipart();
            message.setSubject(subject);

            MimeBodyPart attachPart = new MimeBodyPart();
            String attachFile = path;
            System.out.println(attachFile);
//            String attachFile = "/storage/emulated/0/OneML/1593344466886.jpg";//picture location

            DataSource source = new FileDataSource(attachFile);
            attachPart.setDataHandler(new DataHandler(source));
            attachPart.setFileName(new File(attachFile).getName());


//Trick is to add the content-id header here
            attachPart.setHeader("Content-ID", "image_id");
            multipart.addBodyPart(attachPart);

//third part for displaying image in the email body
            attachPart = new MimeBodyPart();

//            attachPart.setContent("<h1>New Medicine Order</h1>" +
//                    "<img src='cid:image_id'>", "text/html");

            attachPart.setContent("<h3>Order ID : </h3>"+ order_id + "<h3>Address Details : </h3>" + "  Name : " + name + "<br>  Address (to deliver) : " + address
                    + "<br>  PIN : " + pin + "<br>  Email : " + email + "<br>  Booked From : " + addressCustomer + "<br>  Mobile : " + phone
                    + "<br><br><h3>Medicine(s) Delivery Mode : </h3> " + quantity
                    + "<br><br><h3>Preferred Vendors : </h3> " + vendors
                    + "<img src='cid:image_id'>", "text/html");


            multipart.addBodyPart(attachPart);
//Set the multipart message to the email message
            message.setContent(multipart);

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
//
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);


            Transport.send(message);
            System.out.println("Done");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ByteArrayDataSource implements DataSource {
        private byte[] data;
        private String type;

        public ByteArrayDataSource(byte[] data, String type) {
            super();
            this.data = data;
            this.type = type;
        }

        public ByteArrayDataSource(byte[] data) {
            super();
            this.data = data;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContentType() {
            if (type == null)
                return "application/octet-stream";
            else
                return type;
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(data);
        }

        public String getName() {
            return "ByteArrayDataSource";
        }

        public OutputStream getOutputStream() throws IOException {
            throw new IOException("Not Supported");
        }
    }
}


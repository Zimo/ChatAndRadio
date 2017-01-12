package com.zimcoin.Chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zimo on 02/01/17.
 */
public class ChatRoomActivity extends AppCompatActivity{

    private DatabaseReference database_rootPtr;//get root of the firebase database. we will append chlids later
    private Button btn_send_msg;
    private EditText input_chat;
    private TextView output_chat;
    private String name, room_name;
    private String tempKey;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        btn_send_msg = (Button) findViewById(R.id.btnSend);
        input_chat = (EditText) findViewById(R.id.msgSend);
        output_chat = (TextView) findViewById(R.id.msgResponse);

        //get user_name and room_name from MainActivity
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // name is stored in the database
        name = user.getDisplayName();
        room_name = getIntent().getExtras().get("room_name").toString();

        database_rootPtr = FirebaseDatabase.getInstance().getReference().child(room_name); //getReference() returns a reference to the parent. child() give access to it's childs

        //Send messages
        btn_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //1 Create a first child (contains rendom key)
                Map<String, Object> map = new HashMap<String, Object>(); // our database is a set of (key value) => hashmap required (on cree une cle vide)
                database_rootPtr.updateChildren(map); // we have no information to store here
                tempKey = database_rootPtr.push().getKey(); // returns a unique key (String name). It is automatically generated (ex:RANDOMKEY-Q3DFWSD)

                //2 Create a a sub child (contains name, message)
                Map<String,Object> map2 = new HashMap<String, Object>(); // we need hashmap to save (user name, value) and (message name, texte)
                map2.put("Name",name);
                map2.put("MSG",input_chat.getText().toString());
                DatabaseReference message_root = database_rootPtr.child(tempKey);
                message_root.updateChildren(map2);

                input_chat.setText("");
            }
        });

        //Listen to database changes (get messages)
        database_rootPtr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) { // add a message to firebase

                    read_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { // change an existing message on firebase

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    private void read_chat_conversation(DataSnapshot dataSnapshot) {

        Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
        String printedName, printedMessage;
        while(child.hasNext())
        {
            printedMessage = (String) child.next().getValue(); //first child
            printedName = (String) child.next().getValue(); //second child

            //name = "UserNameToDo";
            output_chat.append(printedName+": "+printedMessage+"\n \n");
        }

    }

}

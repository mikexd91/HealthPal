package com.mikexd.healthpal.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.conversation.v1.Conversation;
import com.ibm.watson.developer_cloud.conversation.v1.model.InputData;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.mikexd.healthpal.Utilities.MessageListAdapter;
import com.mikexd.healthpal.Data.Message;
import com.mikexd.healthpal.R;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessageListAdapter mAdapter;
    private ArrayList messageArrayList;
    private EditText inputMessage;
    private ImageButton btnSend;

    private com.ibm.watson.developer_cloud.conversation.v1.model.Context context = null;
    private boolean initialRequest;
    private Context mContext;
    private String workspace_id;
    private String conversation_username;
    private String conversation_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mContext = getApplicationContext();
        conversation_username = mContext.getString(R.string.username);
        conversation_password = mContext.getString(R.string.password);
        workspace_id = mContext.getString(R.string.workspace);

        inputMessage = (EditText) findViewById(R.id.message);
        btnSend = (ImageButton) findViewById(R.id.btn_send);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        messageArrayList = new ArrayList<>();
        mAdapter = new MessageListAdapter(messageArrayList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        this.inputMessage.setText("");
        this.initialRequest = true;
        sendMessage();


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        inputMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    sendMessage();
                }
                return false;
            }
        });

    }

    // Sending a message to Watson Conversation Service
    private void sendMessage() {

        final String inputmessage = this.inputMessage.getText().toString().trim();
        if(!this.initialRequest) {
            Message inputMessage = new Message();
            inputMessage.setMessage(inputmessage);
            inputMessage.setId("1");
            messageArrayList.add(inputMessage);

        }
        else
        {
            Message inputMessage = new Message();
            inputMessage.setMessage(inputmessage);
            inputMessage.setId("100");
            this.initialRequest = false;

        }

        this.inputMessage.setText("");
        mAdapter.notifyDataSetChanged();

        Thread thread = new Thread(new Runnable(){
            public void run() {
                try {

                    Conversation service = new Conversation(Conversation.VERSION_DATE_2017_05_26);
                    service.setUsernameAndPassword(conversation_username, conversation_password);
                    InputData input = new InputData.Builder(inputmessage).build();
                    MessageOptions options = new MessageOptions.Builder(workspace_id).input(input).context(context).build();
                    MessageResponse response = service.message(options).execute();

                    //Passing Context of last conversation
                    if(response.getContext() !=null)
                    {
                        //context.clear();
                        context = response.getContext();

                    }
                    Message outMessage=new Message();
                    if(response!=null)
                    {
                        if(response.getOutput()!=null && response.getOutput().containsKey("text"))
                        {
                            ArrayList responseList = (ArrayList) response.getOutput().get("text");

//                            if(response.getIntents().size()>0){
//                                String intent = response.getIntents().get(0).getIntent();
//                                switch(intent){
//                                    case "bodypain":
//
//                                }
//                                if(intent.endsWith("bodypain")){
//
//                                }
//                            }

                            if(null !=responseList && responseList.size()>0){
                                outMessage.setMessage((String)responseList.get(0));
                                outMessage.setId("2");
                            }
                            messageArrayList.add(outMessage);
                        }

                        runOnUiThread(new Runnable() {
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                                if (mAdapter.getItemCount() > 1) {
                                    recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount()-1);

                                }

                            }
                        });


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }
}

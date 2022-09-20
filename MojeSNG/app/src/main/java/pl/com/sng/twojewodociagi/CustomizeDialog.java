package pl.com.sng.twojewodociagi;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import pl.com.sng.twojewodociagi.R;

/**
 * Created by PBronk on 08.02.2017.
 */

public class CustomizeDialog extends Dialog implements View.OnClickListener {
    Button okButton;
    Context mContext;
    TextView mTitle = null;

    View v = null;
    Button cancelButton;
    ProgressBar progressBar;

    public CustomizeDialog(Context context) {
        super(context);
        mContext = context;
        /** 'Window.FEATURE_NO_TITLE' - Used to hide the mTitle */
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        /** Design the dialog in main.xml file */
        setContentView(R.layout.at_sng_start);
        v = getWindow().getDecorView();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        v.setBackgroundResource(android.R.color.transparent);
        mTitle = (TextView) findViewById(R.id.dialogTitle);


        progressBar = (ProgressBar) findViewById(R.id.progress1);

    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mTitle.setText(title);
    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
        mTitle.setText(mContext.getResources().getString(titleId));
    }

    /**
     * Set the message text for this dialog's window.
     *
     * @param message
     *            - The new message to display in the title.
     */
    public void setMessage(CharSequence message) {

    }

    /**
     * Set the message ID
     *
     * @param messageId
     */
    public void setMessage(int messageId) {

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

}
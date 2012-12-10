package edu.colorado.trackers;

import edu.colorado.trackers.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
 
public class SendEmail extends DialogFragment {
 
	private String toAddress;
	private String subject;
	private String content;
    
	public SendEmail(String sub, String con) 
	{
		subject = sub;
		content = con;
	}
	
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Set an EditText view to get user input 
		final EditText input = new EditText(getActivity());
	    input.requestFocus();
	    input.post(new Runnable() {
	        public void run() {
	            input.requestFocus();
	            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
	            imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
	        }
	    });	    
	    
		builder.setView(input);
        builder.setMessage(R.string.email_message)
               .setPositiveButton(R.string.ok_email, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
             		  toAddress = input.getText().toString();
            		  send();
                   }
               })
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                   }
               });
        return builder.create();
    }
    	
	
	private void send() {
		  Intent email = new Intent(Intent.ACTION_SEND);
		  email.putExtra(Intent.EXTRA_EMAIL, new String[]{ toAddress});
		  email.putExtra(Intent.EXTRA_SUBJECT, subject);
		  email.putExtra(Intent.EXTRA_TEXT, content);

		  //need this to prompts email client only
		  email.setType("message/rfc822");
		  this.startActivity(Intent.createChooser(email, "Choose an Email client :"));
	}
	
}
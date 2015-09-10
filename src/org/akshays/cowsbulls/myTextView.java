package org.akshays.cowsbulls;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class myTextView extends TextView {

	public myTextView(Context context) {
		super(context);
		this.setText("Jai Bajrang Bali");
	}
	public myTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setText("Om Namah Shivay");
		this.setWidth((int)(getWidth()*0.25f));
	}
	
	public myTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        this.setText("Jai mata di");
    }
}

package com.dairam.android.fragments.activity.dialogfragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.dairam.android.fragments.R;
import com.dairam.android.fragments.activity.adapter.ProductImgDialogPageAdapter;
import com.dairam.android.fragments.activity.interfaces.OnShowProductImageDialog;
import com.dairam.android.fragments.activity.viewpager.CirclePageIndicator;

import java.util.List;

public class DialogProductImage extends DialogFragment{
	String TAG ="DialogProductImage"; 
	List<String> imageList;
	Context context;
	OnShowProductImageDialog showProduct;
	int selectedPosition;
	ImageButton btnClose;
	ViewPager pagerImgDialog/*, pagerSubImgDialog*/;
	ProductImgDialogPageAdapter productImgDialogPageAdapter;
	//CircleIndicator
	CirclePageIndicator indicator;
	//ImageView imageView;
	public void newInstance(Context context,List<String> imageList, int selectedImage){
		this.context =  context;
		this.imageList =  imageList;
		this.showProduct = showProduct;
		this.selectedPosition = selectedImage;
		
		Log.e(TAG,"selected Img Posiion : "+selectedPosition);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		final Dialog myDialog = new Dialog(getActivity());
		//myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		myDialog.setContentView(R.layout.dialog_product_image);

		myDialog.getWindow().getAttributes().windowAnimations = R.style.dialoganimation;
		myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		myDialog.getWindow().setLayout(getActivity().getResources().getDisplayMetrics().widthPixels, ViewGroup.LayoutParams.MATCH_PARENT);
		myDialog.getWindow().getAttributes().windowAnimations = R.style.dialoganimation;
		
		pagerImgDialog = (ViewPager) myDialog.findViewById(R.id.viewpagerProdImgDialog);
		indicator=(CirclePageIndicator) myDialog.findViewById(R.id.indicator);
		btnClose = (ImageButton) myDialog.findViewById(R.id.imageBtnProdDialogClose);
		/*pagerSubImgDialog = (AutoScrollViewPager) myDialog.findViewById(R.id.viewpagerProdImgDialog);*/
		btnClose.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				myDialog.dismiss();
			}
		});
		
		if(imageList.size() > 0){
			Log.e("DialogProductImage","imageList Size : "+imageList.size());
			productImgDialogPageAdapter =  new ProductImgDialogPageAdapter(getActivity(), imageList, selectedPosition);
			pagerImgDialog.setAdapter(productImgDialogPageAdapter);
			pagerImgDialog.setCurrentItem(selectedPosition);
			
			indicator.setViewPager(pagerImgDialog);
		}
		//Picasso.with(context).load(selectedImage).placeholder(R.drawable.loading320).into(imageView);
		
		return myDialog;
	}

}

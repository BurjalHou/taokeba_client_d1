package com.taokeba.ui;

import android.os.Bundle;

import com.taokeba.R;

import net.sf.andpdf.pdfviewer.PdfViewerActivity;

public class PDFReaderActivity extends PdfViewerActivity {

	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
		super.onCreate(savedInstanceState);
		
		
	}

	@Override
	public int getNextPageImageResource() {
		// TODO Auto-generated method stub
		return R.drawable.right_arrow;
	}

	@Override
	public int getPdfPageNumberEditField() {
		// TODO Auto-generated method stub
		return R.id.pagenum_edit;
	}

	@Override
	public int getPdfPageNumberResource() {
		// TODO Auto-generated method stub
		return R.layout.dialog_pagenumber;
	}

	@Override
	public int getPdfPasswordEditField() {
		// TODO Auto-generated method stub
		return R.id.etPassword;
	}

	@Override
	public int getPdfPasswordExitButton() {
		// TODO Auto-generated method stub
		return R.id.btExit;
	}

	@Override
	public int getPdfPasswordLayoutResource() {
		// TODO Auto-generated method stub
		return R.layout.pdf_file_password;
	}

	@Override
	public int getPdfPasswordOkButton() {
		// TODO Auto-generated method stub
		return R.id.btOK;
	}

	@Override
	public int getPreviousPageImageResource() {
		// TODO Auto-generated method stub
		return R.drawable.left_arrow;
	}

	@Override
	public int getZoomInImageResource() {
		// TODO Auto-generated method stub
		return R.drawable.zoom_in;
	}

	@Override
	public int getZoomOutImageResource() {
		// TODO Auto-generated method stub
		return R.drawable.zoom_out;
	}

}

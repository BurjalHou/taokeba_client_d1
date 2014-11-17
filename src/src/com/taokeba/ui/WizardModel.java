package com.taokeba.ui;

import android.content.Context;

import com.taokeba.widget.RegistBasicPage;
import com.taokeba.widget.RegistMorePage;
import com.taokeba.wizard.model.AbstractWizardModel;
import com.taokeba.wizard.model.BranchPage;
import com.taokeba.wizard.model.PageList;

/**
 * Created by zhaolin on 14-3-3.
 */
public class WizardModel extends AbstractWizardModel {

    public WizardModel(Context context) {
        super(context);
    }

    @Override
    protected PageList onNewRootPageList() {
        return new PageList(new BranchPage(this, "账户类型").addBranch(
                "学生",
                new RegistBasicPage(this, "基本信息"),
                new RegistMorePage(this, "学籍信息")
            )
        );
    }
}
